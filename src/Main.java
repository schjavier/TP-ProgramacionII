import DataChecks.VerificacionesDeDatos;
import Exceptions.*;
import Modelo.Habitaciones.EstadoHabitacion;
import Modelo.Habitaciones.Habitacion;
import Modelo.Habitaciones.TipoHabitacion;
import Modelo.Reserva.Reserva;

import java.time.LocalDate;
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

    static public void agregarPasajero(Hotel hotel,int dni) throws BadDataException, PersonaExisteException {

        if(hotel.existeEmpleadoConEseDNI(dni)) // check en caso de que el dni sea de un empleado.
        {
            throw new PersonaExisteException("Empleados no pueden ser parte de reservas. Saliendo...");
        }
        System.out.println("Pasajero no encontrado, cargando al sistema...");
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

    public static void listarHabitacionesEstado(Hotel hotel) {
        int tipoHab;
        int estadoSeleccionado;

        System.out.println("Ingrese el tipo de habitacion: ");
        System.out.println(TipoHabitacion.retornarValoresDeEnum());
        tipoHab = Integer.parseInt(teclado.nextLine());

        System.out.println("Ingrese el estado: ");
        System.out.println(EstadoHabitacion.retornarValoresDeEnum());
        estadoSeleccionado = Integer.parseInt(teclado.nextLine());

        if (estadoSeleccionado < EstadoHabitacion.values().length) {
            System.out.println(hotel.listarSegunEstado(tipoHab, EstadoHabitacion.values()[estadoSeleccionado - 1]));
        } else {
            System.out.println("El numero no es valido, intente nuevamente");
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

    public static void gestionarHabitacion(Hotel hotel) throws HabitacionNoEncontradaException, NullNameException {
        Habitacion prueba = null;

        System.out.println("Ingrese el numero de la habitacion a gestionar: ");

        try {
            int numeroHabitacion = Integer.parseInt(teclado.nextLine());
            prueba = hotel.buscarHabitacionPorNumero(numeroHabitacion);
            GestionHabitacion.mostrarMenu(prueba);
            hotel.hacerBackup();
        } catch (NumberFormatException e)
        {
            System.out.println("Solo usar numeros!!!");
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

    /**
     * Marca las habitaciones como revisadas en la cocina
     * @param hotel
     * @throws NullNameException
     */
    public static void revisarCocinasHabitaciones(Hotel hotel) throws NullNameException {
        hotel.revisarCocinasHabitaciones();
        System.out.println("Revisión de cocinas completa!");
    }

    /**
     * Marca las habitaciones como revisadas en el jacuzzi
     * @param hotel
     * @throws NullNameException
     */
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
            System.out.println("4. Ver reservas hechas por titular");
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
                    } catch (HabitacionNoEncontradaException | BadDataException | ReservaExisteException | PersonaExisteException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 2:
                    System.out.println("Ingrese el Id de la reserva que quiere eliminar: ");
                    hotel.eliminarReserva(Integer.parseInt(teclado.nextLine()));
                    break;
                case 3:
                    int id_reserva = -1;
                    System.out.println("Ingrese el Id de la reserva que quiere modificar");
                    try {
                        String numeroIngresado = teclado.nextLine();
                        esSoloNumeros(numeroIngresado);
                        id_reserva = Integer.parseInt(numeroIngresado);
                    } catch (BadDataException e) {
                        System.out.println(e.getMessage());
                    }
                    int eleccion = -1;
                    do {
                        try {
                            Reserva reservaEncontrada = hotel.buscarReservaSegunId(id_reserva);
                            System.out.println(reservaEncontrada + "\nSeleccione una opción");
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
                                    try {
                                        int nuevoDni = agregarPersonaAReservaCreando(hotel, "Titular");
                                        hotel.modificarTitularReserva(id_reserva, nuevoDni);
                                    } catch (PersonaExisteException | ReservaExisteException | BadDataException e) {
                                        System.out.println(e.getMessage());
                                    }
                                    break;
                                case 2:
                                    System.out.println("Ingrese el dni del pasajero");
                                    try {
                                        int dniPasajero = agregarPersonaAReservaCreando(hotel, "Titular");
                                        hotel.agregarPasajeroAReserva(id_reserva, dniPasajero);
                                    } catch (PersonaExisteException | HabitacionNoEncontradaException |
                                             ReservaNoExisteException e) {
                                        System.out.println(e.getMessage());
                                    }

                                    break;
                                case 3:
                                    try {
                                        int numerohabinew = selectorDeHabitacionDisponible(hotel, reservaEncontrada);
                                        hotel.cambiarNroHabitacionDeReserva(id_reserva, numerohabinew);
                                    } catch (HabitacionNoEncontradaException e) {
                                        System.out.println(e.getMessage());
                                    }
                                    break;
                            }

                        } catch (ReservaNoExisteException ex) {
                            System.out.println(ex.getMessage());
                        }

                        break;

                    } while (eleccion != 0);
                    break;
                case 4:
                    System.out.println("Ingrese el numero de DNI de la persona que quiere conocer sus reservas: ");
                    try {
                        int nroDni = Integer.parseInt(teclado.nextLine());
                        hotel.buscarPersonaPorDni(nroDni); // para que tire exception si la persona no existe
                        System.out.println(hotel.historicoPorTitular(nroDni));
                    } catch (PersonaNoExisteException | BadDataException e) {
                        System.out.println(e.getMessage());
                    }
            }
        } while (opcion != 0);


    }

    public static void agregarReserva(Hotel hotel) throws HabitacionNoEncontradaException, BadDataException, ReservaExisteException, PersonaExisteException {
        int dniTitular = 0;
        LocalDate fechaInicio = null;
        LocalDate fechaFinal = null;

        dniTitular = agregarPersonaAReservaCreando(hotel, "titular");
        if (hotel.buscarReservaActivaPorTitular(dniTitular)) {
            throw new ReservaExisteException("Ya hay una reserva activa para esta persona!");
        }

        fechaInicio = asignarFechaAEvento("inicio");
        fechaFinal = asignarFechaAEvento("final");


        Reserva intentoReserva = new Reserva(dniTitular, fechaInicio, fechaFinal, hotel.obtenerDniEmpleadoLogueado());
        VerificacionesDeDatos.fechaTieneSentido(intentoReserva);

        boolean seguirAgregandoPersonas = true;
        while (seguirAgregandoPersonas) {
            int eleccion = -1;
            System.out.println("Hay alguien mas a quien agregar??");
            System.out.println("1. Si");
            System.out.println("Cualquier otro numero para no.");

            try {
                eleccion = teclado.nextInt();
                if (eleccion == 1) {
                    intentoReserva.agregarPersonaAReserva(agregarPersonaAReservaCreando(hotel, "pasajero"));
                } else {
                    seguirAgregandoPersonas = false;
                }
            } catch (InputMismatchException e) {
                System.out.println("Por favor usar numeros!!");
            }
        }

        System.out.println("Pasajeros:" + intentoReserva.getPasajeros());
        for (Integer dni : intentoReserva.getPasajeros()) {
            try {
                System.out.println(hotel.buscarPasajeroConEseDNI(dni));
            } catch (PersonaNoExisteException e) {
                System.out.println("La " + dni + " no existe?");
            }
            System.out.println("---");
        }

        System.out.println("Cantidad:" + intentoReserva.getCantidadPersonasEnReserva());
        int numeroHabitacion = selectorDeHabitacionDisponible(hotel, intentoReserva);


        intentoReserva.asignarHabitacionAReservaYLlenarDatosFaltantes(numeroHabitacion);
        hotel.generarReserva(intentoReserva);
    }


    /**
     *
     * @param hotel sistema
     * @param tipo mensaje a pantalla
     * @return
     * @throws PersonaExisteException En el caso de que la persona que se quiere hacer la reserva, sea un empleado, se expulsara inmediato.
     */
    public static int agregarPersonaAReservaCreando(Hotel hotel, String tipo) throws PersonaExisteException {
        boolean personaexiste = false;
        int dniPersona = 0;
        while (!personaexiste) {
            try {
                System.out.println("Ingrese el DNI del " + tipo + " de la reserva");
                dniPersona = Integer.parseInt(teclado.nextLine());

                try {
                    if (!hotel.existePasajeroConEseDNI(dniPersona)) {
                        agregarPasajero(hotel, dniPersona);
                    }
                    personaexiste = true;
                } catch (BadDataException e) {
                    System.out.println(e.getMessage());
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
    public static LocalDate asignarFechaAEvento(String tipo) {
        LocalDate fecha = null;
        boolean fechavalida = false;
        while (!fechavalida) {
            try {
                System.out.println("Ingrese fecha ( yyyy-mm-dd ) de " + tipo + " de la reserva");

                String fechainiciostring = teclado.nextLine();
                fecha = LocalDate.parse(fechainiciostring);

                if (VerificacionesDeDatos.fechaAntesDeHoy(fecha)) {
                    throw new BadDataException("La fecha de inicio no puede ser antes de hoy!");
                }
                fechavalida = true;
            } catch (DateTimeParseException e) {
                System.out.println("La fecha esta incorrectamente escrita! (Usar guiones, no simbolo menos.)");
            } catch (BadDataException e) {
                System.out.println(e.getMessage());
            }
        }
        return fecha;
    }

    static public int selectorDeHabitacionDisponible(Hotel hotel, Reserva intentoreserva) throws HabitacionNoEncontradaException {
        ArrayList<Integer> numerosDeHabitacion;
        try {
            numerosDeHabitacion = hotel.verHabitacionesDisponibles(intentoreserva);
        } catch (HabitacionNoEncontradaException e) {
            throw new HabitacionNoEncontradaException(e.getMessage());
        }

        boolean correcto = false;
        int numeroHabitacion = 0;
        while (!correcto) {
            System.out.println("Ingrese el Nro. de Habitación de la reserva");
            for (Integer num : numerosDeHabitacion) {
                System.out.println(hotel.traerDatosDeHabitacionSegunNum(num));
            }
            System.out.println("Por favor, seleccionar una de las habitaciones listadas. ");


            try {
                numeroHabitacion = teclado.nextInt();

                if (numerosDeHabitacion.contains(numeroHabitacion)) {
                    correcto = true;
                } else {
                    System.out.println("Debe ser uno de los numeros incluidos");
                }
            } catch (InputMismatchException e) {
                System.out.println("Por favor usar numeros!");
            }
        }
        return numeroHabitacion;
    }
}