package Modelo.Habitaciones;

import Exceptions.NullNameException;
import JSONCreator.CreadorAJSON;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class HabitacionesPresidenciales extends Habitaciones<HabitacionPresidencial>{
    public HabitacionesPresidenciales() {
        super(TipoHabitacion.PRESIDENCIAL);
    }

    public void marcarTodasHabitacionesComoRevisadas()
    {
        for (HabitacionPresidencial habitacion : super.getListaHabitaciones())
        {
            habitacion.marcarMantenimientoHechoEnCocina();
        }
    }

    public void marcarTodasHabitacionesComoRevisadasJacuzzi()
    {
        for (HabitacionPresidencial habitacion : super.getListaHabitaciones())
        {
            habitacion.marcarMantenimientoEnJacuzzi();
        }
    }

    /* por ahora no hago esto */
    @Override
    void leerArchivoYcargarAMemoria() throws IOException, NullNameException {

    }
}
