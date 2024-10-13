package Modelo.Habitaciones;

import Modelo.Habitaciones.EstadoHabitacion;
import Modelo.Habitaciones.Habitacion;

public class HabitacionStandard extends Habitacion { // la dejo vacia asi cuando se arregle esto de los pasajeros lo arranco

    public HabitacionStandard(int capacidadMaxima, EstadoHabitacion estado) {
        super(capacidadMaxima, estado);
    }

    public HabitacionStandard(int capacidadMaxima) {
        super(capacidadMaxima);
    }


}
