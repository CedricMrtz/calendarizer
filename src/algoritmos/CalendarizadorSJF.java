package algoritmos;
import java.util.List;
import modelo.PCB;

public class CalendarizadorSJF extends CalendarizadorBase {

    public CalendarizadorSJF() {
        super("Shortest Job First (SJF)", false);
    }

    @Override
    public PCB seleccionarProceso(List<PCB> colaListos, int tiempoActual) {
        if (colaListos.isEmpty()) {
            return null;
        }
        // Selecciona el proceso con el tiempo restante mas bajo, si tienen el mismo tiempo restante, selecciona el de PID mas bajo
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
}
