package algoritmos;

import java.util.List;
import modelo.PCB;

public class CalendarizadorRR extends CalendarizadorBase {

    private final int quantum;
    private int contador;
    private PCB ultimoProceso;

    public CalendarizadorRR(int quantum) {
        super("Round Robin", true);
        this.quantum = quantum;
        this.contador = 0;
        this.ultimoProceso = null;
    }

    @Override
    public PCB seleccionarProceso(List<PCB> colaListos, int tiempoActual) {
        if (colaListos.isEmpty()) return null;
        return colaListos.get(0);
    }

    @Override
    public boolean debeExpulsar(PCB actual, List<PCB> colaListos, int tiempoActual) {

        // Si cambió el proceso, reinicia contador
        if (ultimoProceso != actual) {
            contador = 0;
            ultimoProceso = actual;
        }

        contador++;

        if (contador >= quantum) {
            contador = 0;
            return true;
        }

        return false;
    }
}
