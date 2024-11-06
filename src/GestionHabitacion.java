import Exceptions.BadDataException;
import Modelo.Habitaciones.*;

import java.util.Scanner;

import static DataChecks.VerificacionesDeDatos.esSoloNumeros;

public class GestionHabitacion {

    public static <T extends Habitacion> void mostrarMenu(T habitacion) {
        System.out.println(habitacion);

        Scanner scanner = new Scanner(System.in);
        int opcion = 0;

        do {
            System.out.println("\n--- Habitacion " + habitacion.getNroHabitacion() + " ---");
            mostrarOpcionesComunes();
            if (habitacion instanceof HabitacionSuite || habitacion instanceof HabitacionPresidencial) {
                System.out.println("6. Revisar cocina");
            }
            if (habitacion instanceof HabitacionPresidencial) {
                System.out.println("7. Revisar jacuzzi");
            }
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");

            try {
                String numeroIngresado = scanner.nextLine();
                esSoloNumeros(numeroIngresado);
                opcion = Integer.parseInt(numeroIngresado);
            } catch (BadDataException e) {
                System.out.println("Solo se aceptan números!");
                opcion = 0;
            }

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
                case 6:
                    if (habitacion instanceof HabitacionSuite) {
                        revisarCocina((HabitacionSuite)habitacion);
                    }
                    if (habitacion instanceof HabitacionPresidencial) {
                        revisarCocina((HabitacionPresidencial) habitacion);
                    }
                    break;
                case 7:
                    if (habitacion instanceof HabitacionPresidencial) {
                        revisarJacuzzi((HabitacionPresidencial) habitacion);
                    }
                    break;
                case 0:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
            }
        } while (opcion != 0);
    }

    public static void mostrarOpcionesComunes() {
        System.out.println("1. Ver estado de la habitación");
        System.out.println("2. Cambiar estado de la habitación");
        System.out.println("3. Ver DNI de ocupantes de la habitación");
        System.out.println("4. Ver número de ocupantes actuales");
        System.out.println("5. Ver resumen de la habitacion");
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

    private static <T extends Habitacion & TieneCocina> void revisarCocina(T habitacion) {
        habitacion.marcarMantenimientoHechoEnCocina();
        System.out.println("Revision de cocina completa!");
    }

    private static void revisarJacuzzi(HabitacionPresidencial habitacion) {
        habitacion.marcarMantenimientoEnJacuzzi();
        System.out.println("Revision de jacuzzi completo!");
    }


}