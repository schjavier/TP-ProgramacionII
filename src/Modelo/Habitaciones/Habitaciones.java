package Modelo.Habitaciones;

import Exceptions.HabitacionNoEncontradaException;

import java.util.ArrayList;
import java.util.HashMap;

public class Habitaciones<T extends Habitacion>{
    ArrayList<T> listaHabitaciones = new ArrayList<>();
    String tipoHabitacion;


    public Habitaciones(String tipoHabitacion) {
        this.tipoHabitacion = tipoHabitacion;
    }


    public void agregarHabitacion(T habitacion)
    {
        listaHabitaciones.add(habitacion);
    }


    public boolean eliminarSegunNumeroHabitacion(int numHabitacion)
    {
        return listaHabitaciones.removeIf(habitacion -> habitacion.getNroHabitacion() == numHabitacion);
        // estaria bueno borrar reservas hechas de esa habitacion (aca no)
    }

    /**
     * Lista todas las habitaciones.
     * @return Un StringBuilder con las habitaciones (Podria devolver un string mejor -> habitaciones.toString())
     */
    public StringBuilder listarHabitaciones()
    {
        StringBuilder habitaciones = new StringBuilder();
        for(T habitacion : listaHabitaciones)
        {
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
    public StringBuilder listarHabitacionesSegunEstado(EstadoHabitacion estado)
    {
        StringBuilder habitaciones = new StringBuilder();

        for(T habitacion : listaHabitaciones)
        {
            if(habitacion.getEstado() == estado)
            {
                habitaciones.append(habitacion).append("\n");
            }
        }

        return habitaciones;
    }

    /**
     * Metodo que asigna a todas las habitaciones el estado pasado por parametro
     * @param estado
     */
    public void asignarEstadoAtodas(EstadoHabitacion estado)
    {
        for(T habitacion : listaHabitaciones)
        {
            habitacion.setEstado(estado);
        }
    }


    /**
     *
     * @param numHabitacion
     * @return
     * @throws HabitacionNoEncontradaException
     */
    public T traerHabitacionSegunId(int numHabitacion) throws HabitacionNoEncontradaException
    {
        T room = null;
        for(T habitacion : listaHabitaciones)
        {
            if(habitacion.getNroHabitacion() == numHabitacion)
            {
                room = habitacion;
            }
        }

        if(room == null)
        {
            throw new HabitacionNoEncontradaException("No hay ninguna habitación con ese numero");
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
        for(T habitacion : listaHabitaciones)
        {
            if(habitacion.getNroHabitacion() == numhabitacion)
            {
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
    public HashMap<EstadoHabitacion,Integer> contarCantidadHabitacionesSegunEstado()
    {
        HashMap<EstadoHabitacion,Integer> cantidad = new HashMap<>();

        for(EstadoHabitacion valor : EstadoHabitacion.values())
        {
            cantidad.put(valor,0);
        }

        for(T habitacion : listaHabitaciones)
        {
            EstadoHabitacion estado = habitacion.getEstado();
            cantidad.put(estado, cantidad.get(estado) + 1);
        }

        return cantidad;
    }


}
