import Exceptions.BadDataException;
import Exceptions.BadOptionException;
import Exceptions.PersonaExisteException;
import Modelo.Persona.Empleado;
import Modelo.Persona.Pasajero;
import Modelo.Persona.Persona;
import java.util.Scanner;
import static DataChecks.VerificacionesDeDatos.esSoloNumeros;

/**
 * Menu para gestionar personas.
 */
public class GestionPersona {
    static Scanner teclado = new Scanner(System.in);

    /**
     * El menu para manejar las personas.
     * @param persona Persona que llega
     * @param hotel El sistema.
     * @param <T> Lo que extiende de personas
     */
    public static <T extends Persona> void mostrarMenu(T persona, Hotel hotel) {

        int opcion = 0;

        do {
            mostrarOpcionesComunes();
            if (persona instanceof Empleado) {
                mostrarOpcionesEmpleado();
            } else if (persona instanceof Pasajero) {
                mostrarOpcionesPasajero();
            }

            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");

            try {
                String numeroIngresado = teclado.nextLine();
                esSoloNumeros(numeroIngresado);
                opcion = Integer.parseInt(numeroIngresado);
            } catch (BadDataException e) {
                System.out.println("Solo se aceptan números!");
                opcion = 0;
            }

            switch (opcion) {
                case 0:
                    System.out.println("Saliendo...");
                    break;
                case 1:
                    System.out.println(persona.toString());
                    break;
                case 2:
                    cambiarNombre(persona);
                    break;
                case 3:
                    cambiarApellido(persona);
                    break;
                case 4:
                    cambiarDni(persona,hotel);
                    break;
                case 5:
                    if (persona instanceof Pasajero) {
                        cambiarDomicilio(persona);
                    } else {
                        cambiarUsuario(persona,hotel);
                    }
                    break;
                case 6:
                    cambiarEmail(persona);
                    break;
                case 7:
                    cambiarClave(persona);
                    break;
                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
            }
        } while (opcion != 0);
    }

    /**
     * Muestra las opciones comunes
     */
    public static void mostrarOpcionesComunes() {
        System.out.println("\n--- Gestion de la persona ---");
        System.out.println("1. Ver informacion");
        System.out.println("2. Cambiar nombre");
        System.out.println("3. Cambiar apellido");
        System.out.println("4. Cambiar dni");
    }

    /**
     * Muestra las opciones que el empleado puede tener
     */
    public static void mostrarOpcionesEmpleado() {
        System.out.println("5. Cambiar usuario");
        System.out.println("6. Cambiar email");
        System.out.println("7. Cambiar clave");

    }
    /**
     * Muestra las opciones que el pasajero puede tener
     */
    public static void mostrarOpcionesPasajero() {
        System.out.println("5. Cambiar domicilio");
    }

    /**
     * Cambia el nombre de la persona
     * @param persona Persona que se esta gestionando
     */
    public static void cambiarNombre(Persona persona) {
        System.out.println("Nombre actual: " + persona.getNombre() + "\n");
        persona.setNombre(Main.definirNombreoApe("Nuevo nombre: "));
    }

    /**
     * Cambia el apellido de la persona
     * @param persona Persona que se esta gestionando
     */
    public static void cambiarApellido(Persona persona) {
        System.out.println("Apellido actual: " + persona.getApellido() + "\n");
        persona.setApellido(Main.definirNombreoApe("Nuevo apellido: "));
    }

    /**
     * Cambia el dni de la persona
     * @param persona La persona que se esta gestionando.
     * @param hotel El sistema.
     */
    public static void cambiarDni(Persona persona, Hotel hotel) {
        System.out.println("DNI actual: " + persona.getDni());
        persona.setDni(Main.definirDni(hotel));
    }

    /**
     * Cambia el domicilio del pasajero
     * @param persona Persona que se esta gestionando
     */
    public static void cambiarDomicilio(Persona persona) {
        Pasajero pasajero = (Pasajero) persona;
        System.out.println("Domicilio actual: " + pasajero.getDomicilio() + "\n");
        System.out.println("Nuevo domicilio: ");
        pasajero.setDomicilio(teclado.nextLine());
    }

    /**
     * Cambiar el usuario del empleado
     * @param persona Persona que se esta gestionando
     * @param hotel Sistema.
     */
    public static void cambiarUsuario(Persona persona, Hotel hotel) {
        try {
            Empleado empleado = (Empleado) persona;
            System.out.println("Usuario actual: " + empleado.getUsuario() + "\n");
            System.out.println("Nuevo usuario: ");
            String nuevoUsuario = teclado.nextLine();
            if (!hotel.existeUsuario(nuevoUsuario)) {
                empleado.setUsuario(nuevoUsuario);
            } else {
                throw new PersonaExisteException("Ya existe una persona con ese usuario");
            }
        } catch (PersonaExisteException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Cambia el email del empleado
     * @param persona Persona que se esta gestionando
     */
    public static void cambiarEmail(Persona persona) {
        try {
            if (!(persona instanceof Empleado)) {
                throw new BadOptionException("Opcion no aplicable a pasajeros");
            }

            Empleado empleado = (Empleado) persona;
            System.out.println("Email actual: " + empleado.getEmail() + "\n");
            System.out.println("Nuevo email: ");
            empleado.setEmail(teclado.nextLine());
        } catch (BadOptionException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Cambia la clave del usuario
     * @param persona Persona que se esta gestionando
     */
    public static void cambiarClave(Persona persona) {
        try {
            if (!(persona instanceof Empleado)) {
                throw new BadOptionException("Opcion no aplicable a pasajeros");
            }

            Empleado empleado = (Empleado) persona;
            System.out.println("Nueva clave: ");
            empleado.setClave(teclado.nextLine());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
