import DataChecks.VerificacionesDeDatos;
import Exceptions.*;
import Modelo.Habitaciones.EstadoHabitacion;
import Modelo.Habitaciones.HabitacionStandard;
import Modelo.Habitaciones.HabitacionSuite;
import Modelo.Habitaciones.Habitaciones;
import Modelo.Persona.Empleado;
import Modelo.Persona.Pasajero;

import java.util.ArrayList;
import java.util.HashMap;

public class Hotel { // ESTO ES EL WRAPPER CLASS

    /* TESTEO */
    HabitacionStandard habitacion1 = new HabitacionStandard(4, EstadoHabitacion.DISPONIBLE);
    HabitacionStandard habitacion2 = new HabitacionStandard(4,EstadoHabitacion.OCUPADA);
    HabitacionStandard habitacion3 = new HabitacionStandard(4,EstadoHabitacion.OCUPADA);
    HabitacionStandard habitacion4 = new HabitacionStandard(4,EstadoHabitacion.LIMPIEZA);
    HabitacionStandard habitacion5 = new HabitacionStandard(4,EstadoHabitacion.DISPONIBLE);

    HabitacionSuite habitacion6 = new HabitacionSuite(4, EstadoHabitacion.DISPONIBLE);
    HabitacionSuite habitacion7 = new HabitacionSuite(4,EstadoHabitacion.OCUPADA);
    HabitacionSuite habitacion8 = new HabitacionSuite(4,EstadoHabitacion.OCUPADA);
    HabitacionSuite habitacion9 = new HabitacionSuite(4,EstadoHabitacion.LIMPIEZA);
    HabitacionSuite habitacion10 = new HabitacionSuite(4,EstadoHabitacion.DISPONIBLE);

    Habitaciones<HabitacionStandard> habitacionesStandard = new Habitaciones<>("Standard");
    Habitaciones<HabitacionSuite> habitacionesSuite = new Habitaciones<>("Suite");
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
        habitacionesStandard.agregarHabitacion(habitacion1);
        habitacionesStandard.agregarHabitacion(habitacion2);
        habitacionesStandard.agregarHabitacion(habitacion3);
        habitacionesStandard.agregarHabitacion(habitacion4);
        habitacionesStandard.agregarHabitacion(habitacion5);

        habitacionesSuite.agregarHabitacion(habitacion6);
        habitacionesSuite.agregarHabitacion(habitacion7);
        habitacionesSuite.agregarHabitacion(habitacion8);
        habitacionesSuite.agregarHabitacion(habitacion9);
        habitacionesSuite.agregarHabitacion(habitacion10);

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
        return selectorDeTipoHabitacion(tipohabitacion).eliminarHabitacionSegunNumero(habitacion);
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
            case 1 -> habitacionesStandard;
            case 2 -> habitacionesSuite;
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


    public void gestionarHabitaciones(int tipohabitacion)
    {
        GestionHabitaciones.mostrarMenu(selectorDeTipoHabitacion(tipohabitacion));
    }




}
