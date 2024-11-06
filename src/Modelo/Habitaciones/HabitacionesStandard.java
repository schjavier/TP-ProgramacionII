package Modelo.Habitaciones;

import Exceptions.NullNameException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class HabitacionesStandard extends Habitaciones<HabitacionStandard>{
    public HabitacionesStandard() {
        super(TipoHabitacion.REGULAR);
    }

    public JSONArray habitacionesAJson() {
        JSONArray jsonArray = new JSONArray();
        for (HabitacionStandard habitacion : this.getListaHabitaciones()) {
            JSONObject jsonObject = habitacion.toJson();
            jsonArray.put(jsonObject);
        }

        return jsonArray;
    }
}
