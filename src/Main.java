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
import Modelo.Persona.TipoEmpleado;
import java.util.InputMismatchException;
import java.util.Scanner;

import static DataChecks.VerificacionesDeDatos.esSoloNumeros;

public class Main {
    public static Scanner teclado = new Scanner(System.in);

    public static void main(String[] args) {
        Hotel hotel = new Hotel("Hotel California");
        boolean continuar = true;

        while(continuar) {
            try {
                menuInicio(hotel);
                continuar = false;

            } catch (BadOptionException | BadDataException | UsuarioNoAutorizadoException | NullNameException ex){
                System.out.println(ex.getMessage());
            }
        }
    }

    public static void menuInicio(Hotel hotel) throws BadOptionException, NullNameException, UsuarioNoAutorizadoException, BadDataException {
        int opcion = 0;

        do {

            System.out.println("\n--- Menu principal ---");
            System.out.println("1. Iniciar sesion / ir al menú");
            System.out.println("2. Cerrar sesion");
            System.out.println("0. Salir");

            System.out.println("Ingrese una opcion: ");

            try {
                String numeroIngresado = teclado.nextLine();
                esSoloNumeros(numeroIngresado);
                opcion = Integer.parseInt(numeroIngresado);
            } catch (BadDataException e) {
                System.out.println("Solo se aceptan números!");
                opcion = 0; //para prevenir comportamientos inesperados
            }

            switch (opcion) {
                case 1:
                    try {
                        if (hotel.obtenerEmpleadoLogueado() == null) {
                            menuLogin(hotel);
                        }

                        System.out.println("Bienvenido " + hotel.obtenerEmpleadoLogueado().getNombre() + " a " + hotel.getNombre() + "!");

                        if (hotel.obtenerEmpleadoLogueado().getTipo() == TipoEmpleado.ADMINISTRADOR) {
                            menuGestionAdmin(hotel);
                        } else if (hotel.obtenerEmpleadoLogueado().getTipo() == TipoEmpleado.RECEPCIONISTA) {
                            menuGestionRecepcionista(hotel);
                        }
                    } catch (PersonaNoExisteException e) {
                        System.out.println("Los datos ingresados son incorrectos o el empleado no existe");
                    }
                    break;
                case 2:
                    hotel.logOut();
                    break;
                case 0:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Elija una opcion valida");
            }
        } while (opcion != 0);
    }

    static public void menuLogin(Hotel hotel) throws PersonaNoExisteException, BadDataException, NullNameException {
        String username;
        String clave;

        if (hotel.obtenerCantidadEmpleados() == 0) {
            System.out.println("El hotel no tiene ningun empleado. Debe crearse un administrador para continuar");
            crearEmpleado(hotel,true);
        }

        System.out.println("Usuario: ");
        username = teclado.nextLine();
        System.out.println("Clave: ");
        clave = teclado.nextLine();

        hotel.intentarIniciarSesion(username,clave);
    }

