package algoritmos;
import java.util.List;
import modelo.PCB;

public class CalendarizadorFCFS extends CalendarizadorBase {

    public CalendarizadorFCFS() {
        super("First-Come, First-Served (FCFS)", false);
    }

    @Override
    public PCB seleccionarProceso(List<PCB> colaListos, int tiempoActual) {
        if (colaListos.isEmpty()) {
            return null;
        }
        // Selecciona el proceso con el tiempo de llegada mas bajo, si llegan al mismo tiempo, selecciona el de PID mas bajo
        PCB procesoSeleccionado = null;
        for (PCB pcb : colaListos) {
            if (procesoSeleccionado == null || 
                pcb.getTiempoLlegada() < procesoSeleccionado.getTiempoLlegada() ||
                (pcb.getTiempoLlegada() == procesoSeleccionado.getTiempoLlegada() && 
                pcb.getPid() < procesoSeleccionado.getPid())) {
                procesoSeleccionado = pcb;
            }        }
        return procesoSeleccionado;
    }
}
