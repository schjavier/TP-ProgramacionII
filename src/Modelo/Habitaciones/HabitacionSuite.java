package Modelo.Habitaciones;

public class HabitacionSuite extends Habitacion{

    public HabitacionSuite(int capacidadMaxima, EstadoHabitacion estado) {
        super(capacidadMaxima, estado);
        super.tipoHabitacion = "Suite";
    }

    public HabitacionSuite(int capacidadMaxima) {
        super(capacidadMaxima);
        super.tipoHabitacion = "Suite";
    }

}
