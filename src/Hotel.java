import DataChecks.VerificacionesDeDatos;
import Exceptions.*;
import JSONCreator.CreadorAJSON;
import Modelo.Habitaciones.*;
import Modelo.Persona.*;
import Modelo.Reserva.Reserva;
import Modelo.Reserva.ReservaService;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 *
 * Clase wrapper del Sistema de gestion Hotelera.
 *
 * @author JulianAlonso
 * @author TomasSilva
 * @author JavierSchettini
 */
public class Hotel {

    private String nombre;
    private Empleado empleadoEnSistema = null;

    private final HabitacionesStandard habitacionesStandard = new HabitacionesStandard();
    private final HabitacionesSuite habitacionesSuite = new HabitacionesSuite();
    private final HabitacionesPresidenciales habitacionesPresidenciales = new HabitacionesPresidenciales();
    private final HabitacionService habitacionesService = new HabitacionService();

    private final ArrayList<Pasajero> pasajeros = new ArrayList<>();
    private final PasajeroService pasajeroService = new PasajeroService();

    private final ArrayList<Empleado> empleados = new ArrayList<>();
    private final EmpleadoService empleadoService = new EmpleadoService();

    private final ArrayList<Reserva> reservas = new ArrayList<>();
    private final ReservaService reservaService = new ReservaService();

    /**
     * Constructor del la clase
     *
     * @param nombre <code>String</code> el nombre del hotel
     */
    public Hotel(String nombre) {
        this.nombre = nombre;

            cargarJSONPasajeros();
            cargarJSONEmpleados();
            cargarJSONHabitaciones();
            cargarJSONReservas();

    }

    /**
     * Devuelve el nombre asignado al hotel
     *
     * @return un String que representa el nombre del hotel
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Permite crear muchas habitaciones a la vez. Se crean todas como DISPONIBLE.
     *
     * @param tipoHabitacion Un numero del 1 al x siendo x el ultimo tipo de habitacion que haya
     */
    public void crearHabitaciones(int cantidadHabitaciones, int capacidadMaxima, int tipoHabitacion) throws BadOptionException {
        Habitaciones habitaciones = null;
        try {
            habitaciones = selectorDeTipoHabitacion(tipoHabitacion);

            for (int i = 0; i < cantidadHabitaciones; i++) {
                switch (tipoHabitacion) {
                    case 1 -> habitaciones.agregarHabitacion(new HabitacionStandard(capacidadMaxima));
                    case 2 -> habitaciones.agregarHabitacion(new HabitacionSuite(capacidadMaxima));
                    case 3 -> habitaciones.agregarHabitacion(new HabitacionPresidencial(capacidadMaxima));
                }
            }
        } catch (BadOptionException e) {
            throw new BadOptionException("Ese tipo de habitacion no existe!");
        }

        habitacionesService.persistir(todasLasHabitacionesAJSON());
    }

    /**
     * Metodo que crea un empleado.
     *
     * @param nombre String
     * @param apellido String
     * @param dni entero
     * @param usuario String nombre del usuario
     * @param email String email del usuario
     * @param clave String password
     * @param tipoEmpleado Enum tipo de empleado
     *
     */

    public void crearEmpleado(String nombre, String apellido, int dni, String usuario, String email, String clave, TipoEmpleado tipoEmpleado){
        empleados.add(new Empleado(nombre, apellido, dni, usuario, email, clave, tipoEmpleado));
        empleadoService.persistir(pasarListaDeEmpleadosAJSON());
    }

    /**
     * Lista todas las habitaciones en el sistema.
     * @return StringBuilder que representa todas las habitaciones
     */

    public StringBuilder listarHabitaciones() {
        return habitacionesStandard.listarHabitaciones().append(habitacionesSuite.listarHabitaciones()
                .append(habitacionesPresidenciales.listarHabitaciones()));
    }

    /**
     *
     * @param habitacion Int que representa el numero de habitacion a eliminar.
     * @return devuelve true si puede eliminarla, false de otra forma.
     */

