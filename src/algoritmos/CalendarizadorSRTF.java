package algoritmos;
import java.util.List;
import modelo.PCB;

public class CalendarizadorSRTF extends CalendarizadorBase {

    public CalendarizadorSRTF() {
        super("Shortest Remaining Time First (SRTF)", true);
    }

    @Override
    public PCB seleccionarProceso(List<PCB> colaListos, int tiempoActual) {
        if (colaListos.isEmpty()) {
            return null;
        }
        // Selecciona el proceso con el tiempo restante mas bajo, si llegan al mismo tiempo, selecciona el de PID mas bajo
        PCB procesoSeleccionado = null;
        for (PCB pcb : colaListos) {
            if (procesoSeleccionado == null || 
                pcb.getTiempoRestante() < procesoSeleccionado.getTiempoRestante() ||
                (pcb.getTiempoRestante() == procesoSeleccionado.getTiempoRestante() && 
                pcb.getPid() < procesoSeleccionado.getPid())) {
                procesoSeleccionado = pcb;
            }
        }
        return procesoSeleccionado;
    }
    @Override
    public boolean debeExpulsar(PCB enEjecucion, List<PCB> colaListos, int tiempoActual) {
      if (enEjecucion == null || colaListos.isEmpty()) {
          return false;
      }

      // Expulsa si hay un evento con menos tiempo restante 
      for (PCB pcb : colaListos) {
          if (pcb.getTiempoRestante() < enEjecucion.getTiempoRestante()) {
              return true;
          }
      }
      return false;
    }
}
