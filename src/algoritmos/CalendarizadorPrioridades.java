package algoritmos;
import modelo.PCB;
import java.util.*;

public class CalendarizadorPrioridades implements Calendarizador {

    private int agingIntervalo;

    public CalendarizadorPrioridades(int agingIntervalo) {
        this.agingIntervalo = agingIntervalo;
    }

    @Override
    public String getNombre() {
        return "Prioridades con Aging";
    }

    @Override
    public PCB seleccionarProceso(List<PCB> colaListos, int tiempoActual) {
        return colaListos.stream()
                .min(Comparator.comparingInt(this::prioridadEfectiva))
                .orElse(null);
    }

    private int prioridadEfectiva(PCB p) {
        int mejora = p.getContadorAging() / agingIntervalo;
        return p.getPrioridad() - mejora;
    }

    @Override
    public boolean esApropiativo() {
        return true;
    }

    @Override
    public boolean debeExpulsar(PCB actual, List<PCB> colaListos, int tiempoActual) {
        PCB mejor = seleccionarProceso(colaListos, tiempoActual);

        return mejor != null &&
               prioridadEfectiva(mejor) < prioridadEfectiva(actual);
    }
}