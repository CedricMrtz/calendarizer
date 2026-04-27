    package algoritmos;
    import java.util.*;
    import modelo.PCB;

public class CalendarizadorPrioridades extends CalendarizadorBase{
        private final int agingIntervalo;

        public CalendarizadorPrioridades(int agingIntervalo) {
            super("Prioridades con Aging", true);
            this.agingIntervalo = agingIntervalo;
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
        public boolean debeExpulsar(PCB actual, List<PCB> colaListos, int tiempoActual) {
            PCB mejor = seleccionarProceso(colaListos, tiempoActual);

            return mejor != null &&
                prioridadEfectiva(mejor) < prioridadEfectiva(actual);
        }
    }
