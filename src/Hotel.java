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

    // prueba de reservas
    ReservaService reservaService = new ReservaService();
    ArrayList<Reserva> reservas = reservaService.filtrarPorActivas();

    Reserva reserva1 = new Reserva(persona1.getDni(), true, habitacion1.getNroHabitacion(), LocalDate.of(2024, 11, 12), LocalDate.of(2024, 11, 15), empleado1.getIdEmpleado());
    Reserva reserva2 = new Reserva(persona2.getDni(), true, habitacion2.getNroHabitacion(), LocalDate.of(2024, 11, 12), LocalDate.of(2024, 11, 15), empleado1.getIdEmpleado());
    Reserva reserva3 = new Reserva(persona3.getDni(), true, habitacion4.getNroHabitacion(), LocalDate.of(2024, 11, 12), LocalDate.of(2024, 11, 15), empleado1.getIdEmpleado());



    /* TESTEO END */



    public Hotel() { // test
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

        reservaService.generarReserva(reserva1);
        reservaService.generarReserva(reserva2);
        reservaService.generarReserva(reserva3);
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





}
