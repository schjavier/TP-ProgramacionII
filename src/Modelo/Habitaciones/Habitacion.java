package Modelo.Habitaciones;

import java.util.ArrayList;

abstract public class Habitacion {
    private static int contadorIdHabitacion = 0; // id auto incremental
    private final int nroHabitacion; // esto deberia ser unico pero ni idea
    private final int capacidadMaxima;
    private EstadoHabitacion estado;
    private ArrayList<Integer> ocupantes;

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

    public int getNroHabitacion() {
        return nroHabitacion;
    }

    public int getCapacidadMaxima() {
        return capacidadMaxima;
    }

    @Override
    public String toString() {
        return "Habitacion + " + nroHabitacion + '\n' +
                "Capacidad maxima: " + capacidadMaxima + '\n' +
                "Estado: " + estado + '\n' +
                "DNI ocupantes: " + ocupantes.toString() + '\n';
    }
}
