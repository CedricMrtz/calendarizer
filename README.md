# Process Scheduler Simulator (Java)

This project implements a process scheduling simulator in Java as part of an Operating Systems course.

## Objective
The goal is to model how an operating system manages processes using different CPU scheduling algorithms. The simulator will represent processes using a Process Control Block (PCB), handle process queues, and evaluate performance metrics.

## Tech Stack
- Java 14+ (recommended Java 17)
- Standard Java libraries only

## Compile and Run
From the project root:

```bash
javac -d bin -sourcepath src src/Main.java
java -cp bin Main
```

## UML Diagram

```mermaid
classDiagram
    direction TB

    namespace modelo {
        class Estado {
            <<enumeration>>
            NUEVO
            LISTO
            EJECUTANDO
            BLOQUEADO
            TERMINADO
        }

        class PCB {
            -int pid
            -String nombre
            -Estado estado
            -int prioridad
            -int tiempoLlegada
            -int tiempoRafaga
            -int tiempoRestante
            -int tiempoEspera
            -int tiempoRetorno
            -int tiempoInicio
            -int tiempoFin
            -int contadorAging
            -Map~String, Object~ contextoCPU
            +PCB(int pid, String nombre, Estado estado, int prioridad, int tiempoLlegada, int tiempoRafaga)
            +transicionarEstado(Estado nuevo) void
            +getPrioridadEfectiva() int
            +compareTo(PCB o) int
            +toString() String
            +getPid() int
            +getNombre() String
            +getEstado() Estado
            +getPrioridad() int
            +getTiempoLlegada() int
            +getTiempoRafaga() int
            +getTiempoRestante() int
            +getTiempoEspera() int
            +getTiempoRetorno() int
            +getTiempoInicio() int
            +getTiempoFin() int
            +getContadorAging() int
            +getContextoCPU() Map~String, Object~
            +setTiempoRestante(int v) void
            +incrementarTiempoEspera() void
            +setTiempoRetorno(int v) void
            +setTiempoInicio(int v) void
            +setTiempoFin(int v) void
            +incrementarContadorAging() void
        }

        class ResultadoSimulacion {
            -String nombreAlgoritmo
            -double esperaPromedio
            -double retornoPromedio
            -double usoCPU
            -int cambiosContexto
            +ResultadoSimulacion(String nombreAlgoritmo, double esperaPromedio, double retornoPromedio, double usoCPU, int cambiosContexto)
            +imprimir() void
            +getNombreAlgoritmo() String
            +getCambiosContexto() int
            +getEsperaPromedio() double
            +getRetornoPromedio() double
            +getUsoCPU() double
        }
    }

    namespace algoritmos {
        class Calendarizador {
            <<interface>>
            +getNombre() String
            +seleccionarProceso(List~PCB~ lista, int t) PCB
            +esApropiativo() boolean
            +debeExpulsar(PCB enEjecucion, List~PCB~ lista, int t) boolean
        }

        class CalendarizadorBase {
            <<abstract>>
            -String nombre
            -boolean apropiativo
            +CalendarizadorBase(String nombre, boolean apropiativo)
            +getNombre() String
            +esApropiativo() boolean
            +debeExpulsar(PCB enEjecucion, List~PCB~ colaListos, int tiempoActual) boolean
        }

        class CalendarizadorFCFS {
            +seleccionarProceso(List~PCB~ lista, int t) PCB
        }

        class CalendarizadorSJF {
            +seleccionarProceso(List~PCB~ lista, int t) PCB
        }

        class CalendarizadorSRTF {
            +seleccionarProceso(List~PCB~ lista, int t) PCB
            +debeExpulsar(PCB enEjecucion, List~PCB~ colaListos, int tiempoActual) boolean
        }

        class CalendarizadorRR {
            -int quantum
            -int contador
            -PCB ultimoProceso
            +CalendarizadorRR(int quantum)
            +seleccionarProceso(List~PCB~ lista, int t) PCB
            +debeExpulsar(PCB actual, List~PCB~ colaListos, int tiempoActual) boolean
        }

        class CalendarizadorPrioridades {
            -int agingIntervalo
            +CalendarizadorPrioridades(int agingIntervalo)
            +seleccionarProceso(List~PCB~ lista, int t) PCB
            -prioridadEfectiva(PCB p) int
            +debeExpulsar(PCB actual, List~PCB~ colaListos, int tiempoActual) boolean
        }
    }

    namespace simulacion {
        class Simulador {
            -Calendarizador calendarizador
            -GestorColas gestor
            -GanttRenderer gantt
            -int cambiosContexto
            -int ticksCPUOcupada
            -int tiempoActual
            -PCB ultimoEjecutado
            +Simulador(Calendarizador calendarizador, List~PCB~ procesos)
            +ejecutar() ResultadoSimulacion
            -admitirYDesbloquear() void
            -evaluarExpulsion() void
            -asignarCPUSiLibre() void
            -ejecutarTick() void
            -construirResultado() ResultadoSimulacion
        }

        class GestorColas {
            -Queue~PCB~ colaNuevos
            -List~PCB~ colaListos
            -PCB enEjecucion
            -List~PCB~ colaBloqueados
            -List~PCB~ colaTerminados
            -Map~Integer, Integer~ tiempoIORestante
            +GestorColas()
            +cargarProceso(PCB proceso) void
            +admitirProcesos(int tiempoActual) void
            +asignarCPU(PCB proceso, int tiempoActual) void
            +expulsarAListos() void
            +terminarProceso(int tiempoActual) void
            +liberarCPU() void
            +bloquearProceso(int tiempoIO) void
            +actualizarBloqueados() void
            +actualizarEspera() void
            +hayProcesosActivos() boolean
            +getColaNuevos() Queue~PCB~
            +getColaListos() List~PCB~
            +getEnEjecucion() PCB
            +getColaBloqueados() List~PCB~
            +getColaTerminados() List~PCB~
        }

        class GanttRenderer {
            -List~String~ timeline
            +registrarTick(String nombreProceso) void
            +imprimir() void
        }

    }
    class Main {
        +main(String[] args) void
        -simular(String ruta, Calendarizador algoritmo) ResultadoSimulacion
        -cargarProcesos(String ruta) List~PCB~
    }

    %% Relaciones modelo
    PCB --> Estado : usa
    ResultadoSimulacion ..> PCB : referencia

    %% Jerarquía algoritmos
    CalendarizadorBase ..|> Calendarizador : implements
    CalendarizadorFCFS --|> CalendarizadorBase
    CalendarizadorSJF --|> CalendarizadorBase
    CalendarizadorSRTF --|> CalendarizadorBase
    CalendarizadorRR --|> CalendarizadorBase
    CalendarizadorPrioridades --|> CalendarizadorBase

    %% Relaciones simulacion
    Simulador o-- GestorColas : agrega
    Simulador o-- GanttRenderer : agrega
    Simulador ..> Calendarizador : usa (polimorfismo)
    Simulador ..> ResultadoSimulacion : produce
    GestorColas ..> PCB : gestiona
    Main ..> Simulador : crea y ejecuta
```
