package algoritmos;

import java.util.List;
import modelo.PCB;

public interface Calendarizador {
    String getNombre();
    PCB seleccionarProceso(List<PCB> colaListos, int tiempoActual);
    boolean esApropiativo();
    boolean debeExpulsar(PCB enEjecucion, List<PCB> colaListos, int tiempoActual);
}
