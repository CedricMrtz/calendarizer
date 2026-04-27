import algoritmos.Calendarizador;
import algoritmos.CalendarizadorFCFS;
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
    String ruta = "./casos_de_prueba/caso1_fcfs_convoy.txt";
    Calendarizador algoritmo = new CalendarizadorFCFS();
    //Calendarizador algoritmo = new CalendarizadorRR(2); // Quantum de 2

    List<PCB> procesos = new ArrayList<>();
    BufferedReader br = new BufferedReader(new FileReader(ruta));
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
    br.close();

    Simulador simulador = new Simulador(algoritmo, procesos, 0);
    ResultadoSimulacion resultado = simulador.ejecutar();
    resultado.imprimir();
  }
}
