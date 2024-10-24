package Modelo.Habitaciones;

import Exceptions.NullNameException;

import java.io.IOException;

public class HabitacionesPresidenciales extends Habitaciones<HabitacionPresidencial>{
    public HabitacionesPresidenciales() {
        super();
    }

    public void marcarTodasHabitacionesComoRevisadas() {
        for (HabitacionPresidencial habitacion : super.getListaHabitaciones()) {
            habitacion.marcarMantenimientoHechoEnCocina();
        }
    }

    public void marcarTodasHabitacionesComoRevisadasJacuzzi() {
        for (HabitacionPresidencial habitacion : super.getListaHabitaciones()) {
            habitacion.marcarMantenimientoEnJacuzzi();
        }
    }

    /* por ahora no hago esto */
    @Override
    void leerArchivoYcargarAMemoria() throws IOException, NullNameException {

    }
}
