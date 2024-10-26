package Modelo.Habitaciones;

import Exceptions.NullNameException;
import org.json.JSONArray;
import org.json.JSONObject;

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

    public JSONArray habitacionesAJson() {
        JSONArray jsonArray = new JSONArray();
        for (HabitacionPresidencial habitacion : this.getListaHabitaciones()) {
            JSONObject jsonObject = habitacion.toJson();
            jsonArray.put(jsonObject);
        }
        return jsonArray;
    }
}
