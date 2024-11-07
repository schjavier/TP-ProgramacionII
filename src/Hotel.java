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
import Persistencia.InterfacePersistecia;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Hotel implements InterfacePersistecia {
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


    public Hotel(String nombre) throws NullNameException {
        this.nombre = nombre;

        try {
            cargarJSONPasajeros();
            cargarJSONEmpleados();
            cargarJSONHabitaciones();
        } catch (NullNameException e) {
            throw new NullNameException(e.getMessage());
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
    public void crearHabitaciones(int cantidadHabitaciones, int capacidadMaxima, int tipoHabitacion) throws BadOptionException, NullNameException {
        Habitaciones habitaciones = selectorDeTipoHabitacion(tipoHabitacion);
        for (int i = 0; i < cantidadHabitaciones; i++) {
            switch (tipoHabitacion) {
                case 1 -> habitaciones.agregarHabitacion(new HabitacionStandard(capacidadMaxima));
                case 2 -> habitaciones.agregarHabitacion(new HabitacionSuite(capacidadMaxima));
                case 3 -> habitaciones.agregarHabitacion(new HabitacionPresidencial(capacidadMaxima));
                default -> throw new BadOptionException("Elegir una opcion correcta!!");
            }
        }

        guardarHabitaciones();
    }

    public void crearEmpleado(String nombre, String apellido, int dni, String usuario, String email, String clave, TipoEmpleado tipoEmpleado) throws NullNameException {
        empleados.add(new Empleado(nombre,apellido,dni,usuario,email,clave,tipoEmpleado));
        guardarEmpleados();
    }

    public String contarEstadoHabitaciones(int tipoHabitacion) throws BadOptionException {
        return selectorDeTipoHabitacion(tipoHabitacion).contarCantidadHabitacionesSegunEstado();
    }

    public StringBuilder listarHabitaciones() {
        return habitacionesStandard.listarHabitaciones().append(habitacionesSuite.listarHabitaciones()
                .append(habitacionesPresidenciales.listarHabitaciones()));
    }

    public StringBuilder listarHabitacionesSegunTipo(int tipoHabitacion) throws BadOptionException {
        return selectorDeTipoHabitacion(tipoHabitacion).listarHabitaciones();
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

    public StringBuilder listarSegunEstado(int tipoHabitacion, EstadoHabitacion estado) throws BadOptionException {
        return selectorDeTipoHabitacion(tipoHabitacion).listarHabitacionesSegunEstado(estado);
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

    public Pasajero buscarPasajeroConEseDNI(int dni) throws PersonaNoExisteException, BadDataException{// para mostrarDatos de una o mas personas (puede ser de las reservas no?)
        VerificacionesDeDatos.verificarDni(dni);
        Pasajero persona = null;
        for (Pasajero pasajero : pasajeros) {
            if (pasajero.getDni() == dni) {
                persona = pasajero;
                break;
            }
        }

        if (persona == null) {
            throw new PersonaNoExisteException("Persona con el DNI: " + dni + " no existe, por favor cargar persona con ese dni de nuevo.");
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


    public void agregarPasajero(String nombre, String apellido, int dni, String direccion) throws NullNameException {
        Pasajero pasajero = new Pasajero(nombre, apellido, dni, direccion);
        pasajeros.add(pasajero);
        guardarPasajeros();
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
            CreadorAJSON.uploadJSON(archivoPasajeros, "[]");
            System.out.println("Creando archivo...");
        } catch (NullNameException e) {
            throw new NullNameException(e.getMessage());
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
            CreadorAJSON.uploadJSON(archivoEmpleados, "[]");
            System.out.println("Creando archivo...");
        } catch (NullNameException e) {
            throw new NullNameException(e.getMessage());
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
     *
     * Genera una reserva nueva y la guarda en la lista de reservas.
     *
     * @param dniTitular int representando el dni del titular de la reserva
     * @param habitacion numero de la habitacion de la reserva
     * @param fechaInicio LocalDateTime para la fecha de inicio de la reserva
     * @param fechaFinal LocalDateTime para el final de la reserva
     * @param guardadoPor dni del empleado que guarda la reserva.
     *
     * @return Devuelve True si la reserva pudo generarse, False de otro modo.
     */

    public boolean generarReserva(int dniTitular, int habitacion, LocalDate fechaInicio, LocalDate fechaFinal, int guardadoPor){
        boolean respuesta = false;
        Reserva reserva = new Reserva(dniTitular, habitacion, fechaInicio, fechaFinal, guardadoPor);


        if (reservas.isEmpty()){
            reservas.add(reserva);
            respuesta = true;
        } else if (!buscarReservaActivaPorTitular(dniTitular)){
            reservas.add(reserva);
            respuesta = true;
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

    private boolean buscarReservaPorId(int idReserva) {
        boolean respuesta = false;

        for (Reserva reserva : reservas){
            if (reserva.getId() == idReserva){
                respuesta = true;
            }

        }
        return respuesta;
    }
// Servicio
    @Override
    public boolean persistir(String contenido) {
        return reservaService.persistir(contenido);
    }

    public boolean eliminarReserva(int id) {
        return false;
    }

    public boolean modificarReserva(int id) {
        return false;
    }
}
