import Modelo.Habitaciones.EstadoHabitacion;
import Modelo.Habitaciones.Habitacion;

import java.util.Scanner;

public class GestionHabitacion {

    public static <T extends Habitacion> void mostrarMenu(T habitacion) {
        System.out.println(habitacion);

        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("\n--- Habitacion " + habitacion.getNroHabitacion() + " ---");
            System.out.println("1. Ver estado de la habitación");
            System.out.println("2. Cambiar estado de la habitación");
            System.out.println("3. Ver DNI de ocupantes de la habitación");
            System.out.println("4. Ver número de ocupantes actuales");
            System.out.println("5. Ver resumen de la habitacion");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");
            opcion = Integer.parseInt(scanner.nextLine());

            switch (opcion) {
                case 1:
                    verEstado(habitacion);
                    break;
                case 2:
                    cambiarEstado(scanner, habitacion);
                    break;
                case 3:
                    verOcupantes(habitacion);
                    break;
                case 4:
                    verNumeroOcupantes(habitacion);
                    break;
                case 5:
                    System.out.println(habitacion);
                    break;
                case 0:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
            }
        } while (opcion != 0);
    }

    private static <T extends Habitacion> void verEstado(T habitacion) {
        System.out.println("Estado actual de la habitación: " + habitacion.getEstado());
    }

    private static <T extends Habitacion> void cambiarEstado(Scanner scanner, T habitacion) {
        System.out.println("Estados disponibles:");
        System.out.println(EstadoHabitacion.retornarValoresDeEnum());
        System.out.print("Seleccione el nuevo estado: ");
        int estadoSeleccionado = Integer.parseInt(scanner.nextLine());
        if (estadoSeleccionado >= 0 && estadoSeleccionado < EstadoHabitacion.values().length) {
            habitacion.setEstado(EstadoHabitacion.values()[estadoSeleccionado]);
            System.out.println("Estado cambiado a: " + habitacion.getEstado());
        } else {
            System.out.println("Estado no válido.");
        }
    }

    private static <T extends Habitacion> void verOcupantes(T habitacion) {
        System.out.println("Ocupantes actuales: " + habitacion.getOcupantes());
    }

    private static <T extends Habitacion> void verNumeroOcupantes(T habitacion) {
        System.out.println("Ocupantes actuales: " + habitacion.getNroOcupantes());
    }


}