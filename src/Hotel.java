import DataChecks.VerificacionesDeDatos;
import Exceptions.*;
import JSONCreator.CreadorAJSON;
import Modelo.Habitaciones.*;
import Modelo.Persona.Empleado;
import Modelo.Persona.Pasajero;
import Modelo.Persona.TipoEmpleado;
import Modelo.Reserva.Reserva;
import Modelo.Reserva.ReservaService;
import Persistencia.InterfacePersistencia;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

public class Hotel implements InterfacePersistencia {

    private String nombre;
    private Empleado empleado_en_sistema = null;

    private final HabitacionesStandard habitacionesStandard = new HabitacionesStandard();
    private final HabitacionesSuite habitacionesSuite = new HabitacionesSuite();
    private final HabitacionesPresidenciales habitacionesPresidenciales = new HabitacionesPresidenciales();

    private final ArrayList<Pasajero> pasajeros = new ArrayList<>();
    private final ArrayList<Empleado> empleados = new ArrayList<>();

    private final ArrayList<Reserva> reservas = new ArrayList<>();
    private final ReservaService reservaService = new ReservaService();

    private final String archivopasajeros = "Pasajeros.json";
    private final String archivoempleados = "Empleados.json";


    public Hotel(String nombre) {
        this.nombre = nombre;

        try {
            cargarJSONPasajeros();
            cargarJSONEmpleados();
            cargarJSONReservas();


        } catch (NullNameException e) {
            throw new NullNameException(e.getMessage());
        }
    }

    /**
     * Permite crear muchas habitaciones a la vez. Se crean todas como DISPONIBLE.
     * @param tipoHabitacion Un numero del 1 al x siendo x el ultimo tipo de habitacion que haya
     */
    public void crearHabitaciones(int cantidadHabitaciones, int capacidadMaxima, int tipoHabitacion) {
        Habitaciones habitaciones = selectorDeTipoHabitacion(tipoHabitacion);
        for (int i = 0; i < cantidadHabitaciones; i++) {
            switch (tipoHabitacion) {
                case 1 -> habitaciones.agregarHabitacion(new HabitacionStandard(capacidadMaxima));
                case 2 -> habitaciones.agregarHabitacion(new HabitacionSuite(capacidadMaxima));
                case 3 -> habitaciones.agregarHabitacion(new HabitacionPresidencial(capacidadMaxima));
                default -> throw new BadOptionException("Elegir una opcion correcta!!");
            }
        }
    }

    public String contarEstadoHabitaciones(int tipohabitacion) {
        return selectorDeTipoHabitacion(tipohabitacion).contarCantidadHabitacionesSegunEstado();
    }

    public StringBuilder listarHabitaciones() {
        return habitacionesStandard.listarHabitaciones().append(habitacionesSuite.listarHabitaciones()
                .append(habitacionesPresidenciales.listarHabitaciones()));
    }

    public StringBuilder listarHabitacionesSegunTipo(int tipohabitacion) {
        return selectorDeTipoHabitacion(tipohabitacion).listarHabitaciones();
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
     * @param tipohabitacion Un numero del 1 al x siendo x el ultimo tipo de habitacion que haya
     * @return La lista que en la que se trabajara
     * @throws BadOptionException En el caso de que el id de tipo habitacion (lista) no este dentro del switch
     */
    public Habitaciones selectorDeTipoHabitacion(int tipohabitacion) throws BadOptionException {
        return switch (tipohabitacion) {
            case 1 -> habitacionesStandard;
            case 2 -> habitacionesSuite;
            case 3 -> habitacionesPresidenciales;
            default -> throw new BadOptionException("Elegir una opcion correcta!!");
        };
    }

    public StringBuilder listarSegunEstado(int tipohabitacion, EstadoHabitacion estado) {
        return selectorDeTipoHabitacion(tipohabitacion).listarHabitacionesSegunEstado(estado);
    }

    public boolean existePasajeroConEseDNI(int dni) throws BadDataException // para hacer reservas o alguna otra cosa
    {
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

    public Pasajero buscarPasajeroConEseDNI(int dni) throws PersonaNoExisteException // para mostrarDatos de una o mas personas (puede ser de las reservas no?)
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
            throw new PersonaNoExisteException("Persona con el DNI: " + dni + " no existe, por favor cargar persona con ese dni de nuevo.");
        }

        return persona;
    }

