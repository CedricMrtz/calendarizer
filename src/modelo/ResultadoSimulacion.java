package modelo;

public class ResultadoSimulacion {
  private final String nombreAlgoritmo;
  private final double esperaPromedio;
  private final double retornoPromedio;
  private final double usoCPU;
  private final int cambiosContexto;

  public ResultadoSimulacion(String nombreAlgoritmo, double esperaPromedio, double retornoPromedio, double usoCPU,
      int cambiosContexto) {
    this.nombreAlgoritmo = nombreAlgoritmo;
    this.esperaPromedio = esperaPromedio;
    this.retornoPromedio = retornoPromedio;
    this.usoCPU = usoCPU;
    this.cambiosContexto = cambiosContexto;
  }

  public void imprimir() {
    System.out.printf("Algoritmo: %s\n", nombreAlgoritmo);
    System.out.printf("Tiempo de espera promedio: %.2f\n", esperaPromedio);
    System.out.printf("Tiempo de retorno promedio: %.2f\n", retornoPromedio);
    System.out.printf("Uso de CPU: %.2f%%\n", usoCPU);
    System.out.printf("Cambios de contexto: %d\n", cambiosContexto);
  }

  // Getters

  public String getNombreAlgoritmo() {
    return nombreAlgoritmo;
  }

  public int getCambiosContexto() {
    return cambiosContexto;
  }

  public double getEsperaPromedio() {
    return esperaPromedio;
  }

  public double getRetornoPromedio() {
    return retornoPromedio;
  }

  public double getUsoCPU() {
    return usoCPU;
  }

}
