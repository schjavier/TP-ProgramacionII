package Modelo.Habitaciones;

import java.util.ArrayList;

abstract public class Habitacion {
    private static int contadorIdHabitacion = 0; // id auto incremental
    private final int nroHabitacion;
    private final int capacidadMaxima;
    private EstadoHabitacion estado;
    private ArrayList<Integer> ocupantes;
    private TipoHabitacion tipoHabitacion;

    public Habitacion(int capacidadMaxima, EstadoHabitacion estado) {
            this.nroHabitacion = ++contadorIdHabitacion;
            this.capacidadMaxima = capacidadMaxima;
            this.estado = estado;
            this.ocupantes = new ArrayList<Integer>();
    }

    public Habitacion(int capacidadMaxima, TipoHabitacion tipo) {
        this.nroHabitacion = ++contadorIdHabitacion;
        this.capacidadMaxima = capacidadMaxima;
        this.estado = EstadoHabitacion.DISPONIBLE;
        this.ocupantes = new ArrayList<Integer>();
        this.tipoHabitacion = tipo;
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
                "Tipo: " + tipoHabitacion.getTipo() + '\n' +
                "Capacidad maxima: " + capacidadMaxima + '\n' +
                "Ocupantes actuales: " + ocupantes.size() + '\n' +
                "Estado: " + estado + '\n' +
                "DNI ocupantes: " + ocupantes.toString() + '\n';
    }
}
