package Modelo.Habitaciones;

public class HabitacionesSuite extends Habitaciones<HabitacionSuite> {
    public HabitacionesSuite() {
        super(TipoHabitacion.SUITE);
    }

    public void marcarTodasHabitacionesComoRevisadas()
    {
        for (HabitacionSuite habitacion : super.getListaHabitaciones())
        {
            habitacion.marcarMantenimientoHechoEnCocina();
        }
    }
}
