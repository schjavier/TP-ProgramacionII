package Modelo.Habitaciones;

import Exceptions.NullNameException;

import java.io.IOException;

public class HabitacionesStandard extends Habitaciones<HabitacionStandard>{
    public HabitacionesStandard() {
        super(TipoHabitacion.REGULAR);
    }

    /* por ahora no hago esto */
    @Override
    void leerArchivoYcargarAMemoria() throws IOException, NullNameException {

    }
}
