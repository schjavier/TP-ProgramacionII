import Exceptions.BadDataException;
import Exceptions.BadOptionException;
import Exceptions.PersonaExisteException;
import Modelo.Persona.Empleado;
import Modelo.Persona.Pasajero;
import Modelo.Persona.Persona;

import java.util.Scanner;

import static DataChecks.VerificacionesDeDatos.esSoloNumeros;


public class GestionPersona {
    static Scanner teclado = new Scanner(System.in);

    public static <T extends Persona> void mostrarMenu(T persona, Hotel hotel) {

        int opcion = 0;

        do {
            mostrarOpcionesComunes();
            if (persona instanceof Empleado) {
                mostrarOpcionesEmpleado();
            } else if (persona instanceof Pasajero) {
                mostrarOpcionesPasajero();
            }

            System.out.print("Seleccione una opción: ");

            try {
                String numeroIngresado = teclado.nextLine();
                esSoloNumeros(numeroIngresado);
                opcion = Integer.parseInt(numeroIngresado);
            } catch (BadDataException e) {
                System.out.println("Solo se aceptan números!");
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

    public static void mostrarOpcionesComunes() {
        System.out.println("\n--- Gestion de la persona ---");
        System.out.println("1. Ver informacion");
        System.out.println("2. Cambiar nombre");
        System.out.println("3. Cambiar apellido");
        System.out.println("4. Cambiar dni");
    }

    public static void mostrarOpcionesEmpleado() {
        System.out.println("5. Cambiar usuario");
        System.out.println("6. Cambiar email");
        System.out.println("7. Cambiar clave");
        System.out.println("0. Salir");
    }

    public static void mostrarOpcionesPasajero() {
        System.out.println("5. Cambiar domicilio");
        System.out.println("0. Salir");
    }

    public static void cambiarNombre(Persona persona) {
        System.out.println("Nombre actual: " + persona.getNombre() + "\n");
        persona.setNombre(Main.definirNombreoApe("Nuevo nombre: "));
    }

    public static void cambiarApellido(Persona persona) {
        System.out.println("Apellido actual: " + persona.getApellido() + "\n");
        persona.setApellido(Main.definirNombreoApe("Nuevo apellido: "));
    }

    public static void cambiarDni(Persona persona, Hotel hotel) {
        System.out.println("DNI actual: " + persona.getDni());
        persona.setDni(Main.definirDni(hotel));
    }

    public static void cambiarDomicilio(Persona persona) {
        Pasajero pasajero = (Pasajero) persona;
        System.out.println("Domicilio actual: " + pasajero.getDomicilio() + "\n");
        System.out.println("Nuevo domicilio: ");
        pasajero.setDomicilio(teclado.nextLine());
    }

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
