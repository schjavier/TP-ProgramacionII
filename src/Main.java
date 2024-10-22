import DataChecks.VerificacionesDeDatos;
import Exceptions.*;
import Modelo.Habitaciones.EstadoHabitacion;
import Modelo.Habitaciones.Habitacion;
import Modelo.Habitaciones.HabitacionPresidencial;
import Modelo.Habitaciones.TipoHabitacion;
import Modelo.Persona.Empleado;
import Modelo.Persona.TipoEmpleado;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static Scanner teclado = new Scanner(System.in);
    public static boolean logueado = false;

    public static void main(String[] args) {

        Hotel hotel = new Hotel("Hotel California");
        menuLogin(hotel);

        if (logueado) {
            menuHabitaciones(hotel);
        }
    }

    static public void menuLogin(Hotel hotel) {
        String username;
        String clave;

        if (hotel.obtenerEmpleados().isEmpty()) {
            System.out.println("El hotel no tiene ningun usuario. Debe crearse un usuario para continuar");
            crearUsuario(hotel,true);
        }

        System.out.println("Usuario: ");
        username = teclado.nextLine();
        System.out.println("Clave: ");
        clave = teclado.nextLine();

        hotel.intentarIniciarSesion(username,clave);
    }

    static public void crearUsuario(Hotel hotel, boolean esPrimerUsuario) {
        String nombre;
        String apellido;
        int dni;
        String usuario;
        String email;
        String clave;
        int tipoEmpleado;

        nombre = definirNombreoApe("Nombre: ");
        apellido = definirNombreoApe("Apellido: ");
        dni = definirDni(hotel);
        usuario = definirUsuario(hotel);

        System.out.println("Email: ");
        email = teclado.nextLine();

        System.out.println("Clave: ");
        clave = teclado.nextLine();

        if (!esPrimerUsuario) {
            System.out.println("Tipo de empleado: ");
            System.out.println(TipoEmpleado.retornarValoresDeEnum());
            tipoEmpleado = Integer.parseInt(teclado.nextLine());
            hotel.crearUsuario(nombre,apellido,dni,usuario,email,clave,tipoEmpleado);  // despues lo cambio
        } else {
            hotel.crearUsuario(nombre,apellido,dni,usuario,email,clave,1);
        }
    }

    static public void menuHabitaciones(Hotel hotel) {
        int opcion;

        do {
            System.out.println("\n--- Gestion del hotel ---");
            System.out.println("1. Crear habitacion(es)");
            System.out.println("2. Gestionar una habitacion por su ID");
            System.out.println("3. Eliminar una habitacion por su ID");
            System.out.println("4. Listar todas las habitaciones");
            System.out.println("5. Listar habitaciones de acuerdo a su tipo y estado");
            System.out.println("6. Obtener conteo total de habitaciones");
            System.out.println("7. Obtener un conteo de todas las habitaciones segun el estado");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");

            opcion = Integer.parseInt(teclado.nextLine());

            switch (opcion) {
                case 1:
                    crearHabitaciones(hotel);
                    break;
                case 2:
                    gestionarHabitacion(hotel);
                    break;
                case 3:
                    eliminarHabitacion(hotel);
                    break;
                case 4:
                    System.out.println(hotel.listarHabitaciones());
                    break;
                case 5:
                    listarHabitacionesEstado(hotel);
                    break;
                case 6:
                    System.out.println("Habitaciones totales: " + hotel.obtenerNroHabitaciones());
                    break;
                case 7:
                    contarHabitacionesEstado(hotel);
                    break;
                case 0:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
            }
        } while (opcion != 0);
    }

    static public void agregarPasajero(Hotel hotel) {
        String nombre;
        String apellido;
        int dni;
        String direccion;

        nombre = definirNombreoApe("Nombre");
        apellido = definirNombreoApe("Apellido");
        dni = definirDni(hotel);
        teclado.nextLine();
        System.out.println("Direccion");
        direccion = teclado.nextLine();

        hotel.agregarPasajero(nombre, apellido, dni, direccion);
    }

    static public String definirNombreoApe(String mensaje) {
        String dato = "";
        boolean nombreVerificado = false;
        while (!nombreVerificado) {
            try {
                System.out.println(mensaje);
                dato = teclado.nextLine();
                VerificacionesDeDatos.tieneNumeros(dato);
                nombreVerificado = true;
            } catch (BadDataException e) {
                System.out.println(e.getMessage());
            } catch (InputMismatchException e) {
                System.out.println("algo paso");
            }
        }
        return dato;
    }

    static public int definirDni(Hotel hotel) {
        int dni = 0;
        boolean dniVerificado = false;

        while (!dniVerificado) {
            try {
                System.out.println("Dni");
                dni = Integer.parseInt(teclado.nextLine());
                hotel.verSiElDniEstaCargado(dni);
                dniVerificado = true;
            } catch (BadDataException | PersonaExisteException e) {
                System.out.println(e.getMessage());
            } catch (InputMismatchException e) {
                System.out.println("No puede tener letras!");
            }
        }
        return dni;
    }

    public static void crearHabitaciones(Hotel hotel) {
        int cantHab = 0;
        int capMaxHab = 0;
        int tipoHab = 0;

        try {
            System.out.println("Ingrese la cantidad de habitaciones: ");
            cantHab = Integer.parseInt(teclado.nextLine());
            System.out.println("Ingrese su capacidad maxima: ");

            capMaxHab = Integer.parseInt(teclado.nextLine());

            boolean tipoVerificado = false;
            while (!tipoVerificado) {
                System.out.println("Ingrese el tipo de habitacion: ");
                System.out.println(TipoHabitacion.retornarValoresDeEnum());
                try {
                    tipoHab = Integer.parseInt(teclado.nextLine());
                    tipoVerificado = TipoHabitacion.verificarEntrada(tipoHab);
                } catch (BadOptionException e) {
                    System.out.println(e.getMessage());
                } catch (InputMismatchException e) {
                    System.out.println("No es correcto!!");
                }
            }
            hotel.crearHabitaciones(cantHab, capMaxHab, tipoHab);
        } catch (BadOptionException e) {
            System.out.println("No existe el tipo de habitacion!");
        }

    }

    public static void eliminarHabitacion(Hotel hotel) {
        int nroHab = 0;

        System.out.println("Ingrese el numero de habitacion a eliminar: ");
        nroHab = Integer.parseInt(teclado.nextLine());
        hotel.eliminarHabitacion(nroHab);
    }

    public static void listarHabitacionesEstado(Hotel hotel) {
        int tipoHab = 0;
        String estadoSeleccionado;

        try {
            System.out.println("Ingrese el tipo de habitacion: ");
            tipoHab = Integer.parseInt(teclado.nextLine());
            System.out.println("Ingrese el estado: ");
            estadoSeleccionado = teclado.nextLine();
            System.out.println(hotel.listarSegunEstado(tipoHab, EstadoHabitacion.valueOf(estadoSeleccionado.toUpperCase())));
        } catch (BadOptionException e) {
            System.out.println("No existe el tipo de habitacion!");
        } catch (IllegalArgumentException e) {
            System.out.println("No existe el estado ingresado");
        }
    }

    public static void contarHabitacionesEstado(Hotel hotel) {
        String estadoSeleccionado;
        int totalHabitaciones = 0;

        try {
            System.out.println("Ingrese el estado: ");
            estadoSeleccionado = teclado.nextLine();
            totalHabitaciones = hotel.obtenerNroHabitacionesSegunEstado(EstadoHabitacion.valueOf(estadoSeleccionado.toUpperCase()));
            System.out.println("Total de habitaciones: " + totalHabitaciones);
        } catch (IllegalArgumentException e) {
            System.out.println("No existe el estado ingresado");
        }
    }

    /**
     * Muestra un menu para gestionar una habitacion segun el numero ingresado por teclado
     */

    public static void gestionarHabitacion(Hotel hotel) {
        Habitacion prueba = null;

        System.out.println("Ingrese el numero de la habitacion a gestionar: ");

        int numeroHabitacion = Integer.parseInt(teclado.nextLine());
        for (int i = 1; i <= 3; i++) { // 3 es el nro de tipos de habitacion que existen
            prueba = hotel.selectorDeTipoHabitacion(i).traerHabitacionSegunId(numeroHabitacion);
            if (prueba != null) {
                break;
            }
        }

        if (prueba == null) {
            throw new HabitacionNoEncontradaException("No existe la habitacion ingresada");
        } else {
            GestionHabitacion.mostrarMenu(prueba);
        }
    }

    public static String definirUsuario(Hotel hotel) {
        String username;
        boolean usuarioNoExiste = true;

        System.out.println("Ingrese un nombre de usuario");
        username = teclado.nextLine();
        if (hotel.existeUsuario(username)) {
            usuarioNoExiste = false;
            throw new BadDataException("El usuario ingresado ya existe");
        }

        return username;
    }

}