    public boolean existeEmpleadoConEseDNI(int dni) {
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


    public void verSiElDniEstaCargardo(int dni) throws PersonaExisteException, BadDataException {
        VerificacionesDeDatos.verificarDni(dni);

        if (existeEmpleadoConEseDNI(dni) || existePasajeroConEseDNI(dni)) {
            throw new PersonaExisteException("Hay alguien con el dni " + dni);
        }
    }


    public void agregarPasajero(String nombre, String apellido, int dni, String direccion) {
        Pasajero pasajero = new Pasajero(nombre, apellido, dni, direccion);
        pasajeros.add(pasajero);
        CreadorAJSON.uploadJSON(archivopasajeros,pasarListaDePersonasAJSON());
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

    public void cargarJSONPasajeros() throws NullNameException
    {
        try {
            JSONArray pasajerosjson = new JSONArray(CreadorAJSON.downloadJSON(archivopasajeros));

            for(int i = 0;i<pasajerosjson.length();i++)
            {
                JSONObject jsonpasajero = pasajerosjson.getJSONObject(i);
                String nombre = jsonpasajero.getString("Nombre");
                String apellido = jsonpasajero.getString("Apellido");
                int dni = jsonpasajero.getInt("Dni");
                String domicilio = jsonpasajero.getString("Domicilio");

                try {
                    VerificacionesDeDatos.verificarDni(dni);
                    Pasajero pasajeronew = new Pasajero(nombre,apellido,dni,domicilio);
                    pasajeros.add(pasajeronew);
                } catch (BadDataException e) {
                    System.out.println("Error en persona" + nombre + " " + apellido + e.getMessage());
                }
            }

        } catch (IOException e) {
            CreadorAJSON.uploadJSON(archivopasajeros,"[]");
            System.out.println("Creando archivo...");
        } catch (NullNameException e)
        {
            throw new NullNameException(e.getMessage());
        }
    }

    public String pasarListaDePersonasAJSON()
    {
        JSONArray arreglopasajeros = new JSONArray();
        for(Pasajero pasajero : pasajeros)
        {
            arreglopasajeros.put(pasajero.toJSON());
        }
        return arreglopasajeros.toString();
    }

    public void cargarJSONEmpleados() throws NullNameException
    {
        try {
            JSONArray empleosjson = new JSONArray(CreadorAJSON.downloadJSON(archivoempleados));
            for(int i = 0;i<empleosjson.length();i++)
            {
                JSONObject jsonpasajero = empleosjson.getJSONObject(i);
                String nombre = jsonpasajero.getString("Nombre");
                String apellido = jsonpasajero.getString("Apellido");
                int dni = jsonpasajero.getInt("Dni");

                String usuario = jsonpasajero.getString("Usuario");
                String clave = jsonpasajero.getString("Clave");
                String email = jsonpasajero.getString("Email");
                TipoEmpleado tipohabitacion = jsonpasajero.getEnum(TipoEmpleado.class,"TipoEmpleado");

                try {
                    VerificacionesDeDatos.verificarDni(dni);
                    Empleado empleado = new Empleado(nombre,apellido,dni,usuario,clave,email,tipohabitacion);
                    empleados.add(empleado);
                } catch (BadDataException e) {
                    System.out.println("Error en persona" + nombre + " " + apellido + e.getMessage());
                }
            }

        } catch (IOException e) {
            CreadorAJSON.uploadJSON(archivoempleados,"[]");
            System.out.println("Creando archivo...");
        } catch (NullNameException e)
        {
            throw new NullNameException(e.getMessage());
        }
    }

    public String pasarListaDeEmpleadosAJSON()
    {
        JSONArray arregloempleados = new JSONArray();
        for(Empleado empleado : empleados)
        {
            arregloempleados.put(empleado.toJSON());
        }
        System.out.println(arregloempleados.toString());
        return arregloempleados.toString();
    }


    /*los mismo seria para las habitaciones y sus tipos :)*/

    // iniciar secion (talvez)

    public void intentarIniciarSesion(String username, String clave) throws PersonaExisteException
    {
        Empleado empleadologin = null;

        for(Empleado empleado : empleados)
        {
            if(empleado.getClave().equals(clave) && empleado.getUsuario().equals(clave))
            {
                empleadologin = empleado;
                break;
            }
        }

        if(empleadologin == null)
        {
            throw new PersonaNoExisteException("El usuario no existe!");
        }

        empleado_en_sistema = empleadologin;
    }

    public void logOut()
    {
        empleado_en_sistema = null;
    }

    public TipoEmpleado buscarPermisoDeUsuario()
    {
        return empleado_en_sistema.getTipo();
    }

    public Empleado retornarEmpleadoLoggeado()
    {
        return empleado_en_sistema;
    }

    public int retornarDniEmpleadoLoggeado()
    {
        return empleado_en_sistema.getDni();
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
        boolean isValid = false;
        Reserva reserva = new Reserva(dniTitular, habitacion, fechaInicio, fechaFinal, guardadoPor);
    try {
        isValid = validarReserva(reserva);
    } catch (ReservaExisteException | BadDataException | ConflictoConFechasException ex) {
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
     * @throws BadDataException
     * @throws ConflictoConFechasException
     *
     */
    public boolean validarReserva(Reserva reserva) throws ReservaExisteException, BadDataException, ConflictoConFechasException {
        boolean respuesta = false;
        
        if (buscarReservaActivaPorTitular(reserva.getDniTitular())){
            throw new ReservaExisteException("Ya hay una reserva registrada con ese titular");
        } else if (!VerificacionesDeDatos.fechaTieneSentido(reserva)) {
            throw new BadDataException("Las fechas contienen errores");
        }else if (verificarFecha(reserva)){
            throw new ConflictoConFechasException("Hay problemas con las fechas!");
        } else {
            respuesta = true;
        }

        return respuesta;
        
    }

    public boolean verificarFecha(Reserva reservaNueva){
        boolean respuesta = false;
        for (Reserva reserva : reservas){
            if (VerificacionesDeDatos.intentoReservaEstaDentroDeGuardada(reservaNueva, reserva) ||
                    VerificacionesDeDatos.intentoReservaContieneUnaReservaGuardada(reservaNueva, reserva) ||
                        VerificacionesDeDatos.intentoReservaInterseccionaConUnaGuardada(reservaNueva, reserva)){

                respuesta = true;

            }
        }
        return respuesta;
    }

    public void cargarJSONReservas() throws NullNameException{
        try {
            JSONArray jsonReservas = new JSONArray(CreadorAJSON.downloadJSON(reservaService.getNombreArchivo()));
            for (int i = 0 ; i < jsonReservas.length(); i++){
                JSONObject jReserva = jsonReservas.getJSONObject(i);

                // creo que no es necesaria, la dejo por las dudas
                int id = jReserva.getInt("id");

                int dniTitular = jReserva.getInt("titular");

                // creo que no es necesaria, la dejo por las dudas
                boolean activa = jReserva.getBoolean("activa");

                int nroHabitacion = jReserva.getInt("habitacion");
                LocalDate fechaInicio = LocalDate.parse(jReserva.getString("fechaInicio"));
                LocalDate fechaFinal = LocalDate.parse(jReserva.getString("fechaFinal"));
                int guardadoPor = jReserva.getInt("guardadoPor");

                Reserva reserva = new Reserva(dniTitular, nroHabitacion, fechaInicio, fechaFinal, guardadoPor);
                reservas.add(reserva);
            }

        } catch (IOException ex){
            reservaService.persistir("[]");
            System.out.println("Creando Archivo....");
        } catch (NullNameException ex){
            throw new NullNameException(ex.getMessage());
        }
    }

    public String mostrarReservaPorId(int id){
        int index = buscarReservaPorId(id);
        StringBuilder reserva =  new StringBuilder();
        if (index != -1){
            reserva.append(reservas.get(index).toString());
        } else {
            // deberia lanzar una excepcion aca??
            System.out.println("La reserva no existe!");
        }

        return reserva.toString();
    }

}