    static public void crearEmpleado(Hotel hotel, boolean esPrimerUsuario) throws NullNameException, BadDataException {
        String nombre;
        String apellido;
        int dni;
        String usuario;
        String email;
        String clave;
        int nroTipoEmpleado;

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
            nroTipoEmpleado = Integer.parseInt(teclado.nextLine());
            if (nroTipoEmpleado < TipoEmpleado.values().length) {
                hotel.crearEmpleado(nombre,apellido,dni,usuario,email,clave,TipoEmpleado.values()[nroTipoEmpleado-1]);
            } else {
                System.out.println("El numero no es valido, intente nuevamente");
            }
        } else {
            hotel.crearEmpleado(nombre,apellido,dni,usuario,email,clave,TipoEmpleado.ADMINISTRADOR);
        }
    }

    static public void menuGestionAdmin(Hotel hotel) throws UsuarioNoAutorizadoException, NullNameException, BadOptionException, BadDataException {
        if (hotel.obtenerEmpleadoLogueado().getTipo() != TipoEmpleado.ADMINISTRADOR) {
            throw new UsuarioNoAutorizadoException("El usuario no tiene permisos para este menu");
        }

        int opcion = 0;

        do {
            System.out.println("\n--- Menu del administrador ---");
            System.out.println("1. Crear habitacion(es)");
            System.out.println("2. Gestionar una habitacion por su ID");
            System.out.println("3. Eliminar una habitacion por su ID");
            System.out.println("4. Crear un empleado");
            System.out.println("5. Ver lista de empleados");
            System.out.println("6. Gestionar un empleado");
            System.out.println("7. Eliminar un empleado");
            System.out.println("8. Hacer backup preventivo");
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
                case 1:
                    crearHabitaciones(hotel);
                    break;
                case 2:
                    try {
                        gestionarHabitacion(hotel);
                    } catch (HabitacionNoEncontradaException e) {
                        System.out.println("La habitacion no pudo ser encontrada o no existe");
                    }
                    break;
                case 3:
                    eliminarHabitacion(hotel);
                    break;
                case 4:
                    crearEmpleado(hotel,false);
                    break;
                case 5:
                    System.out.println(hotel.verEmpleados());
                    break;
                case 6:
                    gestionarPersona(hotel);
                    break;
                case 7:
                    eliminarEmpleado(hotel);
                    break;
                case 8:
                    hotel.hacerBackup();
                    break;
                case 0:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
            }
        } while (opcion != 0);
    }

    static public void menuGestionRecepcionista(Hotel hotel) throws UsuarioNoAutorizadoException, BadOptionException, NullNameException {
        if (hotel.obtenerEmpleadoLogueado().getTipo() != TipoEmpleado.RECEPCIONISTA) {
            throw new UsuarioNoAutorizadoException("El usuario no tiene permisos para este menu");
        }

        int opcion = 0;

        do {
            System.out.println("\n--- Menu del recepcionista ---");
            System.out.println("1. Gestionar una habitacion por su ID");
            System.out.println("2. Listar todas las habitaciones");
            System.out.println("3. Listar habitaciones de acuerdo a su tipo y estado");
            System.out.println("4. Obtener conteo total de habitaciones");
            System.out.println("5. Obtener un conteo de todas las habitaciones segun el estado");
            System.out.println("6. Revisar cocinas de las habitaciones");
            System.out.println("7. Revisar jacuzzis de las habitaciones");
            System.out.println("8. Gestionar reservas");
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
                case 1:
                    try {
                        gestionarHabitacion(hotel);
                    } catch (HabitacionNoEncontradaException e) {
                        System.out.println("La habitacion no pudo ser encontrada o no existe");
                    }
                    break;
                case 2:
                    System.out.println(hotel.listarHabitaciones());
                    break;
                case 3:
                    listarHabitacionesEstado(hotel);
                    break;
                case 4:
                    System.out.println("Habitaciones totales: " + hotel.obtenerNroHabitaciones());
                    break;
                case 5:
                    contarHabitacionesEstado(hotel);
                    break;
                case 6:
                    revisarCocinasHabitaciones(hotel);
                    break;
                case 7:
                    revisarJacuzzisHabitaciones(hotel);
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
    }

    static public void agregarPasajero(Hotel hotel) throws NullNameException {
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
                System.out.println("Dni: ");
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

    public static void crearHabitaciones(Hotel hotel) throws NullNameException {
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

    // Me parece que en este metodo de abajo, no se si iria el bloque try...
    // no encontre el metodo que lanza la exception.


    public static void listarHabitacionesEstado(Hotel hotel) {
        int tipoHab;
        int estadoSeleccionado;

        try {
            System.out.println("Ingrese el tipo de habitacion: ");
            System.out.println(TipoHabitacion.retornarValoresDeEnum());
            tipoHab = Integer.parseInt(teclado.nextLine());

            System.out.println("Ingrese el estado: ");
            System.out.println(EstadoHabitacion.retornarValoresDeEnum());
            estadoSeleccionado = Integer.parseInt(teclado.nextLine());

            if (estadoSeleccionado < EstadoHabitacion.values().length) {
                System.out.println(hotel.listarSegunEstado(tipoHab,EstadoHabitacion.values()[estadoSeleccionado - 1]));
            } else {
                System.out.println("El numero no es valido, intente nuevamente");
            }
        } catch (Exception ex) {
            System.out.println("No existe el tipo de habitacion!");
        }
    }

    public static void contarHabitacionesEstado(Hotel hotel) {
        int estadoSeleccionado;
        int totalHabitaciones;

        System.out.println("Ingrese el estado: ");
        System.out.println(EstadoHabitacion.retornarValoresDeEnum());
        estadoSeleccionado = Integer.parseInt(teclado.nextLine());

        if (estadoSeleccionado < EstadoHabitacion.values().length) {
            totalHabitaciones = hotel.obtenerNroHabitacionesSegunEstado(EstadoHabitacion.values()[estadoSeleccionado - 1]);
            System.out.println("Total de habitaciones: " + totalHabitaciones);
        } else {
            System.out.println("El numero no es valido, intente nuevamente");
        }

    }

    /**
     * Muestra un menu para gestionar una habitacion segun el numero ingresado por teclado
     */

    public static void gestionarHabitacion(Hotel hotel) throws HabitacionNoEncontradaException, BadOptionException, NullNameException {
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
            hotel.hacerBackup();
        }
    }

    public static void gestionarPersona(Hotel hotel) throws NullNameException {
        try {
            System.out.println("Ingrese el numero de dni de la persona a gestionar: ");
            int dniPersona = Integer.parseInt(teclado.nextLine());
            GestionPersona.mostrarMenu(hotel.buscarPersonaPorDni(dniPersona),hotel);
            hotel.hacerBackup();
        } catch (PersonaNoExisteException e) {
            System.out.println("El dni ingresado no se encuentra en los registros del hotel");
        } catch (BadDataException e) {
            System.out.println(e.getMessage());
        }
    }

    public static String definirUsuario(Hotel hotel) throws BadDataException {
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

    public static void eliminarEmpleado(Hotel hotel) throws NullNameException {
        int dniEmpleado = 0;
        boolean dniVerificado = false;

        try {
            System.out.println("Ingrese el numero de dni del empleado a eliminar: ");
            dniEmpleado = Integer.parseInt(teclado.nextLine());
            dniVerificado = hotel.existeEmpleadoConEseDNI(dniEmpleado);

            if (dniEmpleado != hotel.obtenerDniEmpleadoLogueado() && dniVerificado) {
                hotel.eliminarEmpleadoPorElDni(dniEmpleado);
                System.out.println("El empleado ha sido eliminado exitosamente");
            } else if (dniEmpleado == hotel.obtenerDniEmpleadoLogueado()) {
                System.out.println("No se puede eliminar al empleado que está operando en este momento");
            } else {
                System.out.println("El dni ingresado no corresponde a un empleado o ya fue eliminado");
            }

        } catch (BadDataException e) {
            System.out.println("No es un dni");
        }
    }

    public static void revisarCocinasHabitaciones(Hotel hotel) throws NullNameException {
        hotel.revisarCocinasHabitaciones();
        System.out.println("Revisión de cocinas completa!");
    }

    public static void revisarJacuzzisHabitaciones(Hotel hotel) throws NullNameException {
        hotel.revisarJacuzzisHabitaciones();
        System.out.println("Revisión de jacuzzis completa!");
    }

    /**
     * Muestra un menu para las reservas.
     * @param hotel
     */
    public static void menuReservas(Hotel hotel){
        int opcion = -1;
        do {

            System.out.println("\n--- Gestión reservas ---");
            System.out.println("Ingrese una opción: ");
            System.out.println("1. Agregar una reserva");
            System.out.println("2. Eliminar una reserva");
            System.out.println("3. Modificar una Reserva Existente");
            System.out.println("0. Salir");
            try {
                String numeroIngresado = teclado.nextLine();
                esSoloNumeros(numeroIngresado);
                opcion = Integer.parseInt(numeroIngresado);
            } catch (BadDataException e) {
                System.out.println("Solo se aceptan números!");
                opcion = 0; //para prevenir comportamientos inesperados
            }

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

                            System.out.println("Reserva: " + hotel.mostrarReservaPorId(id) + "\nSeleccione una opción");
                            System.out.println("1. Cambiar el dni del titular");
                            System.out.println("2. Agregar un pasajero a la reserva");
                            System.out.println("3. Cambiar el numero de habitacion");
                            System.out.println("4. Cambiar fecha de inicio");
                            System.out.println("5. Cambiar fecha de finalización");
                            System.out.println("0. Salir");
                            try {
                                String numeroIngresado = teclado.nextLine();
                                esSoloNumeros(numeroIngresado);
                                eleccion = Integer.parseInt(numeroIngresado);
                            } catch (BadDataException e) {
                                System.out.println("Solo se aceptan números!");
                                eleccion = 0; //para prevenir comportamientos inesperados
                            }

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

        // aca le mande la reserva para que la genere...
        hotel.generarReserva(intentoreserva);
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