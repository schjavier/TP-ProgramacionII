import Modelo.Habitaciones.*;

import java.util.Scanner;

public class GestionHabitaciones {

    // Esto va a gestionar TODAS las habitaciones del hotel, no solo un tipo
    // Tengo que cambiarlo pero lo commiteo para que quede guardado

    public static <T extends Habitacion> void mostrarMenu(Habitaciones<T> habitaciones) {
        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("\n--- Gestión de Habitaciones de tipo: " + habitaciones.getTipoHabitacion() + " ---");
            System.out.println("1. Agregar habitación");
            System.out.println("2. Eliminar habitación por número");
            System.out.println("3. Ver lista de todas las habitaciones");
            System.out.println("4. Ver lista de habitaciones según estado");
            System.out.println("5. Asignar estado a todas las habitaciones");
            System.out.println("6. Contar habitaciones según estado");
            System.out.println("7. Gestionar habitación por número");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");
            opcion = scanner.nextInt();

            switch (opcion) {
                case 1:
                    agregarHabitacion(scanner, habitaciones);
                    break;
                case 2:
                    eliminarHabitacion(scanner, habitaciones);
                    break;
                case 3:
                    listarHabitaciones(habitaciones);
                    break;
                case 4:
                    listarHabitacionesSegunEstado(scanner, habitaciones);
                    break;
                case 5:
                    asignarEstadoATodas(scanner, habitaciones);
                    break;
                case 6:
                    contarHabitacionesSegunEstado(scanner, habitaciones);
                    break;
                case 7:
                    gestionarHabitacionPorId(scanner, habitaciones);
                    break;
                case 0:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
            }
        } while (opcion != 0);

        scanner.nextLine();
    }

    private static <T extends Habitacion> void agregarHabitacion(Scanner scanner, Habitaciones<T> habitaciones) {
        System.out.print("Ingrese la capacidad máxima de la nueva habitación: ");
        int capacidadMaxima = scanner.nextInt();

        T nuevaHabitacion;
        if (habitaciones.getTipoHabitacion().equals("Standard")) { // No me gusta pero lo voy a cambiar despues
            nuevaHabitacion = (T) new HabitacionStandard(capacidadMaxima);
        } else if (habitaciones.getTipoHabitacion().equals("Suite")) {
            nuevaHabitacion = (T) new HabitacionSuite(capacidadMaxima);
        } else {
            throw new IllegalArgumentException("Tipo de habitaciones no soportado");
        }

        habitaciones.agregarHabitacion(nuevaHabitacion);
        System.out.println("Habitación agregada.");
    }

    private static <T extends Habitacion> void eliminarHabitacion(Scanner scanner, Habitaciones<T> habitaciones) {
        System.out.print("Ingrese el número de la habitación a eliminar: ");
        int nroHabitacion = scanner.nextInt();
        if (habitaciones.eliminarHabitacionSegunNumero(nroHabitacion)) {
            System.out.println("Habitación eliminada.");
        } else {
            System.out.println("No se encontró la habitación.");
        }
    }

    private static <T extends Habitacion> void listarHabitaciones(Habitaciones<T> habitaciones) {
        System.out.println("Lista de todas las habitaciones:");
        System.out.println(habitaciones.listarHabitaciones());
    }

    private static <T extends Habitacion> void listarHabitacionesSegunEstado(Scanner scanner, Habitaciones<T> habitaciones) {
        System.out.println("Estados disponibles:");
        System.out.println(EstadoHabitacion.retornarValoresDeEnum());
        System.out.print("Seleccione el estado: ");
        int estadoSeleccionado = scanner.nextInt();
        if (estadoSeleccionado >= 0 && estadoSeleccionado < EstadoHabitacion.values().length) {
            EstadoHabitacion estado = EstadoHabitacion.values()[estadoSeleccionado];
            System.out.println("Lista de habitaciones en estado " + estado + ":");
            System.out.println(habitaciones.listarHabitacionesSegunEstado(estado));
        } else {
            System.out.println("Estado no válido.");
        }
    }

    private static <T extends Habitacion> void asignarEstadoATodas(Scanner scanner, Habitaciones<T> habitaciones) {
        System.out.println("Estados disponibles:");
        System.out.println(EstadoHabitacion.retornarValoresDeEnum());
        System.out.print("Seleccione el nuevo estado para todas las habitaciones: ");
        int estadoSeleccionado = scanner.nextInt();
        if (estadoSeleccionado >= 0 && estadoSeleccionado < EstadoHabitacion.values().length) {
            EstadoHabitacion estado = EstadoHabitacion.values()[estadoSeleccionado];
            habitaciones.asignarEstadoAtodas(estado);
            System.out.println("Estado asignado a todas las habitaciones.");
        } else {
            System.out.println("Estado no válido.");
        }
    }

    private static <T extends Habitacion> void contarHabitacionesSegunEstado(Scanner scanner, Habitaciones<T> habitaciones) {
        System.out.println("Estados disponibles:");
        System.out.println(EstadoHabitacion.retornarValoresDeEnum());
        System.out.print("Seleccione el estado: ");
        int estadoSeleccionado = scanner.nextInt();
        if (estadoSeleccionado >= 0 && estadoSeleccionado < EstadoHabitacion.values().length) {
            EstadoHabitacion estado = EstadoHabitacion.values()[estadoSeleccionado];
            System.out.println("Cantidad de habitaciones en estado " + estado + ": " + habitaciones.contarCantidadHabitacionesSegunEstado().get(estado));
        } else {
            System.out.println("Estado no válido.");
        }
    }

    private static <T extends Habitacion> void gestionarHabitacionPorId(Scanner scanner, Habitaciones<T> habitaciones) {
        System.out.print("Ingrese el número de la habitación a gestionar: ");
        int nroHabitacion = scanner.nextInt();
        T habitacion = habitaciones.traerHabitacionSegunId(nroHabitacion);
        if (habitacion != null) {
            GestionHabitacion.mostrarMenu(habitacion);
        } else {
            System.out.println("No se encontró la habitación.");
        }
    }
}