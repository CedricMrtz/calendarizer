import algoritmos.Calendarizador;
import algoritmos.CalendarizadorFCFS;
import algoritmos.CalendarizadorPrioridades;
import algoritmos.CalendarizadorRR;
import algoritmos.CalendarizadorSJF;
import algoritmos.CalendarizadorSRTF;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import modelo.Estado;
import modelo.PCB;
import modelo.ResultadoSimulacion;
import simulacion.Simulador;

public class Main {
  public static void main(String[] args) throws Exception {
    String ruta1 = "./casos_de_prueba/caso1_fcfs_convoy.txt";
    String ruta2 = "./casos_de_prueba/caso2_sjf_inanicion.txt";
    String ruta3 = ruta1;
    String ruta4 = "./casos_de_prueba/caso4_prioridades.txt";
    String ruta5 = "./casos_de_prueba/caso5_libre.txt";

    List<Calendarizador> algoritmos = List.of(
        new CalendarizadorFCFS(),
        new CalendarizadorSJF(),
        new CalendarizadorRR(2), // Quantum de 2
        new CalendarizadorRR(10), // Quantum de 10
        new CalendarizadorPrioridades(5), // Aging de 5
        new CalendarizadorSRTF());

    // Caso 1
    simular(ruta1, algoritmos.get(0));
    System.out.println();
    // Caso 2
    simular(ruta2, algoritmos.get(1));
    System.out.println();
    // Caso 3.1
    simular(ruta3, algoritmos.get(2));
    System.out.println();
    // Caso 3.2
    simular(ruta3, algoritmos.get(3));
    System.out.println();
    // Caso 4
    simular(ruta4, algoritmos.get(4));
    System.out.println();
    // Casp 5
    simular(ruta5, algoritmos.get(5));
  }

  private static ResultadoSimulacion simular(String ruta, Calendarizador algoritmo)
      throws Exception {
    List<PCB> procesos = cargarProcesos(ruta);
    Simulador simulador = new Simulador(algoritmo, procesos);

    ResultadoSimulacion resultado = simulador.ejecutar();
    resultado.imprimir();

    return resultado;
  }

  private static List<PCB> cargarProcesos(String ruta) throws Exception {
    List<PCB> procesos = new ArrayList<>();
    try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
      String linea;
      while ((linea = br.readLine()) != null) {
        if (linea.isBlank() || linea.startsWith("#"))
          continue;

        String[] p = linea.split(",");
        int pid = Integer.parseInt(p[0].trim());
        String nombre = p[1].trim();
        int llegada = Integer.parseInt(p[2].trim());
        int rafaga = Integer.parseInt(p[3].trim());
        int prioridad = Integer.parseInt(p[4].trim());

        procesos.add(new PCB(pid, nombre, Estado.NUEVO, prioridad, llegada, rafaga));
      }
    }
    return procesos;
  }
}
