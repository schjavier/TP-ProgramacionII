import DataChecks.VerificacionesDeDatos;
import Exceptions.*;
import Modelo.Habitaciones.EstadoHabitacion;
import Modelo.Habitaciones.TipoHabitacion;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static Scanner teclado = new Scanner(System.in);

    public static void main(String[] args) {
        Hotel hotel = new Hotel("Hotel California");

        //agregarPasajero(hotel);
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


/*        try {
            System.out.println(hotel.existePasajeroConEseDNI(11111111));
        } catch (BadDataException e)
        {
            System.out.println(e.getMessage());
        }

        try{
            System.out.println(hotel.buscarPasajeroConEseDNI(11111111));
        } catch (PersonaNoExisteException e)
        {
            System.out.println(e.getMessage());
        }

        Empleado empleado1 = new Empleado("Julio","Test",22224444,"carlitoslol","carlitos@gmail.com","helloworld");
        System.out.println("\n");
        System.out.println(empleado1);*/

        //agregarPasajero(hotel);
    }

    static public void agregarPasajero(Hotel hotel)
    {
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

        hotel.agregarPasajero(nombre,apellido,dni,direccion);
    }

    static public String definirNombreoApe(String mensaje)
    {
        String dato = "";
        boolean nomver = false;
        while(!nomver)
        {
            try {
                System.out.println(mensaje);
                dato = teclado.nextLine();
                VerificacionesDeDatos.tieneNumeros(dato);
                nomver = true;
            } catch (BadDataException e)
            {
                System.out.println(e.getMessage());
            }
            catch (InputMismatchException e)
            {
                System.out.println("algo paso");
            }
        }
        return dato;
    }

    static public int definirDni(Hotel hotel)
    {
        int dni = 0;
        boolean dniver = false;

        while(!dniver)
        {
            try {
                System.out.println("Dni");
                dni = Integer.parseInt(teclado.nextLine());
                hotel.verSiElDniEstaCargardo(dni);
                dniver = true;
            } catch (BadDataException | PersonaExisteException e)
            {
                System.out.println(e.getMessage());
            }
            catch (InputMismatchException e)
            {
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

            boolean tipook = false;
            while(!tipook)
            {
                System.out.println("Ingrese el tipo de habitacion: ");
                System.out.println(TipoHabitacion.retornarValoresDeEnum());
                try {
                tipoHab = Integer.parseInt(teclado.nextLine());
                tipook = TipoHabitacion.verificarEntrada(tipoHab);
                } catch (BadOptionException e)
                {
                    System.out.println(e.getMessage());
                }
                catch (InputMismatchException e)
                {
                    System.out.println("No es correcto!!");
                }
            }
            hotel.crearHabitaciones(cantHab,capMaxHab,tipoHab);
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
        System.out.println("Ingrese el numero de la habitacion a gestionar: ");
        int numeroHabitacion = Integer.parseInt(teclado.nextLine());
        for (int i = 1; i <= 2; i++) { // 2 es el nro de tipos de habitacion que existen
            try {
                GestionHabitacion.mostrarMenu(hotel.selectorDeTipoHabitacion(i).traerHabitacionSegunId(numeroHabitacion));
                return;
            } catch (HabitacionNoEncontradaException e) {
            }
        }
        System.out.println("No existe la habitacion ingresada");
    }

}