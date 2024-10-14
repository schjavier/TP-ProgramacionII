import DataChecks.VerificacionesDeDatos;
import Exceptions.*;
import Modelo.Habitaciones.*;
import Modelo.Persona.Empleado;
import Modelo.Persona.Pasajero;

import java.util.ArrayList;
import java.util.HashMap;

public class Hotel { // ESTO ES EL WRAPPER CLASS

    /* TESTEO */
//    HabitacionStandard habitacion1 = new HabitacionStandard(4, EstadoHabitacion.DISPONIBLE);
//    HabitacionStandard habitacion2 = new HabitacionStandard(4,EstadoHabitacion.OCUPADA);
//    HabitacionStandard habitacion3 = new HabitacionStandard(4,EstadoHabitacion.OCUPADA);
//    HabitacionStandard habitacion4 = new HabitacionStandard(4,EstadoHabitacion.LIMPIEZA);
//    HabitacionStandard habitacion5 = new HabitacionStandard(4,EstadoHabitacion.DISPONIBLE);
//
//    HabitacionSuite habitacion6 = new HabitacionSuite(4, EstadoHabitacion.DISPONIBLE);
//    HabitacionSuite habitacion7 = new HabitacionSuite(4,EstadoHabitacion.OCUPADA);
//    HabitacionSuite habitacion8 = new HabitacionSuite(4,EstadoHabitacion.OCUPADA);
//    HabitacionSuite habitacion9 = new HabitacionSuite(4,EstadoHabitacion.LIMPIEZA);
//    HabitacionSuite habitacion10 = new HabitacionSuite(4,EstadoHabitacion.DISPONIBLE);
//
//    Habitaciones<HabitacionStandard> habitacionesStandard = new Habitaciones<>("Standard");
//    Habitaciones<HabitacionSuite> habitacionesSuite = new Habitaciones<>("Suite");
//    ArrayList<Pasajero> pasajeros = new ArrayList<>();
//    ArrayList<Empleado> empleados = new ArrayList<>();
//
//    Pasajero persona1 = new Pasajero("Carlos","Test",11111111,"Calle 124 N°214");
//    Pasajero persona2 = new Pasajero("Zara","Nana",44444444,"Donde sea N°111");
//    Pasajero persona3 = new Pasajero("Mora","Li",55555555,"Calle xd N°214");
//    Pasajero persona4 = new Pasajero("Doña","Flores",12345678,"Casa");
//
//    Empleado empleado1 = new Empleado("Mr Empleado","N1",22225555, "Mr1","test@gmail.com","Test123");
//    Empleado empleado2 = new Empleado("Miss Empleado","N2",22225555, "Miss1","miss1@gmail.com","Lololol123123");
    HabitacionesStandard habitacionesStandard = new HabitacionesStandard();
    HabitacionesSuite habitacionesSuite = new HabitacionesSuite();
    /* TESTEO END */

    /*private final Habitaciones<HabitacionStandard> habitacionesStandard = new Habitaciones<>(TipoHabitacion.REGULAR);
    private final Habitaciones<HabitacionSuite> habitacionesSuite = new Habitaciones<>(TipoHabitacion.SUITE);*/
    private final ArrayList<Pasajero> pasajeros = new ArrayList<>();
    private final ArrayList<Empleado> empleados = new ArrayList<>();
    private String nombre;

    public Hotel(String nombre) {
        this.nombre = nombre;
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
                //case 3 -> agregarpresidencial
                default -> throw new BadOptionException("Elegir una opcion correcta!!");
            }
        }
        habitaciones.listarHabitaciones();
    }

    public String contarEstadoHabitaciones(int tipohabitacion) {
        return selectorDeTipoHabitacion(tipohabitacion).contarCantidadHabitacionesSegunEstado();
    }

    public StringBuilder listarHabitaciones() {
        return habitacionesStandard.listarHabitaciones().append(habitacionesSuite.listarHabitaciones());
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
            //case 3 -> habitacionesPresidencial;
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


    public boolean agregarPasajero(String nombre, String apellido, int dni, String direccion) {
        Pasajero pasajero = new Pasajero(nombre, apellido, dni, direccion);
        return pasajeros.add(pasajero);
    }

    public int obtenerNroHabitaciones() {
        int total = 0;
        for (HabitacionStandard habitacion : habitacionesStandard.getListaHabitaciones()) {
            total += 1;
        }
        for (HabitacionSuite habitacion : habitacionesSuite.getListaHabitaciones()) {
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

        return total;
    }

}
