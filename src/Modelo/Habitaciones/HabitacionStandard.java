package Modelo.Habitaciones;

public class HabitacionStandard extends Habitacion { // la dejo vacia asi cuando se arregle esto de los pasajeros lo arranco

    public HabitacionStandard(int capacidadMaxima, EstadoHabitacion estado) {
        super(capacidadMaxima, estado);
        super.tipoHabitacion = "Standard";
    }

    public HabitacionStandard(int capacidadMaxima) {
        super(capacidadMaxima);
        super.tipoHabitacion = "Standard";
    }


}
