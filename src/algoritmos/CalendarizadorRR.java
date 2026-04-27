package algoritmos;
import modelo.PCB;
import java.util.List;

public class CalendarizadorRR extends CalendarizadorBase{

    private int quantum;
    private int contador;

public CalendarizadorRR(int quantum) {
    super("Round Robin", true);
    this.quantum = quantum;
}
    @Override
    public PCB seleccionarProceso(List<PCB> colaListos, int tiempoActual) {
        if (colaListos.isEmpty()) return null;
        return colaListos.get(0);
    }

    @Override
    public boolean debeExpulsar(PCB actual, List<PCB> colaListos, int tiempoActual) {
        contador++;

        if (contador >= quantum) {
            contador = 0;
            return true;
        }

        return false;
    }
}