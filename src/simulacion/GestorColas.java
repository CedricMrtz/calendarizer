
public class GestorColas {
  private Queue<PCB> colaNuevos;
  private List<PCB> colaListos;
  private PCB enEjecucion;
  private List<PCB> colaBloqueados;
  private List<PCB> colaTerminados

  public GestorColas() {
    this.colaNuevos = new LinkedList<>();
    this.colaListos = new ArrayList<>();
    this.enEjecucion = null;
    this.colaBloqueados = new ArrayList<>();
    this.colaTerminados = new ArrayList<>();
  }

  public void cargarProceso(PCB proceso) {
    if (proceso.getEstado() != Estado.NUEVO) {
      throw new IllegalArgumentException("El proceso debe estar en estado NUEVO para ser cargado");
    }
    colaNuevos.add(proceso);
  }

  public void admitirProceso(int tiempoActual) {
    while (!colaNuevos.isEmpty() && colaNuevos.peek().getTiempoLlegada() <= tiempoActual) {
      PCB proceso = colaNuevos.poll();
      proceso.setEstado(Estado.LISTO);
      colaListos.add(proceso);
    }
  }

  // TODO: Cambiar esto por que tiene un error
  public void asignarCPU(PCB proceso) {
    if (proceso.getEstado() != Estado.LISTO) {
      throw new IllegalArgumentException("El proceso debe estar en estado LISTO para ser ejecutado");
    }
    enEjecucion = proceso;
    enEjecucion.setEstado(Estado.EJECUTANDO);
  }

  public void liberarCPU() {
    if (enEjecucion == null) {
      throw new IllegalStateException("No hay proceso");
    }
    enEjecucion = null;
  }
}
