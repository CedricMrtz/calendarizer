# Process Scheduler Simulator (Java)

This project implements a process scheduling simulator in Java as part of an Operating Systems course.

## Objective
The goal is to model how an operating system manages processes using different CPU scheduling algorithms. The simulator will represent processes using a Process Control Block (PCB), handle process queues, and evaluate performance metrics.

## Tech Stack
- Java 11+ (recommended Java 17)
- Standard Java libraries only

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
            -Map contextoCPU
            +transicionarEstado(Estado e) void
            +getPrioridadEfectiva() int
            +compareTo(PCB o) int
            +toString() String
        }

        class ResultadoSimulacion {
            -double esperaPromedio
            -double retornoPromedio
            -double usoCPU
            -int cambiosContexto
            +getCambiosContexto() int
            +imprimir() void
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
            +getNombre() String
            +esApropiartivo() boolean
            +debeExpulsar() boolean
        }

        class CalendarizadorFCFS {
            +seleccionarProceso(List~PCB~ lista, int t) PCB
            +esApropiativo() boolean
            +debeExpulsar(PCB p, List~PCB~ lista, int t) boolean
        }

        class CalendarizadorSJF {
            +seleccionarProceso(List~PCB~ lista, int t) PCB
            +esApropiativo() boolean
            +debeExpulsar(PCB p, List~PCB~ lista, int t) boolean
        }

        class CalendarizadorSRTF {
            +seleccionarProceso(List~PCB~ lista, int t) PCB
            +esApropiativo() boolean
            +debeExpulsar(PCB p, List~PCB~ lista, int t) boolean
        }

        class CalendarizadorRR {
            -int quantum
            +CalendarizadorRR(int quantum)
            +seleccionarProceso(List~PCB~ lista, int t) PCB
            +esApropiativo() boolean
            +debeExpulsar(PCB p, List~PCB~ lista, int t) boolean
        }

        class CalendarizadorPrioridades {
            -int intervaloN
            +CalendarizadorPrioridades(int intervaloN)
            +seleccionarProceso(List~PCB~ lista, int t) PCB
            +esApropiativo() boolean
            +debeExpulsar(PCB p, List~PCB~ lista, int t) boolean
        }
    }

    namespace simulacion {
        class Simulador {
            -Calendarizador algoritmo
            -GestorColas gestorColas
            -GanttRenderer gantt
            -int tiempoActual
            -PCB procesoEnCPU
            +Simulador(Calendarizador alg, List~PCB~ procesos)
            +iniciar() void
            +ejecutarTick() void
            +getResultado() ResultadoSimulacion
        }

        class GestorColas {
            -Queue~PCB~ colaNuevos
            -List~PCB~ colaListos
            -List~PCB~ colaBloqueados
            -List~PCB~ terminados
            +admitirProcesos(int t) void
            +desbloquear(int t) void
            +getColaListos() List~PCB~
            +imprimirEstado() void
        }

        class GanttRenderer {
            -List registros
            -int maxTick
            +registrar(PCB p, int t) void
            +renderizar() void
        }

    }
    class Main {
        +main(String[] args) void
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
