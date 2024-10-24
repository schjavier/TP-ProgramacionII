package Modelo.Habitaciones;

import Exceptions.HabitacionNoEncontradaException;
import Exceptions.NullNameException;
import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

abstract public class Habitaciones<T extends Habitacion> {
    private ArrayList<T> listaHabitaciones = new ArrayList<>();


    public Habitaciones() {
    }

    public ArrayList<T> getListaHabitaciones() {
        return listaHabitaciones;
    }

    public void agregarHabitacion(T habitacion)
    {
        listaHabitaciones.add(habitacion);
    }


    public boolean eliminarHabitacionSegunNumero(int numHabitacion) {
        return listaHabitaciones.removeIf(habitacion -> habitacion.getNroHabitacion() == numHabitacion);
        // estaria bueno borrar reservas hechas de esa habitacion (aca no)
    }

    /**
     * Lista todas las habitaciones.
     * @return Un StringBuilder con las habitaciones (Podria devolver un string mejor -> habitaciones.toString())
     */
    public StringBuilder listarHabitaciones() {
        StringBuilder habitaciones = new StringBuilder();
        for (T habitacion : listaHabitaciones) {
            habitaciones.append(habitacion.toString()).append("\n");
        }
        return habitaciones;
    }

    /**
     * Metodo que lista todas las habitaciones segun el estado en el que se encuentran.
     * @param estado Enum que representa el estado de la habitacion
     * @return Devuelve un stringBuilder con todas las habitaciones que coinciden
     * con el estado pasado por parametro.
     *
     * (Podria devolver un string mejor -> habitaciones.toString())
     */
    public StringBuilder listarHabitacionesSegunEstado(EstadoHabitacion estado) {
        StringBuilder habitaciones = new StringBuilder();

        for (T habitacion : listaHabitaciones) {
            if (habitacion.getEstado() == estado) {
                habitaciones.append(habitacion).append("\n");
            }
        }

        return habitaciones;
    }

    /**
     * Metodo que asigna a todas las habitaciones el estado pasado por parametro
     * @param estado
     */
    public void asignarEstadoAtodas(EstadoHabitacion estado) {
        for (T habitacion : listaHabitaciones) {
            habitacion.setEstado(estado);
        }
    }


    /**
     *
     * @param numHabitacion
     * @return
     * @throws HabitacionNoEncontradaException
     */
    public T traerHabitacionSegunId(int numHabitacion){
        T room = null;
        for (T habitacion : listaHabitaciones) {
            if (habitacion.getNroHabitacion() == numHabitacion) {
                room = habitacion;
            }
        }

        return room;
    }

    /**
     *  Si creo que seria lo mejor hacer un static id
     * @param numhabitacion
     * @return
     */
    public boolean verSiElNumeroEstaDisponible(int numhabitacion) // esto es para hacer que sean unicas (creo que puede ser un static id, haciendo esto innecesario)
    {
        boolean respuesta = true;
        for (T habitacion : listaHabitaciones) {
            if (habitacion.getNroHabitacion() == numhabitacion) {
                respuesta = false;
                break;
            }
        }
        return respuesta;
    }

    /**
     * Juan cruz me dijo una vez que no esta bueno devolver un map,
     * por que desde el map se pueden modificar las cosas,
     * entonces no se aplicaria el encapsulamiento de manera correcta.
     * @return
     */
    public String contarCantidadHabitacionesSegunEstado() {
        HashMap<EstadoHabitacion, Integer> cantidad = new HashMap<>();

        for (EstadoHabitacion valor : EstadoHabitacion.values()) {
            cantidad.put(valor, 0);
        }

        for (T habitacion : listaHabitaciones) {
            EstadoHabitacion estado = habitacion.getEstado();
            cantidad.put(estado, cantidad.get(estado) + 1);
        }

        System.out.println(cantidad);

        return cantidad.toString();
    }

    abstract void leerArchivoYcargarAMemoria() throws IOException, NullNameException;
}
