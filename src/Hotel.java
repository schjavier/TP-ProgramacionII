import DataChecks.VerificacionesDeDatos;
import Exceptions.*;
import Modelo.Habitaciones.*;
import Modelo.Persona.Empleado;
import Modelo.Persona.Pasajero;

import java.util.ArrayList;
import java.util.HashMap;

public class Hotel {

    private final HabitacionesStandard habitacionesStandard = new HabitacionesStandard();
    private final HabitacionesSuite habitacionesSuite = new HabitacionesSuite();
    private final HabitacionesPresidenciales habitacionesPresidenciales = new HabitacionesPresidenciales();
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

}
