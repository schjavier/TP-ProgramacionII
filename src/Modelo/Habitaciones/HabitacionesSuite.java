package Modelo.Habitaciones;

import Exceptions.NullNameException;

import java.io.IOException;

public class HabitacionesSuite extends Habitaciones<HabitacionSuite> {
    public HabitacionesSuite() {
        super(TipoHabitacion.SUITE);
    }

    public void marcarTodasHabitacionesComoRevisadas() {
        for (HabitacionSuite habitacion : super.getListaHabitaciones()) {
            habitacion.marcarMantenimientoHechoEnCocina();
        }
    }

    /* por ahora no hago esto */
    @Override
    void leerArchivoYcargarAMemoria() throws IOException, NullNameException {

    }
}
