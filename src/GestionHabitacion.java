import Exceptions.BadDataException;
import Exceptions.PersonaNoExisteException;
import Modelo.Habitaciones.*;

import java.util.Scanner;

import static DataChecks.VerificacionesDeDatos.esSoloNumeros;

/**
 * Clase para gestionar habitaciones.
 *
 *
 */

public class GestionHabitacion {

    /**
     * Metodo generico para gestionar una habitacion, dependiendo del tipo.
     *
     * @param habitacion la habitacion a gestionar
     *
     */
    public static <T extends Habitacion> void mostrarMenu(T habitacion, Hotel hotel) throws BadDataException {

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
                    verOcupantes(habitacion,hotel);
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
     * Metodo que muestra unas serie de opciones comunes a todos los tipos de habitacion
     */

    public static void mostrarOpcionesComunes() {
        System.out.println("1. Ver estado de la habitación");
        System.out.println("2. Cambiar estado de la habitación");
        System.out.println("3. Ver info de ocupantes de la habitación");
        System.out.println("4. Ver número de ocupantes actuales");
        System.out.println("5. Ver resumen de la habitacion");
    }


    /**
     * Metodo generico para ver el estado de una habitacion
     *
     * @param habitacion habitacion de la cual queremos saber el estado
     */
    private static <T extends Habitacion> void verEstado(T habitacion) {
        System.out.println("Estado actual de la habitación: " + habitacion.getEstado());
    }

    /**
     * Metodo generico que permite cambiar el estado de una habitacion.
     *
     * @param scanner lee los datos ingresados por teclado
     * @param habitacion la habitacion a cambiarle el estado
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
     * Muestra la información de los ocupantes, a partir de sus DNIs
     * @param habitacion Habitacion actual
     * @param hotel Una instancia de Hotel, para extraer la información de los ocupantes
     * @param <T> Tipo que extiende de {@link Habitacion}
     */
    private static <T extends Habitacion> void verOcupantes(T habitacion,Hotel hotel) throws BadDataException {
        if (habitacion.getEstado() == EstadoHabitacion.OCUPADA) {
            System.out.println(hotel.obtenerInfoPasajeros(habitacion.getOcupantes()));
        } else {
            System.out.println("La habitación no tiene ocupantes");
        }
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
     * @param habitacion Habitacion actual, solo puede ser presidencial
     */
    private static void revisarJacuzzi(HabitacionPresidencial habitacion) {
        habitacion.marcarMantenimientoEnJacuzzi();
        System.out.println("Revision de jacuzzi completo!");
    }


}