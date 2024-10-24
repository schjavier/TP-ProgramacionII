package Modelo.Habitaciones;

import org.json.JSONObject;

import java.util.ArrayList;

abstract public class Habitacion {
    private static int contadorIdHabitacion = 0; // id auto incremental
    private final int nroHabitacion;
    private final int capacidadMaxima;
    private EstadoHabitacion estado;
    private ArrayList<Integer> ocupantes;

    public Habitacion(int capacidadMaxima, EstadoHabitacion estado) { // esto para que casos serviria?
            this.nroHabitacion = ++contadorIdHabitacion;
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

    public EstadoHabitacion getEstado() {
        return estado;
    }

    public void setEstado(EstadoHabitacion estado) {
        this.estado = estado;
    }

    public ArrayList<Integer> getOcupantes() {
        return ocupantes;
    }

    public void setOcupantes(ArrayList<Integer> ocupantes) {
        this.ocupantes = ocupantes;
    }

    public int getNroOcupantes() {
        return ocupantes.size();
    }

    public int getNroHabitacion() {
        return nroHabitacion;
    }

    public int getCapacidadMaxima() {
        return capacidadMaxima;
    }

    @Override
    public String toString() {
        return "Habitacion " + nroHabitacion + '\n' +
                "Capacidad maxima: " + capacidadMaxima + '\n' +
                "Ocupantes actuales: " + ocupantes.size() + '\n' +
                "Estado: " + estado + '\n' +
                "DNI ocupantes: " + ocupantes.toString() + '\n';
    }

    public JSONObject toJson() {
        JSONObject habitacion = new JSONObject();

        habitacion.put("nroHabitacion", this.nroHabitacion);
        habitacion.put("capacidadMaxima", this.capacidadMaxima);
        habitacion.put("estado", this.estado);
        habitacion.put("ocupantes",this.ocupantes.toString());
        return habitacion;
    }
}
