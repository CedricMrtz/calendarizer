package simulacion;

import algoritmos.Calendarizador;
import modelo.Estado;
import modelo.PCB;
import modelo.ResultadoSimulacion;

import java.util.ArrayList;
import java.util.List;

public class Simulador {

  private final Calendarizador calendarizador;
  private final GestorColas gestor;
  private final int intervalAging;

  private final List<String> gantt = new ArrayList<>();
  private int cambiosContexto = 0;
  private int ticksCPUOcupada = 0;
  private int tiempoActual = 0;

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
      gestor.admitirProcesos(tiempoActual);

      gestor.actualizarBloqueados();

      gestor.admitirProcesos(tiempoActual);

      PCB enEjecucion = gestor.getEnEjecucion();

      if (enEjecucion != null && calendarizador.esApropiativo()) {
        if (calendarizador.debeExpulsar(enEjecucion, gestor.getColaListos(), tiempoActual)) {
          gestor.expulsarAListos();
          cambiosContexto++;
          enEjecucion = null;
        }
      }

      if (gestor.getEnEjecucion() == null && !gestor.getColaListos().isEmpty()) {
        PCB siguiente = calendarizador.seleccionarProceso(gestor.getColaListos(), tiempoActual);
        if (siguiente != null) {
          gestor.asignarCPU(siguiente, tiempoActual);
          cambiosContexto++;
        }
      }

      enEjecucion = gestor.getEnEjecucion();
      if (enEjecucion != null) {
        enEjecucion.setTiempoRestante(enEjecucion.getTiempoRestante() - 1);
        gantt.add("P" + enEjecucion.getPid());
        ticksCPUOcupada++;

        if (enEjecucion.getTiempoRestante() == 0) {
          gestor.terminarProceso(tiempoActual + 1);
          cambiosContexto++;
        }
      } else {
        gantt.add("--");
      }

      gestor.actualizarEspera(intervalAging);

      tiempoActual++;
    }

    return construirResultado();
  }

  private ResultadoSimulacion construirResultado() {
    List<PCB> terminados = gestor.getColaTerminados();

    double promedioEspera = terminados.stream().mapToInt(PCB::getTiempoEspera).average().orElse(0);
    double promedioRetorno = terminados.stream().mapToInt(PCB::getTiempoRetorno).average().orElse(0);
    double usoCPU = tiempoActual > 0 ? (ticksCPUOcupada * 100.0 / tiempoActual) : 0;

    return new ResultadoSimulacion(
        calendarizador.getNombre(),
        promedioEspera,
        promedioRetorno,
        usoCPU,
        cambiosContexto);
  }
}
