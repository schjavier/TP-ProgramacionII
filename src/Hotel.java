import DataChecks.VerificacionesDeDatos;
import Exceptions.*;
import JSONCreator.CreadorAJSON;
import Modelo.Habitaciones.*;
import Modelo.Persona.Empleado;
import Modelo.Persona.Pasajero;
import Modelo.Persona.TipoEmpleado;
import Modelo.Reserva.Reserva;
import Modelo.Reserva.ReservaService;
import Persistencia.InterfacePersistecia;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Hotel implements InterfacePersistecia {
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

    public boolean generarReserva(int dniTitular, int habitacion, LocalDateTime fechaInicio, LocalDateTime fechaFinal, int guardadoPor){
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
