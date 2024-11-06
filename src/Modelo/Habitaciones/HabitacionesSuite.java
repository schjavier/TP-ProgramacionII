package Modelo.Habitaciones;

import Exceptions.NullNameException;
import org.json.JSONArray;
import org.json.JSONObject;

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

    public JSONArray habitacionesAJson() {
        JSONArray jsonArray = new JSONArray();
        for (HabitacionSuite habitacion : this.getListaHabitaciones()) {
            JSONObject jsonObject = habitacion.toJson();
            jsonArray.put(jsonObject);
        }
        return jsonArray;
    }
}
