package Modelo.Habitaciones;

import Exceptions.HabitacionNoEncontradaException;
import Exceptions.NullNameException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Lista que contiene metodos basicos que se comparten entre todos los tipos de habitaciones.
 * @param <T>
 */
abstract public class Habitaciones<T extends Habitacion> {
    private String nombrearchivo = "";
    private ArrayList<T> listaHabitaciones = new ArrayList<>();
    private TipoHabitacion tipoHabitacion;


    public Habitaciones(TipoHabitacion tipoHabitacion) {
        this.tipoHabitacion = tipoHabitacion;
        this.nombrearchivo = this.nombrearchivo.concat("Habitaciones" + tipoHabitacion.getTipo() + ".json");
    }

    public ArrayList<T> getListaHabitaciones() {
        return listaHabitaciones;
    }

    /**
     * Agrega una habitacion a la lista
     * @param habitacion Habitacion que se creo
     */
    public void agregarHabitacion(T habitacion)
    {
        listaHabitaciones.add(habitacion);
    }


    /**
     * Se elimina una habitacion segun el numero elegido. (NO ESTOY SEGURO SI TIENE SENTIDO TENER ESTO.)
     * @param numHabitacion Numero elegido.
     * @return retorna true si elimino algo.
     */
    public boolean eliminarHabitacionSegunNumero(int numHabitacion) {
        return listaHabitaciones.removeIf(habitacion -> habitacion.getNroHabitacion() == numHabitacion);
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
     *
     * @param numHabitacion
     * @return
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


    public ArrayList<T> retornarLista()
    {
        return listaHabitaciones;
    }

    public void limpiarPersonasDeHabitaciones()
    {
        for (T habitacion : listaHabitaciones) {
            if(habitacion.getNroOcupantes() != 0)
            {
                habitacion.removerPersonas();
                habitacion.setEstado(EstadoHabitacion.DISPONIBLE);
            }
        }
    }

}
