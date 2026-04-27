package simulacion;

import java.util.*;
import modelo.Estado;
import modelo.PCB;

public class GestorColas {
  private final Queue<PCB> colaNuevos;
  private final List<PCB> colaListos;
  private PCB enEjecucion;
  private final List<PCB> colaBloqueados;
  private final List<PCB> colaTerminados;
  private final Map<Integer, Integer> tiempoIORestante;

  public GestorColas() {
    this.colaNuevos = new LinkedList<>();
    this.colaListos = new ArrayList<>();
    this.enEjecucion = null;
    this.colaBloqueados = new ArrayList<>();
    this.colaTerminados = new ArrayList<>();
    this.tiempoIORestante = new HashMap<>();
  }

  public void cargarProceso(PCB proceso) {
    if (proceso.getEstado() != Estado.NUEVO) {
      throw new IllegalArgumentException(
          "El proceso debe de estar en nuevo");
    }
    colaNuevos.add(proceso);
  }

  public void admitirProcesos(int tiempoActual) {
    while (!colaNuevos.isEmpty() && colaNuevos.peek().getTiempoLlegada() <= tiempoActual) {
      PCB proceso = colaNuevos.poll();
      proceso.transicionarEstado(Estado.LISTO);
      colaListos.add(proceso);
    }
  }

  public void asignarCPU(PCB proceso, int tiempoActual) {
    if (!colaListos.contains(proceso)) {
      throw new IllegalArgumentException(
          "El proceso no esta en la cola de listos");
    }
    colaListos.remove(proceso);
    proceso.transicionarEstado(Estado.EJECUTANDO);

    if (proceso.getTiempoInicio() == -1) {
      proceso.setTiempoInicio(tiempoActual);
    }

    enEjecucion = proceso;
  }

  public void expulsarAListos() {
    if (enEjecucion == null) {
      throw new IllegalStateException("No hay proceso en ejecución para expulsar");
    }
    enEjecucion.transicionarEstado(Estado.LISTO);
    colaListos.add(enEjecucion);
    enEjecucion = null;
  }

  public void terminarProceso(int tiempoActual) {
    if (enEjecucion == null) {
      throw new IllegalStateException("No hay proceso en ejecución para terminar");
    }
    enEjecucion.transicionarEstado(Estado.TERMINADO);
    enEjecucion.setTiempoFin(tiempoActual);
    enEjecucion.setTiempoRetorno(tiempoActual - enEjecucion.getTiempoLlegada());
    colaTerminados.add(enEjecucion);
    enEjecucion = null;
  }

  public void liberarCPU() {
    if (enEjecucion == null) {
      throw new IllegalStateException("No hay proceso en ejecución");
    }
    enEjecucion = null;
  }

  public void bloquearProceso(int tiempoIO) {
    if (enEjecucion == null) {
      throw new IllegalStateException("No hay proceso en ejecución para bloquear");
    }
    enEjecucion.transicionarEstado(Estado.BLOQUEADO);
    tiempoIORestante.put(enEjecucion.getPid(), tiempoIO);
    colaBloqueados.add(enEjecucion);
    enEjecucion = null;
  }

  public void actualizarBloqueados() {
    Iterator<PCB> it = colaBloqueados.iterator();
    while (it.hasNext()) {
      PCB proceso = it.next();
      int restante = tiempoIORestante.get(proceso.getPid()) - 1;
      if (restante <= 0) {
        tiempoIORestante.remove(proceso.getPid());
        proceso.transicionarEstado(Estado.LISTO);
        colaListos.add(proceso);
        it.remove();
      } else {
        tiempoIORestante.put(proceso.getPid(), restante);
      }
    }
  }

  public void actualizarEspera(int intervalAging) {
    for (PCB proceso : colaListos) {
      proceso.incrementarTiempoEspera();
      if (intervalAging > 0 && proceso.getTiempoEspera() % intervalAging == 0) {
        proceso.incrementarContadorAging();
      }
    }
  }

  // terminar si no hay nada
  public boolean hayProcesosActivos() {
    return !colaNuevos.isEmpty()
        || !colaListos.isEmpty()
        || enEjecucion != null
        || !colaBloqueados.isEmpty();
  }

  public Queue<PCB> getColaNuevos() {
    return colaNuevos;
  }

  public List<PCB> getColaListos() {
    return colaListos;
  }

  public PCB getEnEjecucion() {
    return enEjecucion;
  }

  public List<PCB> getColaBloqueados() {
    return colaBloqueados;
  }

  public List<PCB> getColaTerminados() {
    return colaTerminados;
  }
}
