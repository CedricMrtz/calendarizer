package simulacion;

import algoritmos.Calendarizador;
import java.util.ArrayList;
import java.util.List;
import modelo.PCB;
import modelo.ResultadoSimulacion;

public class Simulador {

  private final Calendarizador calendarizador;
  private final GestorColas gestor;
  private final int intervalAging;

  private final GanttRenderer gantt = new GanttRenderer();
  private int cambiosContexto = 0;
  private int ticksCPUOcupada = 0;
  private int tiempoActual = 0;
  private PCB ultimoEjecutado = null;

  public Simulador(Calendarizador calendarizador, List<PCB> procesos, int intervalAging) {
    this.calendarizador = calendarizador;
    this.gestor = new GestorColas();
    this.intervalAging = intervalAging;

    List<Integer> pids = new ArrayList<>();
    for (PCB p : procesos) {
      if (pids.contains(p.getPid())) {
        throw new IllegalArgumentException("PID duplicado: " + p.getPid());
      }
      pids.add(p.getPid());
      gestor.cargarProceso(p);
    }
  }

  public ResultadoSimulacion ejecutar() {
    System.out.println("Iniciando simulación: " + calendarizador.getNombre() + " \n");
    while (gestor.hayProcesosActivos()) {
      admitirYDesbloquear();
      evaluarExpulsion();
      asignarCPUSiLibre();
      ejecutarTick();
      gestor.actualizarEspera(intervalAging);
      tiempoActual++;
    }
    gantt.imprimir();
    return construirResultado();
  }

  private void admitirYDesbloquear() {
    gestor.admitirProcesos(tiempoActual);
    gestor.actualizarBloqueados();
    gestor.admitirProcesos(tiempoActual);
  }

  private void evaluarExpulsion() {
    PCB enEjecucion = gestor.getEnEjecucion();
    if (enEjecucion != null && calendarizador.esApropiativo()) {
      if (calendarizador.debeExpulsar(enEjecucion, gestor.getColaListos(), tiempoActual)) {
        gestor.expulsarAListos();
        cambiosContexto++;
      }
    }
  }

  private void asignarCPUSiLibre() {
    if (gestor.getEnEjecucion() == null && !gestor.getColaListos().isEmpty()) {
      PCB siguiente = calendarizador.seleccionarProceso(gestor.getColaListos(), tiempoActual);
      if (siguiente != null) {
        gestor.asignarCPU(siguiente, tiempoActual);
        if (ultimoEjecutado != null) {
          cambiosContexto++;
        }
        ultimoEjecutado = siguiente;
      }
    }
  }

  private void ejecutarTick() {
    PCB enEjecucion = gestor.getEnEjecucion();
    if (enEjecucion != null) {
      enEjecucion.setTiempoRestante(enEjecucion.getTiempoRestante() - 1);
      gantt.registrarTick("P" + enEjecucion.getPid());
      ticksCPUOcupada++;
      if (enEjecucion.getTiempoRestante() == 0) {
        gestor.terminarProceso(tiempoActual + 1);
      }
    } else {
      gantt.registrarTick("--");
    }
  }

  private ResultadoSimulacion construirResultado() {
    List<PCB> terminados = gestor.getColaTerminados();

    double promedioEspera = terminados.stream().mapToInt(PCB::getTiempoEspera).average().orElse(0);
    double promedioRetorno = terminados.stream().mapToInt(PCB::getTiempoRetorno).average().orElse(0);
    double usoCPU = tiempoActual > 0 ? (ticksCPUOcupada * 100.0 / tiempoActual) : 0;
    System.out.println("\n--- TABLA DE PROCESOS ---");

    for (PCB p : terminados) {
      System.out.println(
          "P" + p.getPid() +
              " | Llegada: " + p.getTiempoLlegada() +
              " | Ráfaga: " + p.getTiempoRafaga() +
              " | Inicio: " + p.getTiempoInicio() +
              " | Fin: " + p.getTiempoFin() +
              " | Espera: " + p.getTiempoEspera() +
              " | Retorno: " + p.getTiempoRetorno());
    }

    return new ResultadoSimulacion(
        calendarizador.getNombre(),
        promedioEspera,
        promedioRetorno,
        usoCPU,
        cambiosContexto);
  }
}
