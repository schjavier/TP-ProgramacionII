import DataChecks.VerificacionesDeDatos;
import Exceptions.*;
import JSONCreator.CreadorAJSON;
import Modelo.Habitaciones.*;
import Modelo.Persona.Empleado;
import Modelo.Persona.Pasajero;
import Modelo.Persona.Persona;
import Modelo.Persona.TipoEmpleado;
import Modelo.Reserva.Reserva;
import Modelo.Reserva.ReservaService;
import Persistencia.InterfacePersistencia;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Hotel implements InterfacePersistencia {

    private String nombre;
    private Empleado empleadoEnSistema = null;

    private final HabitacionesStandard habitacionesStandard = new HabitacionesStandard();
    private final HabitacionesSuite habitacionesSuite = new HabitacionesSuite();
    private final HabitacionesPresidenciales habitacionesPresidenciales = new HabitacionesPresidenciales();

    private final ArrayList<Pasajero> pasajeros = new ArrayList<>();
    private final ArrayList<Empleado> empleados = new ArrayList<>();

    private final ArrayList<Reserva> reservas = new ArrayList<>();
    private final ReservaService reservaService = new ReservaService();

    private final String archivoPasajeros = "Pasajeros.json";
    private final String archivoEmpleados = "Empleados.json";
    private final String archivoHabitaciones = "Habitaciones.json";


    public Hotel(String nombre) {
        this.nombre = nombre;

        try {
            cargarJSONPasajeros();
            cargarJSONEmpleados();
            cargarJSONHabitaciones();
            cargarJSONReservas();
        } catch (NullNameException e) {
            System.out.println("Nombre nulo.");
        }
    }

    /**
     * Devuelve el nombre asignado al hotel
     * @return un String que representa el nombre del hotel
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Permite crear muchas habitaciones a la vez. Se crean todas como DISPONIBLE.
     * @param tipoHabitacion Un numero del 1 al x siendo x el ultimo tipo de habitacion que haya
     */
    public void crearHabitaciones(int cantidadHabitaciones, int capacidadMaxima, int tipoHabitacion) throws BadOptionException{
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

        guardarHabitaciones();
    }

    public void crearEmpleado(String nombre, String apellido, int dni, String usuario, String email, String clave, TipoEmpleado tipoEmpleado) throws NullNameException {
        empleados.add(new Empleado(nombre,apellido,dni,usuario,email,clave,tipoEmpleado));
        guardarEmpleados();
    }

    public String contarEstadoHabitaciones(int tipohabitacion) {
        String resp = "";
        try {
            resp = selectorDeTipoHabitacion(tipohabitacion).contarCantidadHabitacionesSegunEstado();
        } catch (BadOptionException e) {
            resp = e.getMessage();
        }
        return resp;
    }

    public StringBuilder listarHabitaciones() {
        return habitacionesStandard.listarHabitaciones().append(habitacionesSuite.listarHabitaciones()
                .append(habitacionesPresidenciales.listarHabitaciones()));
    }

    public StringBuilder listarHabitacionesSegunTipo(int tipohabitacion) {
        StringBuilder todos = new StringBuilder();
        try {
            todos = selectorDeTipoHabitacion(tipohabitacion).listarHabitaciones();
        } catch (BadOptionException e) {
            todos.append(e.getMessage());
        }
        return todos;
    }

    //Que elimine habitaciones solo con el numero
    public boolean eliminarHabitacion(int habitacion) {
        if (habitacionesStandard.eliminarHabitacionSegunNumero(habitacion)) {
            return true;
        } else if (habitacionesSuite.eliminarHabitacionSegunNumero(habitacion)) {
            return true;
        } else if (habitacionesPresidenciales.eliminarHabitacionSegunNumero(habitacion)){
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

    public void revisarCocinasHabitaciones() throws NullNameException {
        habitacionesSuite.marcarTodasHabitacionesComoRevisadas();
        habitacionesPresidenciales.marcarTodasHabitacionesComoRevisadas();
        guardarHabitaciones();
    }

    public void revisarJacuzzisHabitaciones() throws NullNameException {
        habitacionesPresidenciales.marcarTodasHabitacionesComoRevisadasJacuzzi();
        guardarHabitaciones();
    }

    public StringBuilder listarSegunEstado(int tipohabitacion, EstadoHabitacion estado) {
        StringBuilder todos = new StringBuilder();
        try {
            todos = selectorDeTipoHabitacion(tipohabitacion).listarHabitacionesSegunEstado(estado);
        } catch (BadOptionException e) {
            todos.append(e.getMessage());
        }
        return todos;
    }

    public int obtenerCantidadEmpleados() {
        return empleados.size();
    }

    public StringBuilder verEmpleados() {
        StringBuilder resultado = new StringBuilder("Lista de empleados: \n\n");

        for (Empleado empleado: empleados) {
            resultado.append(empleado.toString());
            resultado.append("\n");
        }

        return resultado;
    }

    public ArrayList<Pasajero> obtenerPasajeros() {
        return pasajeros;
    }

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

    public Persona buscarPersonaPorDni(int dni) throws PersonaNoExisteException,BadDataException {
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


    public void verSiElDniEstaCargado(int dni) throws PersonaExisteException, BadDataException {
        VerificacionesDeDatos.verificarDni(dni);

        if (existeEmpleadoConEseDNI(dni) || existePasajeroConEseDNI(dni)) {
            throw new PersonaExisteException("Hay alguien con el dni " + dni);
        }
    }


    public void agregarPasajero(String nombre, String apellido, int dni, String direccion) {
        Pasajero pasajero = new Pasajero(nombre, apellido, dni, direccion);
        pasajeros.add(pasajero);
        try {
            guardarPasajeros();
        } catch (IOException e) {
            System.out.println("Error en archivo!");
        }
    }

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

    public void cargarJSONPasajeros() throws NullNameException {
        try {
            JSONArray pasajerosJson = new JSONArray(CreadorAJSON.downloadJSON(archivoPasajeros));

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
                CreadorAJSON.uploadJSON(archivoPasajeros, "[]");
            } catch (IOException ex) {
                System.out.println("Error en archivo pasajeros!");
            }
            System.out.println("Creando archivo...");
        }
    }

    public void guardarPasajeros() throws NullNameException {
        String arregloPasajeros = pasarListaDePasajerosAJSON();
        CreadorAJSON.uploadJSON(archivoPasajeros,arregloPasajeros);
    }

    public String pasarListaDePasajerosAJSON() {
        JSONArray arregloPasajeros = new JSONArray();
        for (Pasajero pasajero : pasajeros) {
            arregloPasajeros.put(pasajero.toJSON());
        }
        return arregloPasajeros.toString();
    }

    public boolean existeUsuario(String username) {
        for (Empleado empleado : empleados) {
            if (empleado.getUsuario().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public void cargarJSONEmpleados() throws NullNameException {
        try {
            JSONArray empleadosJson = new JSONArray(CreadorAJSON.downloadJSON(archivoEmpleados));
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
                CreadorAJSON.uploadJSON(archivoEmpleados, "[]");
            } catch (IOException ex) {
                System.out.println("Error en archivo empleados!");
            }
            System.out.println("Creando archivo...");
        }
    }

    public String pasarListaDeEmpleadosAJSON() {
        JSONArray arregloEmpleados = new JSONArray();
        for (Empleado empleado : empleados) {
            arregloEmpleados.put(empleado.toJSON());
        }
        return arregloEmpleados.toString();
    }

    public void guardarEmpleados() throws NullNameException {
        String arregloEmpleados = pasarListaDeEmpleadosAJSON();
        CreadorAJSON.uploadJSON(archivoEmpleados,arregloEmpleados);
    }

    public JSONObject todasLasHabitacionesAJson() {
        JSONObject habitaciones = new JSONObject();

        habitaciones.put("habitacionesStandard",habitacionesStandard.habitacionesAJson());
        habitaciones.put("habitacionesSuite",habitacionesSuite.habitacionesAJson());
        habitaciones.put("habitacionesPresidenciales",habitacionesPresidenciales.habitacionesAJson());

        return habitaciones;
    }

    public void guardarHabitaciones() throws NullNameException {
        String arregloHabitaciones = todasLasHabitacionesAJson().toString();
        CreadorAJSON.uploadJSON(archivoHabitaciones,arregloHabitaciones);
    }

    public void cargarJSONHabitaciones() throws NullNameException {
        try {
            JSONObject habitacionesJson = new JSONObject(CreadorAJSON.downloadJSON(archivoHabitaciones));

            cargarHabitacionesDesdeJSON(habitacionesJson.getJSONArray("habitacionesStandard"), HabitacionStandard.class);
            cargarHabitacionesDesdeJSON(habitacionesJson.getJSONArray("habitacionesSuite"), HabitacionSuite.class);
            cargarHabitacionesDesdeJSON(habitacionesJson.getJSONArray("habitacionesPresidenciales"), HabitacionPresidencial.class);

        } catch (IOException e) {
            System.out.println("Error al leer el archivo de habitaciones: " + e.getMessage());
        }
    }

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

    public void logOut() {
        empleadoEnSistema = null;
    }

    public TipoEmpleado buscarPermisoDeUsuario() {
        return empleadoEnSistema.getTipo();
    }

    public Empleado obtenerEmpleadoLogueado() {
        return empleadoEnSistema;
    }

    public int obtenerDniEmpleadoLogueado() {
        return empleadoEnSistema.getDni();
    }

    public boolean eliminarEmpleadoPorElDni(int dni) throws NullNameException {
        for (Empleado empleado : empleados) {
            if (empleado.getDni() == dni) {
                empleados.remove(empleado);
                guardarEmpleados();
                return true;
            }
        }
        return false;
    }

    public void hacerBackup() throws NullNameException {
        guardarPasajeros();
        guardarEmpleados();
        guardarHabitaciones();
    }


    // Todo lo relativo a las reservas.

    /**
     *
     * Busca reservas por dni.
     *
     * @param dni El dni del titular de la reserva
     * @return true, si se encontro al menos una reserva con el dni asociado, false en otro caso.
     */
    public boolean buscarReservaPorTitular(int dni){
        boolean respuesta = false;

        for (Reserva reserva : reservas){
            if (reserva.getDniTitular() == dni){
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

    public boolean buscarReservaActivaPorTitular(int dni){
        boolean respuesta = false;
        for (Reserva reserva : reservas){
            if (reserva.getDniTitular() == dni && reserva.isActiva()){
                respuesta = true;
            }
        }
        return respuesta;
    }

    /**
     *
     * Muestra un historico de reservas filtrado por el dni del titular
     *
     * @param dni El dni del titular de la reserva
     * @return Un String con la informacion de las reservas que tuvo el titular a lo largo del tiempo.
     */
    public String historicoPorTitular(int dni){
        StringBuilder respuesta = new StringBuilder("Reserva - Fecha Inicio - Fecha Final - Habitacion").append("\n");

        for (Reserva reserva : reservas){
            if (reserva.getDniTitular() == dni){
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
     * Genera uan reserva
     * @param reserva Reserva completa.
     * @return respuesta, si se agrega o no.
     */
    public boolean generarReserva(Reserva reserva/*int dniTitular, int habitacion, LocalDate fechaInicio, LocalDate fechaFinal, int guardadoPor*/){
        boolean respuesta = false;
        boolean isValid = false;
        /*Reserva reserva = new Reserva(dniTitular, habitacion, fechaInicio, fechaFinal, guardadoPor);*/
    try {
        isValid = validarReserva(reserva);
    } catch (ReservaExisteException ex) {
        System.out.println(ex.getMessage());
    }
    if (isValid){
        reservas.add(reserva);
        respuesta = true;
        persistir(listaReservasToJson(reservas));
    }
        return respuesta;
    }


    /**
     * Metodo que transforma la lista de reservas en un JsonArray
     *
     * @param reservas La lista de reservas
     * @return devuelve la lsta de reservas convertida en Json como string.
     */
    public String listaReservasToJson(ArrayList<Reserva> reservas){

        JSONArray reservasJson = new JSONArray(reservas);
        for (Reserva reserva : reservas)
        {
            reservasJson.put(reserva.toJson());
        }

        return reservasJson.toString();

    }

    /**
     *
     * @param idReserva entero que representa el id de la reserva a buscar.
     * @return -1 si no encontro la reserva, si no devuelve el indice de la reserva buscada
     */

    public int buscarReservaPorId(int idReserva) {
        int index = -1;

        for (Reserva reserva : reservas){
            if (reserva.getId() == idReserva){
                index = reservas.indexOf(reserva);
            }

        }
        return index;
    }
// Servicio
    @Override
    public boolean persistir(String contenido) {
        return reservaService.persistir(contenido);
    }

    public boolean eliminarReserva(int id) {
        boolean respuesta = false;
        for (Reserva reserva : reservas){
            if (reserva.getId() == id){
                reservas.remove(reserva);
                persistir(listaReservasToJson(reservas));
                respuesta = true;
            }
        }
        return respuesta;
    }

    public boolean modificarTitularReserva(int id, int dniTitular) {
        boolean respuesta = false;
        if (buscarReservaPorId(id) != -1){
            Reserva reservaAModificar = reservas.get(buscarReservaPorId(id));
            reservaAModificar.setDniTitular(dniTitular);
            respuesta = true;
        }
        return respuesta;
    }

    public boolean agregarPasajeroAReserva(int id, int dniPasajero) {
        boolean respuesta = false;
        if (buscarReservaPorId(id) != -1){
            Reserva reservaAModificar = reservas.get(buscarReservaPorId(id));
            reservaAModificar.getPasajeros().add(dniPasajero);
            respuesta = true;
        }
        return respuesta;
    }


    public boolean cambiarFechaInicio(int id, LocalDate fechaInicio) {
        boolean respuesta = false;
        if (buscarReservaPorId(id) != -1){
            Reserva reservaAModificar = reservas.get(buscarReservaPorId(id));
            reservaAModificar.setFechaInicio(fechaInicio);
            respuesta = true;
        }

        return respuesta;
    }

    public boolean cambiarFechaFinal(int id, LocalDate fechaFinal) {
        boolean respuesta = false;
        if (buscarReservaPorId(id) != -1) {
            Reserva reservaAModificar = reservas.get(buscarReservaPorId(id));
            reservaAModificar.setFechaFinal(fechaFinal);
            respuesta = true;

        }
        return respuesta;
    }

    public boolean cambiarNroHabitacion(int id, int nroHabitacion) {
        boolean respuesta = false;
            if (buscarReservaPorId(id) != -1){
                Reserva reservaAModificar = reservas.get(buscarReservaPorId(id));
                reservaAModificar.setHabitacion(nroHabitacion);
                respuesta = true;
            }
        return respuesta;
    }

    /**
     * Valida la reserva
     *
     * @param reserva
     * @return true si la reserva es valida, false de otro modo.
     * @throws ReservaExisteException
     */
    public boolean validarReserva(Reserva reserva) throws ReservaExisteException {
        boolean respuesta = false;

        if (buscarReservaActivaPorTitular(reserva.getDniTitular())){
            throw new ReservaExisteException("Ya hay una reserva registrada con ese titular");
        } else {
            respuesta = true;
        }

        return respuesta;

    }

    /*NO SE SI ESTE METODO SIRVE MAS...*/
    /*
    public boolean verificarFecha(Reserva reservaNueva){
        boolean respuesta = false;
        for (Reserva reserva : reservas){
            if (VerificacionesDeDatos.intentoReservaEstaDentroDeGuardada(reservaNueva, reserva) || VerificacionesDeDatos.intentoReservaContieneUnaReservaGuardada(reservaNueva, reserva) || VerificacionesDeDatos.intentoReservaInterseccionaConUnaGuardada(reservaNueva, reserva)){
                respuesta = true;
            }
        }
        return respuesta;
    }*/

    public void cargarJSONReservas() throws NullNameException{
        try {
            JSONArray jsonReservas = new JSONArray(CreadorAJSON.downloadJSON(reservaService.getNombreArchivo()));
            for (int i = 0 ; i < jsonReservas.length(); i++){
                JSONObject jReserva = jsonReservas.getJSONObject(i);

                // creo que no es necesaria, la dejo por las dudas
                int id = jReserva.getInt("id");

                int dniTitular = jReserva.getInt("dniTitular");

                // creo que no es necesaria, la dejo por las dudas
                boolean activa = jReserva.getBoolean("activa");

                int nroHabitacion = jReserva.getInt("habitacion");
                LocalDate fechaInicio = LocalDate.parse(jReserva.getString("fechaInicio"));
                LocalDate fechaFinal = LocalDate.parse(jReserva.getString("fechaFinal"));
                int guardadoPor = jReserva.getInt("guardadoPor");

                Reserva reserva = new Reserva(dniTitular, nroHabitacion, fechaInicio, fechaFinal, guardadoPor);
                reservas.add(reserva);
            }

        } catch (IOException ex) {
            reservaService.persistir("[]");
            System.out.println("Creando Archivo....");
        }
    }

    public String mostrarReservaPorId(int id) throws ReservaNoExisteException{
        int index = buscarReservaPorId(id);
        StringBuilder reserva =  new StringBuilder();
        if (index != -1){
            reserva.append(reservas.get(index).toString());
        } else {
            throw new ReservaNoExisteException("La reserva no existe!");
        }

        return reserva.toString();
    }

    public ArrayList<Integer> verHabitacionesDisponibles(Reserva intento) throws HabitacionNoEncontradaException
    {
        ArrayList<Integer> numerosdehabitacionesdisp = new ArrayList<>();
        for(int i = 1; i <= TipoHabitacion.values().length;i++)
        {
            try {
                Habitaciones habitaciones = selectorDeTipoHabitacion(i);
                ArrayList<Habitacion> habitacionesencollection = habitaciones.retornarLista();

                for(int j = 0; j < habitacionesencollection.size();i++)
                {
                    if(habitacionesencollection.get(j).getCapacidadMaxima() >= intento.getCantidadPersonasEnReserva())
                    {
                        int numerodehabitacion = habitacionesencollection.get(j).getNroHabitacion();

                        if(verSiHabitacionEstaDisponible(intento,numerodehabitacion))
                        {
                            numerosdehabitacionesdisp.add(numerodehabitacion);
                        }
                    }
                }
            } catch (BadOptionException e) {
                System.out.println("Error!!!!");
            }
        }

        if(numerosdehabitacionesdisp.isEmpty())
        {
            throw new HabitacionNoEncontradaException("No hay habitaciones disponibles segun requisitos. Saliendo...");
        }

        return numerosdehabitacionesdisp;
    }

    public boolean verSiHabitacionEstaDisponible(Reserva intento, int numhabitacion)
    {
        boolean disponible = true;
        for(Reserva reserva : reservas) // tendriamos que hacer reservas activas?
        {
            if(numhabitacion == reserva.getHabitacion())
            {
                if (VerificacionesDeDatos.intentoReservaEstaDentroDeGuardada(intento, reserva) || VerificacionesDeDatos.intentoReservaContieneUnaReservaGuardada(intento, reserva) || VerificacionesDeDatos.intentoReservaInterseccionaConUnaGuardada(intento, reserva)){
                    disponible = false;
                    break;
                }
            }
        }
        return disponible;
    }


}

