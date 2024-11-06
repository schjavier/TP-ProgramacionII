import DataChecks.VerificacionesDeDatos;
import Exceptions.*;
import Modelo.Habitaciones.EstadoHabitacion;
import Modelo.Habitaciones.Habitacion;
import Modelo.Habitaciones.TipoHabitacion;
import Modelo.Persona.Persona;
import Modelo.Reserva.Reserva;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
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
            System.out.println("8. Menu Reserva");

            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");

            opcion = Integer.parseInt(teclado.nextLine());

            switch (opcion) {
                case 1:
                    crearHabitaciones(hotel);
                    break;
                case 2:
                    try {
                        System.out.println("Ingrese el numero de la habitacion a gestionar: ");
                        int numerohabi = teclado.nextInt();
                        try {
                            gestionarHabitacion(hotel,numerohabi);
                        } catch (HabitacionNoEncontradaException e) {
                            System.out.println(e.getMessage());
                        }
                    } catch (InputMismatchException e)
                    {
                        System.out.println("Debe ser un numero entero!");
                    } finally {
                        teclado.nextLine();
                    }
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
                case 8:
                    menuReservas(hotel);
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

    static public void agregarPasajero(Hotel hotel,int dni)
    {
        String nombre;
        String apellido;
        String direccion;

        nombre = definirNombreoApe("Nombre");
        apellido = definirNombreoApe("Apellido");
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

    public static void gestionarHabitacion(Hotel hotel, int numeroHabitacion) throws HabitacionNoEncontradaException {
        Habitacion habitaciongestionar = null;
        for (int i = 1; i <= 3; i++) { // 3 es el nro de tipos de habitacion que existen

            try {
                habitaciongestionar = hotel.selectorDeTipoHabitacion(i).traerHabitacionSegunId(numeroHabitacion);
            } catch (BadOptionException e) {
                System.out.println(e.getMessage());
            }

            if(habitaciongestionar != null)
            {
                break;
            }
        }

        if(habitaciongestionar != null)
        {
            GestionHabitacion.mostrarMenu(habitaciongestionar);
        } else
        {
            throw new HabitacionNoEncontradaException("No existe la habitacion ingresada");
        }
    }

    /**
     * Muestra un menu para las reservas.
     * @param hotel
     */
        public static void menuReservas(Hotel hotel){
            int opcion = -1;
            do {

                System.out.println("Gestion de Reservas");
                System.out.println("Que desea hacer?");
                System.out.println("1 - Agregar una reserva");
                System.out.println("2 - Eliminar una reserva");
                System.out.println("3 - Modificar una Reserva Existente");
                System.out.println("0 - Salir");
                opcion = Integer.parseInt(teclado.nextLine());

                switch (opcion) {
                    case 1:
                        try {
                            agregarReserva(hotel);
                        } catch (HabitacionNoEncontradaException | BadDataException e) {
                            System.out.println(e.getMessage());
                        }
                        break;
                    case 2:
                        System.out.println("Ingrese el Id de la reserva que quiere eliminar: ");
                        hotel.eliminarReserva(Integer.parseInt(teclado.nextLine()));
                        break;
                    case 3:
                        System.out.println("Ingrese el Id de la reserva que quiere modificar");
                        int id = Integer.parseInt(teclado.nextLine());
                        int eleccion = -1;
                        do {

                            try {

                                System.out.println("Aqui esta la reserva: " + hotel.mostrarReservaPorId(id) + "\nQue atributo quiere modificar?");
                                System.out.println("1 - Cambiar el dni del titular");
                                System.out.println("2 - Agregar un pasajero a la reserva");
                                System.out.println("3 - Cambiar el numero de habitacion");
                                System.out.println("4 - Cambiar fecha de inicio");
                                System.out.println("5 - Cambiar fecha de finalización");
                                System.out.println("0 - Salir");
                                eleccion = Integer.parseInt(teclado.nextLine());

                                switch (eleccion) {
                                    case 1:
                                        System.out.println("ingrese el dni: ");
                                        int nuevoDni = Integer.parseInt(teclado.nextLine());
                                        hotel.modificarTitularReserva(id, nuevoDni);
                                        break;
                                    case 2:
                                        System.out.println("Ingrese el dni del pasajero");
                                        int dniPasajero = Integer.parseInt(teclado.nextLine());
                                        hotel.agregarPasajeroAReserva(id, dniPasajero);
                                        break;
                                    case 3:
                                        System.out.println("Ingrese el nuevo número de habitación");
                                        int nroHabitacion = Integer.parseInt(teclado.nextLine());
                                        hotel.cambiarNroHabitacion(id, nroHabitacion);
                                        break;
                                    case 4:
                                        System.out.println("Ingrese la nueva fecha de inico (yyyy-mm-dd)");
                                        LocalDate nuevaFechaInicio = LocalDate.parse(teclado.nextLine());
                                        hotel.cambiarFechaInicio(id, nuevaFechaInicio);
                                        break;
                                    case 5:
                                        System.out.println("Ingrese la nueva fecha de finalización (yyyy-mm-dd)");
                                        LocalDate nuevaFechaFinal = LocalDate.parse(teclado.nextLine());
                                        hotel.cambiarFechaInicio(id, nuevaFechaFinal);
                                        break;

                                }

                            } catch (ReservaNoExisteException ex) {
                                System.out.println(ex.getMessage());
                            }

                            break;

                        }while (eleccion != 0) ;
                }
            } while (opcion != 0);


        }

        public static void agregarReserva(Hotel hotel) throws HabitacionNoEncontradaException, BadDataException
        {
            int dniTitular = 0;
            boolean personaexiste = false;
            LocalDate fechaInicio = null;
            LocalDate fechaFinal = null;

            dniTitular = agregarPersonaAReservaCreando(hotel);

            fechaInicio = asignarFechaAEvento("inicio");
            fechaFinal = asignarFechaAEvento("final");

            ArrayList<Integer> dnipasajeros = new ArrayList<>();



            Reserva intentoreserva = new Reserva(dniTitular,fechaInicio,fechaFinal,00000000); // deberiamos tener el dni del empleado
            VerificacionesDeDatos.fechaTieneSentido(intentoreserva);


            boolean seguiragregandopersonas = true;
            while(seguiragregandopersonas)
            {
                int eleccion = -1;
                System.out.println("Hay alguien mas a quien agregar??");
                System.out.println("1- Si");
                System.out.println("Cualquier otro numero para no.");

                try {
                    eleccion = teclado.nextInt();
                    if (eleccion == 1) {
                        intentoreserva.agregarPersonaAReserva(agregarPersonaAReservaCreando(hotel));
                    } else {
                        seguiragregandopersonas = false;
                    }
                } catch (InputMismatchException e)
                {
                    System.out.println("Por favor usar numeros!!");
                }
            }

            System.out.println("Pasajeros:" + intentoreserva.getPasajeros());
            System.out.println("Cantidad:" + intentoreserva.getCantidadPersonasEnReserva());


            ArrayList<Integer> numerosDeHabitacion;
            try {
                numerosDeHabitacion = hotel.verHabitacionesDisponibles(intentoreserva);
            } catch (HabitacionNoEncontradaException e) {
                throw new HabitacionNoEncontradaException(e.getMessage());
            }

            boolean correcto = false;
            int numeroHabitacion = 0;
            while(!correcto)
            {
                System.out.println("Ingrese el Nro. de Habitación de la reserva");
                System.out.println("Por favor, seleccionar una de las siguientes habitaciones." + numerosDeHabitacion);
                try {
                    numeroHabitacion = teclado.nextInt();

                    if(numerosDeHabitacion.contains(numeroHabitacion))
                    {
                       correcto = true;
                    } else
                    {
                        System.out.println("Debe ser uno de los numeros incluidos");
                    }
                } catch (InputMismatchException e)
                {
                    System.out.println("Por favor usar numeros!");
                }
            }


            intentoreserva.asignarHabitacionAReservaYLlenarDatosFaltantes(numeroHabitacion);

            hotel.generarReserva(dniTitular, numeroHabitacion, fechaInicio, fechaFinal, guardadoPor,dnipasajeros);
        }


    /**
     * Para cargar una persona en una reserva, aplica para titular y pasajeros.
     * @param hotel
     * @return
     */
    public static int agregarPersonaAReservaCreando(Hotel hotel)
        {
            boolean personaexiste = false;
            int dniPersona = 0;
            while(!personaexiste)
            {
                try {
                    System.out.println("Ingrese el DNI del titular de la reserva");
                    dniPersona = Integer.parseInt(teclado.nextLine());
                    try {
                        hotel.buscarPasajeroConEseDNI(dniPersona);
                        personaexiste = true;
                    } catch (PersonaNoExisteException e)
                    {
                        System.out.println(e.getMessage());
                        agregarPasajero(hotel, dniPersona);
                        personaexiste = true;
                    } catch (BadDataException e) {
                        System.out.println("Dni esta mal porque: \n" + e.getMessage());
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Tiene que tener numeros!");
                }
            }
            return dniPersona;
        }


    /**
     * Asigna una fecha al inicio o final de reserva
     * @return Retorna objeto LocalDate
     */
    public static LocalDate asignarFechaAEvento(String tipo)
        {
            LocalDate fecha = null;
            boolean fechavalida = false;
            while(!fechavalida)
            {
                try {
                    System.out.println("Ingrese fecha ( yyyy-mm-dd ) de " + tipo + " de la reserva");

                    String fechainiciostring = teclado.nextLine();
                    fecha = LocalDate.parse(fechainiciostring);

                    if(VerificacionesDeDatos.fechaAntesDeHoy(fecha))
                    {
                        throw new BadDataException("La fecha de inicio no puede ser antes de hoy!");
                    }
                    fechavalida = true;
                } catch (DateTimeParseException e)
                {
                    System.out.println("La fecha esta incorrectamente escrita! (Usar guiones, no simbolo menos.)");
                } catch (BadDataException e)
                {
                    System.out.println(e.getMessage());
                }
            }
            return fecha;
        }
}