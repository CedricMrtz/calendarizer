package modelo;

import java.util.HashMap;
import java.util.Map;

public class PCB implements Comparable<PCB> {
    private final int pid;
    private final String nombre;
    private Estado estado;
    private final int prioridad;
    private final int tiempoLlegada;
    private final int tiempoRafaga;
    private int tiempoRestante;
    private int tiempoEspera;
    private int tiempoRetorno;
    private int tiempoInicio;
    private int tiempoFin;
    private int contadorAging;
    private final Map<String, Object> contextoCPU;

    public PCB(int pid, String nombre, Estado estado, int prioridad, int tiempoLlegada, int tiempoRafaga) {
        this.pid = pid;
        this.nombre = nombre;
        this.estado = estado;
        this.prioridad = prioridad;
        this.tiempoLlegada = tiempoLlegada;
        this.tiempoRafaga = tiempoRafaga;
        this.tiempoRestante = tiempoRafaga;
        this.tiempoEspera = 0;
        this.tiempoRetorno = 0;
        this.tiempoInicio = -1; // Indica que aun no ha comenzado
        this.tiempoFin = -1; // Indica que aun no ha terminado
        this.contadorAging = 0;
        this.contextoCPU = new HashMap<>();

    }

    public void transicionarEstado(Estado nuevo) {
        if (this.estado == Estado.TERMINADO) {
            throw new IllegalStateException("El proceso " + pid + " ya termino");
        }

        // Checa si la transicion es valida segun el estado actual
        boolean valido = 
        switch (this.estado) {
            case NUEVO -> nuevo == Estado.LISTO;
            case LISTO -> nuevo == Estado.EJECUTANDO;
            case EJECUTANDO -> nuevo == Estado.LISTO ||
                               nuevo == Estado.BLOQUEADO ||
                               nuevo == Estado.TERMINADO;
            case BLOQUEADO -> nuevo == Estado.LISTO;
            default -> false;
        };

        if (!valido) {
            throw new IllegalStateException(
                "Transicion invalida de " + this.estado + " a " + nuevo
            );
        }

        this.estado = nuevo;
    }
    
    public int getPrioridadEfectiva() {
        return prioridad - contadorAging;
    }   

    @Override
    public int compareTo(PCB o) {
        return Integer.compare(this.getPrioridadEfectiva(), o.getPrioridadEfectiva());
    }

    @Override
    public String toString() {
        return String.format(
            "PID: %d,\n Nombre: %s,\n Estado: %s,\n Prioridad: %d,\n Llegada: %d,\n Rafaga: %d,\n Restante: %d,\n Espera: %d,\n Fin: %d",
            pid, nombre, estado, prioridad, tiempoLlegada, tiempoRafaga, tiempoRestante, tiempoEspera, tiempoFin
        );
    }

    // Getters & Setters

    
    public int getPid() { 
        return pid; 
    }
    
    public String getNombre() { 
        return nombre;
    }
    
    public Estado getEstado() { 
        return estado; 
    }
    
    public int getPrioridad() {
        return prioridad; 
    }
    
    public int getTiempoLlegada() { 
        return tiempoLlegada; 
    }
    
    public int getTiempoRafaga() { 
        return tiempoRafaga; 
    }
    
    public int getTiempoRestante() { 
        return tiempoRestante; 
    }
    
    public int getTiempoEspera() { 
        return tiempoEspera; 
    }
    
    public int getTiempoRetorno() { 
        return tiempoRetorno; 
    }
    
    public int getTiempoInicio() { 
        return tiempoInicio; 
    }
    
    public int getTiempoFin() { 
        return tiempoFin; 
    }
    
    public int getContadorAging() { 
        return contadorAging; 
    }

    public Map<String, Object> getContextoCPU() {
        return contextoCPU;
    }

    public void setTiempoRestante(int v) {
        if (v < 0) throw new IllegalArgumentException("Tiempo restante no puede ser negativo");
        this.tiempoRestante = v;
    }
    
    public void incrementarTiempoEspera() { 
        this.tiempoEspera++; 
    }
    
    public void setTiempoRetorno(int v) {
        this.tiempoRetorno = v; 
    }
    
    public void setTiempoInicio(int v) { 
        this.tiempoInicio = v; 
    }
    
    public void setTiempoFin(int v) { 
        this.tiempoFin = v; 
    }
    
    public void incrementarContadorAging() { 
        this.contadorAging++; 
    }

}
