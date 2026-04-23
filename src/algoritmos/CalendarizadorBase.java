package algoritmos;

import java.util.List;
import modelo.PCB;


public abstract class CalendarizadorBase implements Calendarizador {
    private final String nombre;
    private final boolean apropiativo;

    public CalendarizadorBase(String nombre, boolean apropiativo) {
        this.nombre = nombre;
        this.apropiativo = apropiativo;
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public boolean esApropiativo() {
        return apropiativo;
    }

    @Override
    public boolean debeExpulsar(PCB enEjecucion, List<PCB> colaListos, int tiempoActual) {
        return false;
    }
}
