import Modelo.Persona.Empleado;
import Modelo.Persona.Pasajero;
import Modelo.Persona.Persona;

import java.util.Scanner;

public class GestionPersona {

    public static <T extends Persona> void mostrarMenu(T persona) {
        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            mostrarOpcionesComunes();
            if (persona instanceof Empleado) {
                mostrarOpcionesEmpleado();
            } else if (persona instanceof Pasajero) {
                mostrarOpcionesPasajero();
            }
            System.out.print("Seleccione una opción: ");
            opcion = Integer.parseInt(scanner.nextLine());

            switch (opcion) {
                case 0:
                    System.out.println("Saliendo...");
                    break;
                case 1:
                    System.out.println(persona.toString());
                    break;
                case 2:
                    //cambiarNombre(persona);
                case 3:
                    //cambiarApellido(persona);
                case 4:
                    //cambiarDni(persona);
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
    }

    public static void mostrarOpcionesPasajero() {
        System.out.println("5. Cambiar domicilio");
    }

}
