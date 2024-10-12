package Modelo.Habitaciones;

import Modelo.Habitaciones.EstadoHabitacion;
import Modelo.Habitaciones.Habitacion;

public class HabitacionStandard extends Habitacion { // la dejo vacia asi cuando se arregle esto de los pasajeros lo arranco

    public HabitacionStandard(int nroHabitacion, int capacidadMaxima, EstadoHabitacion estado) {
        super(nroHabitacion, capacidadMaxima, estado);
    }

    public HabitacionStandard(int nroHabitacion, int capacidadMaxima) {
        super(nroHabitacion, capacidadMaxima);
    }


}
