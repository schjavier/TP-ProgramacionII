package Modelo.Habitaciones;

import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Clase abstracta, establece el contrato de herencia para con sus hijos
 */

abstract public class Habitacion {
    private static int contadorIdHabitacion = 0; // id auto incremental
    private final int nroHabitacion;
    private final int capacidadMaxima;
    private EstadoHabitacion estado;
    private ArrayList<Integer> ocupantes;

    public Habitacion(int nroHabitacion, int capacidadMaxima, EstadoHabitacion estado) {
            ++contadorIdHabitacion;
            this.nroHabitacion = nroHabitacion;
            this.capacidadMaxima = capacidadMaxima;
            this.estado = estado;
            this.ocupantes = new ArrayList<Integer>();
    }

    public Habitacion(int capacidadMaxima) {
        this.nroHabitacion = ++contadorIdHabitacion;
        this.capacidadMaxima = capacidadMaxima;
        this.estado = EstadoHabitacion.DISPONIBLE;
        this.ocupantes = new ArrayList<Integer>();
    }

    /**
     * devuelve el estado de una habitacion
     * @return un objeto de tipo {@code EstadoHabitacion}
     */
    public EstadoHabitacion getEstado() {
        return estado;
    }

    /**
     * Metodo que permite setear un estado recivido por parametro
     * @param estado objeto de tipo {@code EstadoHabitacion} que representa el nuevo valor de atributo
     */
    public void setEstado(EstadoHabitacion estado) {
        this.estado = estado;
    }

    /**
     * Metodo par obtener los ocupantes
     * @return un {@code ArrayList} con todos los ocupantes de una habitacion
     */
    public ArrayList<Integer> getOcupantes() {
        return ocupantes;
    }

    /**
     * Metodo para obener el mumero de ocupantes.
     * @return un {@code int} que es el numero total de ocupantes de una habitacion
     */
    public int getNroOcupantes() {
        return ocupantes.size();
    }

    /**
     * Metodo getter para el nro de habitacion,
     * @return
     */
    public int getNroHabitacion() {
        return nroHabitacion;
    }

    /**
     * Metodo getter para la capacidad maxima permitida por habitacion
     * @return un {@code int} que representa la capacidad maxima de una habitacion
     */

    public int getCapacidadMaxima() {
        return capacidadMaxima;
    }

    /**
     * metodoq ue permite agrega personas a una habitacion
     * @param dni el dni de la persona a agregar
     */

    public void agregarPersonaAHabitacion(int dni)
    {
        ocupantes.add(dni);
    }

    /**
     * metodo que permite eliminar una persona
     */

    public void removerPersonas()
    {
        ocupantes.clear();
    }

    /**
     * Metodo sobre escrito de herededado de la clase object
     * @return
     */
    @Override
    public String toString() {
        return "Habitacion " + nroHabitacion + '\n' +
                "Capacidad maxima: " + capacidadMaxima + '\n' +
                "Ocupantes actuales: " + ocupantes.size() + '\n' +
                "Estado: " + estado + '\n' +
                "DNI ocupantes: " + ocupantes.toString() + '\n';
    }

    /**
     * Metodo que permite pasar una habitacion a JSON.
     * @return un objeto de tipo {@code JSONObject}
     */
    public JSONObject toJson() {
        JSONObject habitacion = new JSONObject();

        habitacion.put("nroHabitacion", this.nroHabitacion);
        habitacion.put("capacidadMaxima", this.capacidadMaxima);
        habitacion.put("estado", this.estado);
        return habitacion;
    }
}
