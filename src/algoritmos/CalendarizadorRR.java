package algoritmos;
import modelo.PCB;
import java.util.List;

public class CalendarizadorRR implements Calendarizador {

    private int quantum;
    private int contador;

    public CalendarizadorRR(int quantum) {
        this.quantum = quantum;
        this.contador = 0;
    }

    @Override
    public String getNombre() {
        return "Round Robin";
    }

    @Override
    public PCB seleccionarProceso(List<PCB> colaListos, int tiempoActual) {
        if (colaListos.isEmpty()) return null;
        return colaListos.get(0);
    }

    @Override
    public boolean esApropiativo() {
        return true;
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