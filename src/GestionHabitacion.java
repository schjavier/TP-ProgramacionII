import Exceptions.BadDataException;
import Modelo.Habitaciones.*;

import java.util.Scanner;

import static DataChecks.VerificacionesDeDatos.esSoloNumeros;

/**
 * <p>Clase para gestionar todo sobre habitaciones
 */
public class GestionHabitacion {

    /**
     * Recibe una habitacion sobre que se va a trabajar en el menu.
     * @param habitacion Habitacion que llega
     * @param <T> Tipo que extiende de {@link Habitacion}
     */
    public static <T extends Habitacion> void mostrarMenu(T habitacion) {

        Scanner scanner = new Scanner(System.in);
        int opcion = 0;

        System.out.println(habitacion);
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
                    try {
                        cambiarEstado(scanner, habitacion);
                    } catch (BadDataException e) {
                        System.out.println(e.getMessage());
                    }
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

    /**
     * Muestra las opciones que tiene en comun todas las habitaciones
     */
    public static void mostrarOpcionesComunes() {
        System.out.println("1. Ver estado de la habitación");
        System.out.println("2. Cambiar estado de la habitación");
        System.out.println("3. Ver DNI de ocupantes de la habitación");
        System.out.println("4. Ver número de ocupantes actuales");
        System.out.println("5. Ver resumen de la habitacion");
    }

    /**
     * Se muestra el estado de la habitacion actual.
     * @param habitacion Habitacion actual
     * @param <T> Tipo que extiende de {@link Habitacion}
     */
    private static <T extends Habitacion> void verEstado(T habitacion) {
        System.out.println("Estado actual de la habitación: " + habitacion.getEstado());
    }

    /**
     * Cambia el estado de la habitación. No se permite hacerlo si tiene personas dentro.
     * @param scanner Teclado
     * @param habitacion Habitacion actual
     * @param <T> Tipo que extiende de {@link Habitacion}
     * @throws BadDataException En caso de que la habitacion tenga personas adentro, o se quiera cambiar a ocupada
     */
    private static <T extends Habitacion> void cambiarEstado(Scanner scanner, T habitacion) throws BadDataException {
        String err = "";
        System.out.println("Estados disponibles:");
        System.out.println(EstadoHabitacion.retornarValoresDeEnum());
        System.out.print("Seleccione el nuevo estado: ");

        int estadoSeleccionado = 0;
        String numeroIngresado = scanner.nextLine();
        esSoloNumeros(numeroIngresado);
        estadoSeleccionado = Integer.parseInt(numeroIngresado);

        if (estadoSeleccionado >= 0 && estadoSeleccionado < EstadoHabitacion.values().length) {
            EstadoHabitacion estadoCheck = EstadoHabitacion.values()[estadoSeleccionado];

            if(estadoCheck == EstadoHabitacion.OCUPADA)
            {
                err = err.concat("No puede ser el estado ocupado!");
            }

            if(habitacion.getNroOcupantes() != 0)
            {
                err = err.concat("No puede cambiarse el estado si la habitación se encuentra ocupada actualmente!");
            }

            if(!err.isBlank())
            {
                throw new BadDataException(err);
            }


            System.out.println("Estado cambiado a: " + habitacion.getEstado());
        } else {
            err = err.concat("Estado no valido");
            throw new BadDataException(err);
        }
    }

    /**
     * Muestra la lista de dnis de los ocupantes
     * @param habitacion Habitacion actual
     * @param <T> Tipo que extiende de {@link Habitacion}
     */
    private static <T extends Habitacion> void verOcupantes(T habitacion) {
        System.out.println("Ocupantes actuales: " + habitacion.getOcupantes());
    }

    /**
     * Muestra un numero de la cantidad de personas que hospedan la habitacion
     * @param habitacion Habitacion actual
     * @param <T> Tipo que extiende de {@link Habitacion}
     */
    private static <T extends Habitacion> void verNumeroOcupantes(T habitacion) {
        System.out.println("Ocupantes actuales: " + habitacion.getNroOcupantes());
    }

    /**
     * Cambia la fecha de cocinaRevisada al dia de hoy.
     * @param habitacion Habitacion actual
     * @param <T> Tipo que extiende de {@link Habitacion} y tiene la interfaz {@link TieneCocina}
     */
    private static <T extends Habitacion & TieneCocina> void revisarCocina(T habitacion) {
        habitacion.marcarMantenimientoHechoEnCocina();
        System.out.println("Revision de cocina completa!");
    }

    /**
     * Cambia la fecha de jacuzziRevisado al dia de hoy.
     * @param habitacion Habitacion actual, solo puede ser precidencial
     */
    private static void revisarJacuzzi(HabitacionPresidencial habitacion) {
        habitacion.marcarMantenimientoEnJacuzzi();
        System.out.println("Revision de jacuzzi completo!");
    }


}