    //Que elimine habitaciones solo con el numero
    public boolean eliminarHabitacion(int habitacion) {
        if (habitacionesStandard.eliminarHabitacionSegunNumero(habitacion)) {
            return true;
        } else if (habitacionesSuite.eliminarHabitacionSegunNumero(habitacion)) {
            return true;
        } else if (habitacionesPresidenciales.eliminarHabitacionSegunNumero(habitacion)) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * @param tipoHabitacion Un numero del 1 al x siendo x el ultimo tipo de habitacion que haya
     * @return La lista que en la que se trabajara
     * @throws BadOptionException En el caso de que el id de tipo habitacion (lista) no este dentro del switch
     */
    public Habitaciones selectorDeTipoHabitacion(int tipoHabitacion) throws BadOptionException {
        return switch (tipoHabitacion) {
            case 1 -> habitacionesStandard;
            case 2 -> habitacionesSuite;
            case 3 -> habitacionesPresidenciales;
            default -> throw new BadOptionException("Elegir una opcion correcta!!");
        };
    }

    /**
     * Metodo que revisa todas las cocinas de las habitaciones.
     * Marca todas las habitaciones como revisadas y persiste la informacion.
     *
     */

    public void revisarCocinasHabitaciones(){
        habitacionesSuite.marcarTodasHabitacionesComoRevisadas();
        habitacionesPresidenciales.marcarTodasHabitacionesComoRevisadas();
        habitacionesService.persistir(todasLasHabitacionesAJSON());
    }

    /**
     * Metodo que revisa todos los jacuzzis de las habitaciones.
     * Marca todas las habitaciones como revisadas y persiste la informacion.
     *
     */

    public void revisarJacuzzisHabitaciones(){
        habitacionesPresidenciales.marcarTodasHabitacionesComoRevisadasJacuzzi();
        habitacionesService.persistir(todasLasHabitacionesAJSON());
    }

    /**
     *
     * @param tipohabitacion entero que representa el tipo de habitacion.
     * @param estado <code>Enum EstadoHabitacion</code> estado actual de la habitacion.
     * @return <code>StringBuilder</code> que representa todas las habitacion segun el estado.
     */
    public StringBuilder listarSegunEstado(int tipohabitacion, EstadoHabitacion estado) {
        StringBuilder todos = new StringBuilder();
        try {
            todos = selectorDeTipoHabitacion(tipohabitacion).listarHabitacionesSegunEstado(estado);
        } catch (BadOptionException e) {
            todos.append(e.getMessage());
        }
        return todos;
    }

    /**
     * Metodo que devuelve la cantidad de empleados
     * @return <code>int</code> cantidad de empleados
     */
    public int obtenerCantidadEmpleados() {
        return empleados.size();
    }

    /**
     * Metodo que devuelve todos los empleados.
     * @return un <code>StringBuilder</code> que representa todos los empleados
     */
    public StringBuilder verEmpleados() {
        StringBuilder resultado = new StringBuilder("Lista de empleados: \n\n");

        for (Empleado empleado : empleados) {
            resultado.append(empleado.toString());
            resultado.append("\n");
        }

        return resultado;
    }

    /**
     * Metodo Que verifica si existe un pasajero segun el DNI.
     *
     * @param dni <code>int</code> numero de DNI.
     * @return <code>true</code> si encuentra el dni del pasajero. <code>false</code> de otra forma.
     * @throws BadDataException Puede lanzar una excepcion de este tipo, si no se puede verificar el dni que llega por parametro
     */

    public boolean existePasajeroConEseDNI(int dni) throws BadDataException { // para hacer reservas o alguna otra cosa
        VerificacionesDeDatos.verificarDni(dni);

        boolean existe = false;
        for (Pasajero pasajero : pasajeros) {
            if (pasajero.getDni() == dni) {
                existe = true;
                break;
            }
        }
        return existe;
    }

    /**
     * Metodo que busca un pasajero segun el DNI.
     * @param dni <code>int</code> que representa el DNI del pasajero a buscar.
     * @return devuelve el objeto <code>Pasajero</code> que corresponde a ese DNI.
     * @throws PersonaNoExisteException Puede lanzar esta excepcion si el pasajero que busca, no existe.
     * @throws BadDataException Puede lanzar esta excepcion si el DNI no pude ser verificado.
     */
    public Pasajero buscarPasajeroConEseDNI(int dni) throws PersonaNoExisteException, BadDataException // para mostrarDatos de una o mas personas (puede ser de las reservas no?)
    {
        VerificacionesDeDatos.verificarDni(dni);
        Pasajero persona = null;
        for (Pasajero pasajero : pasajeros) {
            if (pasajero.getDni() == dni) {
                persona = pasajero;
                break;
            }
        }

        if (persona == null) {
            throw new PersonaNoExisteException("Persona con el DNI: " + dni + " no existe, por favor cargar persona con ese dni");
        }

        return persona;
    }

    /**Metodo que busca una persona, sea empleado o pasajero segun el DNI pasado por parametro.
     *
     * @param dni <code>int</code> representa el numero de DNI de la persona a buscar
     * @return devuelve el objeto <code>Persona</code> correspondiente a ese DNI.
     * @throws PersonaNoExisteException si la persona buscada no existe lanza esta excepcion.
     * @throws BadDataException Puede lanzar esta escepcion si el dni no puede ser verificado.
     */

    public Persona buscarPersonaPorDni(int dni) throws PersonaNoExisteException, BadDataException {
        VerificacionesDeDatos.verificarDni(dni);

        for (Empleado empleado : empleados) {
            if (empleado.getDni() == dni) {
                return empleado;
            }
        }

        for (Pasajero pasajero : pasajeros) {
            if (pasajero.getDni() == dni) {
                return pasajero;
            }
        }

        throw new PersonaNoExisteException("Persona con el DNI: " + dni + " no existe.");
    }

    /**
     * Metodo que corrobora que exista un empleado con el dni pasado por parametro,
     *
     * @param dni <code>int</code> que representa el numero de documento.
     * @return <code>true</code> si existe el empleado.
     *         <code>false</code> de cualquier otro modo.
     * @throws BadDataException puede lanzar esta excepcion si no puede verificar el DNI.
     */
    public boolean existeEmpleadoConEseDNI(int dni) throws BadDataException {
        VerificacionesDeDatos.verificarDni(dni);
        boolean existe = false;
        for (Empleado empleado : empleados) {
            if (empleado.getDni() == dni) {
                existe = true;
                break;
            }
        }
        return existe;
    }

    /**
     * Metodo que verifica su el DNI esta cargado en el sistema.
     * @param dni <code>int</code> que representa el numero de DNI
     * @throws PersonaExisteException Lanza esta excepcion si existe un empleado o pasajero con el DNI.
     * @throws BadDataException Puede lanzar esta excepcion si no puede verificar el DNI pasado por parametro.
     */
    public void verSiElDniEstaCargado(int dni) throws PersonaExisteException, BadDataException {
        VerificacionesDeDatos.verificarDni(dni);

        if (existeEmpleadoConEseDNI(dni) || existePasajeroConEseDNI(dni)) {
            throw new PersonaExisteException("Hay alguien con el dni " + dni);
        }
    }

    /**
     * Metodo para agreagr un Pasajero.
     * Crea un obejeto <code>Pasajero</code>, lo agrega a la lista de pasajeros, y guarda los cambios.
     *
     * @param nombre <code>String</code> nombre del pasajero
     * @param apellido <code>String</code> apellido del pasajero a agregar
     * @param dni <code>int</code> que representa el numeor de DNI.
     * @param direccion <code>String</code> direccion del pasajero a agregar.
     */
    public void agregarPasajero(String nombre, String apellido, int dni, String direccion) {
        Pasajero pasajero = new Pasajero(nombre, apellido, dni, direccion);
        pasajeros.add(pasajero);
        pasajeroService.persistir(pasarListaDePasajerosAJSON());
    }

    /**
     * Metodo que cuenta el total de habitaciones.
     *
     * @return <code>int</code> que representa el numero total de habitaciones.
     */
    public int obtenerNroHabitaciones() {
        int total = 0;
        for (HabitacionStandard habitacion : habitacionesStandard.getListaHabitaciones()) {
            total += 1;
        }
        for (HabitacionSuite habitacion : habitacionesSuite.getListaHabitaciones()) {
            total += 1;
        }
        for (HabitacionPresidencial habitacion : habitacionesPresidenciales.getListaHabitaciones()) {
            total += 1;
        }

        return total;
    }

    /**
     * Metodo que cuenta el numero total de habitaciones segun el estado actual.
     * @param estadoHabitacion <code>Enum EstadoHabitacion</code> estado actual de la habitacion.
     * @return <code>int</code> numero total de habitaciones por estado.
     */
    public int obtenerNroHabitacionesSegunEstado(EstadoHabitacion estadoHabitacion) {
        int total = 0;

        for (HabitacionStandard habitacion : habitacionesStandard.getListaHabitaciones()) {
            if (habitacion.getEstado() == estadoHabitacion) {
                total += 1;
            }
        }
        for (HabitacionSuite habitacion : habitacionesSuite.getListaHabitaciones()) {
            if (habitacion.getEstado() == estadoHabitacion) {
                total += 1;
            }
        }
        for (HabitacionPresidencial habitacion : habitacionesPresidenciales.getListaHabitaciones()) {
            if (habitacion.getEstado() == estadoHabitacion) {
                total += 1;
            }
        }

        return total;
    }

    /**
     * Metodo que carga el Archivo JSON de pasajeros al sistema.
     *
     */

    public void cargarJSONPasajeros(){
        try {
            JSONArray pasajerosJson = new JSONArray(CreadorAJSON.downloadJSON(pasajeroService.getNombreArchivo()));

            for (int i = 0; i < pasajerosJson.length(); i++) {
                JSONObject jsonPasajero = pasajerosJson.getJSONObject(i);
                String nombre = jsonPasajero.getString("Nombre");
                String apellido = jsonPasajero.getString("Apellido");
                int dni = jsonPasajero.getInt("Dni");
                String domicilio = jsonPasajero.getString("Domicilio");

                try {
                    VerificacionesDeDatos.verificarDni(dni);
                    Pasajero pasajeroNew = new Pasajero(nombre, apellido, dni, domicilio);
                    pasajeros.add(pasajeroNew);
                } catch (BadDataException e) {
                    System.out.println("Error en persona" + nombre + " " + apellido + e.getMessage());
                }
            }

        } catch (IOException e) {
            try {
                CreadorAJSON.uploadJSON(pasajeroService.getNombreArchivo(), "[]");
            } catch (IOException ex) {
                System.out.println("Error en archivo pasajeros!");
            }
            System.out.println("Creando archivo...");
        }
    }

    /**
     * Metodo que trasforma la lista de pasajero en un <code>JSONArray</code>.
     * @return devuelve un <code>String</code> que es el JSONArray pasado a string.
     */
    public String pasarListaDePasajerosAJSON() {
        JSONArray arregloPasajeros = new JSONArray();
        for (Pasajero pasajero : pasajeros) {
            arregloPasajeros.put(pasajero.toJSON());
        }
        return arregloPasajeros.toString();
    }

    /**
     * Metodo que verifica su existe un usuario en el sistema.
     * @param username Nombre de usuario a buscar
     * @return <code>true</code> si encuentra al usuario, <code>false</code> de cualqueir otro modo.
     */

    public boolean existeUsuario(String username) {
        for (Empleado empleado : empleados) {
            if (empleado.getUsuario().equals(username)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Metodo que carga el Json de los empleado en el Sistema.
     * Si atrapa una excepcion de tipo <code>IOException</code> lanzado por el metodo <code>dowloadJSON</code> crear el archivo, con un arreglo vacio.
     *
     */

    public void cargarJSONEmpleados(){
        try {
            JSONArray empleadosJson = new JSONArray(CreadorAJSON.downloadJSON(empleadoService.getNombreArchivo()));
            for (int i = 0; i < empleadosJson.length(); i++) {
                JSONObject jsonEmpleados = empleadosJson.getJSONObject(i);
                String nombre = jsonEmpleados.getString("Nombre");
                String apellido = jsonEmpleados.getString("Apellido");
                int dni = jsonEmpleados.getInt("Dni");

                String usuario = jsonEmpleados.getString("Usuario");
                String email = jsonEmpleados.getString("Email");
                String clave = jsonEmpleados.getString("Clave");
                TipoEmpleado tipoEmpleado = jsonEmpleados.getEnum(TipoEmpleado.class, "TipoEmpleado");

                try {
                    VerificacionesDeDatos.verificarDni(dni);
                    Empleado empleado = new Empleado(nombre, apellido, dni, usuario, email, clave, tipoEmpleado);
                    empleados.add(empleado);
                } catch (BadDataException e) {
                    System.out.println("Error en persona" + nombre + " " + apellido + e.getMessage());
                }
            }

        } catch (IOException e) {
            try {
                CreadorAJSON.uploadJSON(empleadoService.getNombreArchivo(), "[]");
            } catch (IOException ex) {
                System.out.println("Error en archivo empleados!");
            }
            System.out.println("Creando archivo...");
        }
    }

    /**
     * Metodo que transforma la lista de empleados del sistema en <code>JSONArray</code>
     * @return devuelve un <code>JSONArray</code> convertido en String.
     */

    public String pasarListaDeEmpleadosAJSON() {
        JSONArray arregloEmpleados = new JSONArray();
        for (Empleado empleado : empleados) {
            arregloEmpleados.put(empleado.toJSON());
        }
        return arregloEmpleados.toString();
    }

    /**
     * Metodo que transforma todas las habitaciones, en JSON.
     *
     * @return devuelve un <code>String</code> que representa todos los tipos de habitaciones en formato Json.
     */

    public String todasLasHabitacionesAJSON() {
        JSONObject habitaciones = new JSONObject();

        habitaciones.put("habitacionesStandard", habitacionesStandard.habitacionesAJson());
        habitaciones.put("habitacionesSuite", habitacionesSuite.habitacionesAJson());
        habitaciones.put("habitacionesPresidenciales", habitacionesPresidenciales.habitacionesAJson());

        return habitaciones.toString();
    }

    /**
     * Metodo que carga la todas las Habitaciones desde el archivo JSON.
     */

    public void cargarJSONHabitaciones(){
        try {
            JSONObject habitacionesJson = new JSONObject(CreadorAJSON.downloadJSON(habitacionesService.getNombreArchivo()));

            cargarHabitacionesDesdeJSON(habitacionesJson.getJSONArray("habitacionesStandard"), HabitacionStandard.class);
            cargarHabitacionesDesdeJSON(habitacionesJson.getJSONArray("habitacionesSuite"), HabitacionSuite.class);
            cargarHabitacionesDesdeJSON(habitacionesJson.getJSONArray("habitacionesPresidenciales"), HabitacionPresidencial.class);

        } catch (IOException e) {
            System.out.println("Error al leer el archivo de habitaciones: " + e.getMessage());
        }
    }

    /**
     * Metodo Generico que permite cargar las habitaciones segun su tipo.
     * Itera sobre el <code>JSONArray</code> recivido por parametro.
     * Verifica que el tipo de la habitacion sea de alguno de los tipos ya definidos en el Sistema.
     * Crea una nueva habitacion del tipo encontrado.
     *
     * @param habitacionesArray un <code>JSONArray</code> de todas las habitaciones
     * @param tipoHabitacion Clase generica que representa el tipo de Habitacion
     * @param <T> Devuelve un Objeto generico de tipo T que extiende de Habitacion,
     */

    private <T extends Habitacion> void cargarHabitacionesDesdeJSON(JSONArray habitacionesArray, Class<T> tipoHabitacion) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (int i = 0; i < habitacionesArray.length(); i++) {
            JSONObject jsonHabitacion = habitacionesArray.getJSONObject(i);
            T nuevaHab = null;

            if (tipoHabitacion == HabitacionStandard.class) {
                nuevaHab = (T) new HabitacionStandard(jsonHabitacion.getInt("nroHabitacion"),
                        jsonHabitacion.getInt("capacidadMaxima"),
                        jsonHabitacion.getEnum(EstadoHabitacion.class, "estado"));
            } else if (tipoHabitacion == HabitacionSuite.class) {
                nuevaHab = (T) new HabitacionSuite(jsonHabitacion.getInt("nroHabitacion"),
                        jsonHabitacion.getInt("capacidadMaxima"),
                        jsonHabitacion.getEnum(EstadoHabitacion.class, "estado"),
                        LocalDate.parse(jsonHabitacion.getString("ultimaRevisionCocina"), formatter));
            } else if (tipoHabitacion == HabitacionPresidencial.class) {
                nuevaHab = (T) new HabitacionPresidencial(jsonHabitacion.getInt("nroHabitacion"),
                        jsonHabitacion.getInt("capacidadMaxima"),
                        jsonHabitacion.getEnum(EstadoHabitacion.class, "estado"),
                        LocalDate.parse(jsonHabitacion.getString("ultimaRevisionCocina"), formatter),
                        LocalDate.parse(jsonHabitacion.getString("ultimaRevisionJacuzzi"), formatter));
            }

            if (tipoHabitacion == HabitacionStandard.class) {
                habitacionesStandard.agregarHabitacion((HabitacionStandard) nuevaHab);
            } else if (tipoHabitacion == HabitacionSuite.class) {
                habitacionesSuite.agregarHabitacion((HabitacionSuite) nuevaHab);
            } else if (tipoHabitacion == HabitacionPresidencial.class) {
                habitacionesPresidenciales.agregarHabitacion((HabitacionPresidencial) nuevaHab);
            }
        }
    }

    /**
     * Metodo que intenta iniciar la sesion en le sistema.
     *
     * @param username <code>String</code>  el nombre de usuario de usuario a autenticar.
     * @param clave <code>String</code> password del usuario
     * @throws PersonaNoExisteException Lanza esta excepcion si las credenciales del empleado son <code>null</code>
     */

    public void intentarIniciarSesion(String username, String clave) throws PersonaNoExisteException {
        Empleado credencialesEmpleado = null;

        for (Empleado empleado : empleados) {
            if (empleado.getClave().equals(clave) && empleado.getUsuario().equals(username)) {
                credencialesEmpleado = empleado;
                break;
            }
        }

        if (credencialesEmpleado == null) {
            throw new PersonaNoExisteException("El usuario no existe!");
        }

        empleadoEnSistema = credencialesEmpleado;
    }

    /**
     * Cierra la sesion actual
     */

    public void logOut() {
        empleadoEnSistema = null;
    }

    /**
     * Metodo para obtener el empleado que esta actualemente conectado al sistema.
     *
     * @return Devuelve un Objeto de tipo <code>Empleado</code>
     */
    public Empleado obtenerEmpleadoLogueado() {
        return empleadoEnSistema;
    }

    /** Metodo para obtener el DNI del empleado actualente conectado al sistema.
     *
     * @return <code>int</code> que representa el numero de documento del usuario.
     */
    public int obtenerDniEmpleadoLogueado() {
        return empleadoEnSistema.getDni();
    }

    /**
     * Elimina un empleado mediante el id que recibe por parametro.
     *
     * @param dni recibe un <code>int</code> que representa el DNI del empleado a eliminar
     * @return <code>true</code> si lo pudo eliminar, <code>false</code> de otro modo.
     */

    public boolean eliminarEmpleadoPorElDni(int dni) {
        for (Empleado empleado : empleados) {
            if (empleado.getDni() == dni) {
                empleados.remove(empleado);
                empleadoService.persistir(pasarListaDeEmpleadosAJSON());
                return true;
            }
        }
        return false;
    }

    /**
     * Metodo que persiste todos los datos cargados en memoria.
     *
     */

    public void hacerBackup(){
        pasajeroService.persistir(pasarListaDePasajerosAJSON());
        empleadoService.persistir(pasarListaDeEmpleadosAJSON());
        habitacionesService.persistir(todasLasHabitacionesAJSON());
        reservaService.persistir(listaReservasToJson());
    }


    // Todo lo relativo a las reservas.

    /**
     * Busca reservas por dni.
     *
     * @param dni El dni del titular de la reserva
     * @return true, si se encontro al menos una reserva con el dni asociado, false en otro caso.
     */
    public boolean buscarReservaPorTitular(int dni) {
        boolean respuesta = false;

        for (Reserva reserva : reservas) {
            if (reserva.getDniTitular() == dni) {
                respuesta = true;
            }
        }
        return respuesta;
    }

    /**
     * Busca reservas activas por el dni del titular de la reservas.
     *
     * @param dni El dni del titular de la reserva
     * @return True si encuentra una reserva activa con el ese dni, false en cualquier otro caso.
     */

    public boolean buscarReservaActivaPorTitular(int dni) {
        boolean respuesta = false;
        for (Reserva reserva : reservas) {
            if (reserva.getDniTitular() == dni && reserva.isActiva()) {
                respuesta = true;
            }
        }
        return respuesta;
    }

    /**
     * Muestra un historico de reservas filtrado por el dni del titular
     *
     * @param dni El dni del titular de la reserva
     * @return Un String con la informacion de las reservas que tuvo el titular a lo largo del tiempo.
     */
    public String historicoPorTitular(int dni) {
        StringBuilder respuesta = new StringBuilder("Reserva - Fecha Inicio - Fecha Final - Habitacion").append("\n");

        for (Reserva reserva : reservas) {
            if (reserva.getDniTitular() == dni) {
                respuesta.append(reserva.getId())
                        .append(" - ")
                        .append(reserva.getFechaInicio())
                        .append(" - ")
                        .append(reserva.getFechaFinal())
                        .append(" - ")
                        .append(reserva.getHabitacion())
                        .append("\n");
            }
        }

        return respuesta.toString();
    }


    /**
     * Genera una reserva
     *
     * @param reserva Reserva completa.
     * @return respuesta, si se agrega o no.
     */
    public void generarReserva(Reserva reserva) {
        reservas.add(reserva);
        actualizarHabitacionesEnModificacionEnReserva();
        reservaService.persistir(listaReservasToJson());
    }


    /**
     * Metodo que transforma la lista de reservas en un JsonArray
     *
     * @return devuelve la lsta de reservas convertida en Json como string.
     */
    public String listaReservasToJson() {
        JSONArray reservasJson = new JSONArray();
        for (Reserva reserva : reservas) {
            reservasJson.put(reserva.toJson());
        }

        return reservasJson.toString();

    }

    /**
     * Metodo que retorna una reserva segun el ID pasado por parametro.
     *
     * @param id de la reserva.
     * @return devuelve un objecto de tipo <code>Reserva</code>.
     * @throws ReservaNoExisteException Lanza esta excepcion si no existe una reserva asociada al id.
     */

    public Reserva buscarReservaSegunId(int id) throws ReservaNoExisteException {
        Reserva busqueda = null;
        for (Reserva reserva : reservas) {
            if (reserva.getId() == id) {
                busqueda = reserva;
            }
        }


        if (busqueda == null) {
            throw new ReservaNoExisteException("La reserva no existe con ese id!");
        }

        return busqueda;
    }

    /**
     * Elimina una reserva mediante el id recibido por parametro.
     * @param id de la reserva a eliminar
     * @return <code>true</code> si puso ser eliminada, <code>false</code> de otra forma.
     */

    public boolean eliminarReserva(int id) {
        boolean respuesta = false;
        for (Reserva reserva : reservas) {
            if (reserva.getId() == id) {
                reservas.remove(reserva);
                reservaService.persistir(listaReservasToJson());
                respuesta = true;
                break;
            }
        }
        return respuesta;
    }


    /** Metodo que modifica el titular de una reserva.
     *
     * @param id de la reserva a modificar
     * @param dniTitular numero de documento del nuevo titular de la reserva
     * @return <code>true</code> si se pudo modificar, <code>false</code> de otra forma.
     * @throws ReservaNoExisteException Puede lanza resta excepcion cuando la reserva no existe.
     * @throws ReservaExisteException Lanza esta excepcion cuando el titular ya tiene una reserva activa.
     * @throws BadDataException Puede lanzar esta escepcion cuando no puede verificar el numero de documento.
     */
    public boolean modificarTitularReserva(int id, int dniTitular) throws ReservaNoExisteException, ReservaExisteException, BadDataException {
        boolean respuesta = false;
        VerificacionesDeDatos.verificarDni(dniTitular);

        Reserva reservaAModificar = buscarReservaSegunId(id);
        if (buscarReservaActivaPorTitular(dniTitular)) {
            throw new ReservaExisteException("Esa persona ya tiene una reserva guardada!");
        }
        reservaAModificar.setDniTitular(dniTitular);
        respuesta = true;
        return respuesta;
    }

    /**
     * Metodo que permite agregar mas pasajeros a una reserva.
     *
     * @param id de la reserva a modificar.
     * @param dniPasajero el numero de documento del pasajero.
     * @return <code>true</code> si el pasajero fue agregado con exito, <code>false</code> de otra forma.
     * @throws ReservaNoExisteException puede lanzar esta excepcion si el id no esta asociado a ninguna reserva.
     * @throws HabitacionNoEncontradaException puede lanzar esta excepcion si el numero de habitacion no se encuentra.
     * @throws MuchasPersonasException Lanza esta excepcion cuando se supera el limite de pasajeros permitidos.
     */

    public boolean agregarPasajeroAReserva(int id, int dniPasajero) throws ReservaNoExisteException, HabitacionNoEncontradaException, MuchasPersonasException {
        boolean respuesta = false;
        Reserva reservaAModificar = buscarReservaSegunId(id);
        Habitacion habitacionreserva = buscarHabitacionPorNumero(reservaAModificar.getHabitacion());

        if (habitacionreserva.getCapacidadMaxima() < reservaAModificar.getCantidadPersonasEnReserva() + 1) {
            throw new MuchasPersonasException("Las personas excederian la capacidad de la habitación");
        }

        reservaAModificar.agregarPersonaAReserva(dniPasajero);
        respuesta = true;
        return respuesta;
    }

    /**
     *
     * @param id de la reserva a modificar.
     * @param nroHabitacion nuevo numero de habitacion.
     * @return <code>true</code> si la reserva pudo ser modificada, <code>false</code> de otra forma.
     * @throws ReservaNoExisteException Puede lanzar esta excepcion si no se encuantra reserva asociada al id.
     */
    public boolean cambiarNroHabitacionDeReserva(int id, int nroHabitacion) throws ReservaNoExisteException {
        boolean respuesta = false;
        Reserva reservaAModificar = buscarReservaSegunId(id);
        reservaAModificar.setHabitacion(nroHabitacion);
        respuesta = true;
        return respuesta;
    }

    /**
     * Metodo que carga las reservas desde el archivo json.
     *
     *
     */

    public void cargarJSONReservas(){
        try {
            JSONArray jsonReservas = new JSONArray(CreadorAJSON.downloadJSON(reservaService.getNombreArchivo()));
            for (int i = 0; i < jsonReservas.length(); i++) {
                JSONObject jReserva = jsonReservas.getJSONObject(i);

                int dniTitular = jReserva.getInt("dniTitular");
                JSONArray pasajeros = jReserva.getJSONArray("pasajeros");


                int nroHabitacion = jReserva.getInt("habitacion");
                LocalDate fechaInicio = LocalDate.parse(jReserva.getString("fechaInicio"));
                LocalDate fechaFinal = LocalDate.parse(jReserva.getString("fechaFinal"));
                int guardadoPor = jReserva.getInt("guardadoPor");

                Reserva reserva = new Reserva(dniTitular, nroHabitacion, fechaInicio, fechaFinal, guardadoPor);

                if (VerificacionesDeDatos.reservaEsActiva(reserva)) {
                    reserva.setActiva(true);
                } else {
                    reserva.setActiva(false);
                }


                for (int cantidadpersonas = 0; cantidadpersonas < pasajeros.length(); cantidadpersonas++) {
                    int dnipersona = pasajeros.getInt(cantidadpersonas);
                    reserva.agregarPersonaAReserva(dnipersona);
                }

                if (VerificacionesDeDatos.reservaEstaPasandoAhora(reserva)) {
                    Habitacion habitacionguardada = null;

                    try {
                        habitacionguardada = buscarHabitacionPorNumero(reserva.getHabitacion());
                        habitacionguardada.setEstado(EstadoHabitacion.OCUPADA);
                        for (int p = 0; p < pasajeros.length(); p++) {
                            int dnipersona = pasajeros.getInt(p);
                            habitacionguardada.agregarPersonaAHabitacion(dnipersona);
                        }
                    } catch (HabitacionNoEncontradaException e) {
                        System.out.println("Error en busqueda de habitacion en carga de datos [RESERVA]");
                    }
                }

                reservas.add(reserva);
            }

        } catch (IOException ex) {
            reservaService.persistir("[]");
            System.out.println("Creando Archivo....");
        }
    }

    /** Metodo que muestra las habitaciones disponibles.
     *
     * @param intento un objeto de tipo <code>Reserva</code>.
     * @return Una lista de los numeros de todas las habitaciones disponibles
     * @throws HabitacionNoEncontradaException Puede lanzar esta excepcion si no encuetra una habitacion.
     */

    public ArrayList<Integer> verHabitacionesDisponibles(Reserva intento) throws HabitacionNoEncontradaException {
        ArrayList<Integer> numerosdehabitacionesdisp = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            try {
                Habitaciones habitaciones = selectorDeTipoHabitacion(i);
                ArrayList<Habitacion> habitacionesencollection = habitaciones.retornarLista();

                for (int j = 0; j < habitacionesencollection.size(); j++) {
                    if (habitacionesencollection.get(j).getCapacidadMaxima() >= intento.getCantidadPersonasEnReserva() && habitacionesencollection.get(j).getEstado() != EstadoHabitacion.MANTENIMIENTO) {
                        int numerodehabitacion = habitacionesencollection.get(j).getNroHabitacion();

                        if (verSiHabitacionEstaDisponible(intento, numerodehabitacion)) {
                            numerosdehabitacionesdisp.add(numerodehabitacion);
                        }
                    }
                }
            } catch (BadOptionException e) {
                System.out.println("Error!!!!");
            }
        }

        if (numerosdehabitacionesdisp.isEmpty()) {
            throw new HabitacionNoEncontradaException("No hay habitaciones disponibles segun requisitos. Saliendo...");
        }

        return numerosdehabitacionesdisp;
    }

    /**
     * Metodo que chequea que una habitacion este disponible.
     *
     * @param intento un objeto de tipo <code>Reserva</code> que es el intento de reserva.
     * @param numhabitacion el numero de la habitacion a reservar
     * @return <code>true</code> si esta disponible, <code>false</code> si no lo esta.
     */

    public boolean verSiHabitacionEstaDisponible(Reserva intento, int numhabitacion) {
        boolean disponible = true;
        for (Reserva reserva : reservas) {
            if (numhabitacion == reserva.getHabitacion() && reserva.isActiva()) {
                if (VerificacionesDeDatos.intentoReservaEstaDentroDeGuardada(intento, reserva) || VerificacionesDeDatos.intentoReservaContieneUnaReservaGuardada(intento, reserva) || VerificacionesDeDatos.intentoReservaInterseccionaConUnaGuardada(intento, reserva)) {
                    disponible = false;
                    break;
                }
            }
        }
        return disponible;
    }

    /**
     * Metodo que retorna los datos de una habitacion segun su numero.
     *
     * @param numeroHabitacion el numero de la habitacion a buscar.
     * @return un <code>String</code> con toda la info de la habitacion.
     */

    public String traerDatosDeHabitacionSegunNum(int numeroHabitacion) {
        String info = "";
        try {
            info = info.concat(buscarHabitacionPorNumero(numeroHabitacion).toString());
        } catch (HabitacionNoEncontradaException e) {
            info = info.concat(e.getMessage());
        }
        return info;
    }

    /**
     * Metodo que retrona una habitacion segun su numero.
     *
     * @param numeroHabitacion el numero de la habitacion a buscar.
     * @return un objeto de tipo <code>Habitacion</code> asociado al numero recivid por parametro.
     * @throws HabitacionNoEncontradaException puede lanzar esta excepcion si la habitacion no se encuentra.
     */
    public Habitacion buscarHabitacionPorNumero(int numeroHabitacion) throws HabitacionNoEncontradaException {
        Habitacion prueba = null;
        for (int i = 1; i <= 3; i++) { // 3 es el nro de tipos de habitacion que existen
            try {
                prueba = selectorDeTipoHabitacion(i).traerHabitacionSegunId(numeroHabitacion);
                if (prueba != null) {
                    break;
                }
            } catch (BadOptionException e) {
                System.out.println("Error en busqueda!");
            }
        }

        if (prueba == null) {
            throw new HabitacionNoEncontradaException("No existe esa habitacion con ese numero");
        }

        return prueba;
    }

    /** Metodo que limpia las personas de las habitaciones de las 3 listas.
     *  Inserta las personas de nuevo en caso de que una reserva siga vigente.

     */

    public void actualizarHabitacionesEnModificacionEnReserva() {
        for (int i = 1; i <= 3; i++) {
            try {
                Habitaciones habitaciones = selectorDeTipoHabitacion(i);
                habitaciones.limpiarPersonasDeHabitaciones();
            } catch (BadOptionException e) {
                System.out.println("Error!!!!");
            }
        }

        for (Reserva reserva : reservas) {
            if (VerificacionesDeDatos.reservaEstaPasandoAhora(reserva)) {
                Habitacion habitacionguardada = null;

                try {
                    habitacionguardada = buscarHabitacionPorNumero(reserva.getHabitacion());
                    habitacionguardada.setEstado(EstadoHabitacion.OCUPADA);
                    for (Integer dnipersona : reserva.getPasajeros()) {
                        habitacionguardada.agregarPersonaAHabitacion(dnipersona);
                    }
                } catch (HabitacionNoEncontradaException e) {
                    System.out.println("Error en busqueda de habitacion en carga de datos [RESERVA]");
                }
            }
        }
    }
}

