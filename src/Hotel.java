import DataChecks.VerificacionesDeDatos;
import Exceptions.*;
import Modelo.Habitaciones.EstadoHabitacion;
import Modelo.Habitaciones.Habitacion;
import Modelo.Habitaciones.HabitacionStandard;
import Modelo.Habitaciones.Habitaciones;
import Modelo.Persona.Empleado;
import Modelo.Persona.Pasajero;
import Modelo.Reserva.Reserva;
import Modelo.Reserva.ReservaService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Hotel { // ESTO ES EL WRAPPER CLASS

    private ArrayList<Reserva> historialReservas;

    /* TESTEO */
    HabitacionStandard habitacion1 = new HabitacionStandard(12,4, EstadoHabitacion.DISPONIBLE);
    HabitacionStandard habitacion2 = new HabitacionStandard(15,4,EstadoHabitacion.OCUPADA);
    HabitacionStandard habitacion3 = new HabitacionStandard(11,4,EstadoHabitacion.OCUPADA);
    HabitacionStandard habitacion4 = new HabitacionStandard(18,4,EstadoHabitacion.LIMPIEZA);
    HabitacionStandard habitacion5 = new HabitacionStandard(9,4,EstadoHabitacion.DISPONIBLE);

    Habitaciones<HabitacionStandard> habitaciones = new Habitaciones<>("test");
    ArrayList<Pasajero> pasajeros = new ArrayList<>();
    ArrayList<Empleado> empleados = new ArrayList<>();

    Pasajero persona1 = new Pasajero("Carlos","Test",11111111,"Calle 124 N°214");
    Pasajero persona2 = new Pasajero("Zara","Nana",44444444,"Donde sea N°111");
    Pasajero persona3 = new Pasajero("Mora","Li",55555555,"Calle xd N°214");
    Pasajero persona4 = new Pasajero("Doña","Flores",12345678,"Casa");

    Empleado empleado1 = new Empleado("Mr Empleado","N1",22225555, "Mr1","test@gmail.com","Test123");
    Empleado empleado2 = new Empleado("Miss Empleado","N2",22225555, "Miss1","miss1@gmail.com","Lololol123123");



    /* TESTEO END */



    public Hotel() { // test
        historialReservas = new ArrayList<>();

        habitaciones.agregarHabitacion(habitacion1);
        habitaciones.agregarHabitacion(habitacion2);
        habitaciones.agregarHabitacion(habitacion3);
        habitaciones.agregarHabitacion(habitacion4);
        habitaciones.agregarHabitacion(habitacion5);

        pasajeros.add(persona1);
        pasajeros.add(persona2);
        pasajeros.add(persona3);
        pasajeros.add(persona4);

        empleados.add(empleado1);
        empleados.add(empleado2);


    }

    public HashMap contarEstadoHabitaciones(int tipohabitacion)
    {
        return selectorDeTipoHabitacion(tipohabitacion).contarCantidadHabitacionesSegunEstado();
    }


    public StringBuilder listarHabitaciones(int tipohabitacion)
    {
        return selectorDeTipoHabitacion(tipohabitacion).listarHabitaciones();
    }

    public boolean eliminarHabitacion(int tipohabitacion,int habitacion)
    {
        return selectorDeTipoHabitacion(tipohabitacion).eliminarSegunNumeroHabitacion(habitacion);
    }


    /**
     *
     * @param tipohabitacion Un numero del 1 al x siendo x el ultimo tipo de habitacion que haya
     * @return La lista que en la que se trabajara
     * @throws BadOptionException En el caso de que el id de tipo habitacion (lista) no este dentro del switch
     */
    public Habitaciones selectorDeTipoHabitacion(int tipohabitacion) throws BadOptionException
    {
        return switch (tipohabitacion) {
            case 1 -> habitaciones;
            default -> throw new BadOptionException("Elegir una opcion correcta!!");
        };
    }

    public StringBuilder listarSegunEstado(int tipohabitacion,EstadoHabitacion estado)
    {
        return selectorDeTipoHabitacion(tipohabitacion).listarHabitacionesSegunEstado(estado);

    }

    public boolean existePasajeroConEseDNI(int dni) throws BadDataException // para hacer reservas o alguna otra cosa
    {
        VerificacionesDeDatos.verificarDni(dni);

        boolean existe = false;
        for(Pasajero pasajero : pasajeros)
        {
            if(pasajero.getDni() == dni)
            {
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
        for(Pasajero pasajero : pasajeros)
        {
            if(pasajero.getDni() == dni)
            {
                persona = pasajero;
                break;
            }
        }

        if(persona == null)
        {
            throw new PersonaNoExisteException("Persona con el DNI: "+ dni +" no existe, por favor cargar persona con ese dni de nuevo.");
        }

        return persona;
    }

    public boolean existeEmpleadoConEseDNI(int dni)
    {
        VerificacionesDeDatos.verificarDni(dni);
        boolean existe = false;
        for(Empleado empleado : empleados)
        {
            if(empleado.getDni() == dni)
            {
                existe = true;
                break;
            }
        }
        return existe;
    }


    public void verSiElDniEstaCargardo(int dni) throws PersonaExisteException, BadDataException
    {
        VerificacionesDeDatos.verificarDni(dni);

        if(existeEmpleadoConEseDNI(dni) || existePasajeroConEseDNI(dni))
        {
            throw new PersonaExisteException("Hay alguien con el dni " + dni );
        }
    }



    public boolean agregarPasajero(String nombre,String apellido,int dni,String direccion)
    {
        Pasajero pasajero = new Pasajero(nombre,apellido,dni,direccion);
        return pasajeros.add(pasajero);
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

        for (Reserva reserva : historialReservas){
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
        for (Reserva reserva : historialReservas){
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

        for (Reserva reserva : historialReservas){
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
     * Filtran el historial de las reservas para mostrar todas las activas!
     * @return string debe devolver un string todavia no lo hace! ja!
     */

    public ArrayList<Reserva> filtrarPorActivas(){
        ArrayList<Reserva> reservasActivas = new ArrayList<>();
        for (Reserva reserva : historialReservas){
            if (reserva.isActiva()){
                reservasActivas.add(reserva);
            }
        }
        return reservasActivas;
    }

    /**
     *
     * Genera una reserva nueva y la guarda en el historial de reservas.
     *
     * @param dniTitular int representando el dni del titular de la reserva
     * @param activa si se encuentra activa, es decir si no sucedio o esta sucediendo
     * @param habitacion numero de la habitacion de la reserva
     * @param fechaInicio LocalDateTime para la fecha de inicio de la reserva
     * @param fechaFinal LocalDateTime para el final de la reserva
     * @param guardadoPor dni del empleado que guarda la reserva.
     *
     * @return Devuelve True si la reserva pudo generarse, False de otro modo.
     */

    public boolean generarReserva(int dniTitular, boolean activa, int habitacion, LocalDate fechaInicio, LocalDate fechaFinal, int guardadoPor){
        boolean respuesta = false;
        Reserva reserva = new Reserva(dniTitular, activa, habitacion, fechaInicio, fechaFinal, guardadoPor);

        if (historialReservas.isEmpty()){
            historialReservas.add(reserva);
            respuesta = true;
        } else if (!buscarReservaActivaPorTitular(dniTitular)){
            historialReservas.add(reserva);
            respuesta = true;
        }
        return respuesta;
    }

    /**
     *
     * Genera una reserva nueva y la guarda en el historial de reservas.
     *
     * @param reserva Objeto Reserva que sera generado.
     * @return Devuelve True si la reserva pudo generarse, False de otro modo.
     */
    public boolean generarReserva(Reserva reserva){
        boolean respuesta = false;
        if (historialReservas.isEmpty()){
            historialReservas.add(reserva);
            respuesta = true;
        } else if (!buscarReservaActivaPorTitular(reserva.getDniTitular())){
            historialReservas.add(reserva);
            respuesta = true;
        }
        return respuesta;
    }


}
