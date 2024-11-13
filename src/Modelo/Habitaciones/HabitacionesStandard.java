package Modelo.Habitaciones;

import org.json.JSONArray;
import org.json.JSONObject;

public class HabitacionesStandard extends Habitaciones<HabitacionStandard>{
    public HabitacionesStandard() {
        super(TipoHabitacion.REGULAR);
    }


    /**
     * Metodo que permite pasar de una lista de habitaciones aun JSONArray
     * @return un objeto de tipo {@code JSONArray}
     */
    public JSONArray habitacionesAJson() {
        JSONArray jsonArray = new JSONArray();
        for (HabitacionStandard habitacion : this.getListaHabitaciones()) {
            JSONObject jsonObject = habitacion.toJson();
            jsonArray.put(jsonObject);
        }

        return jsonArray;
    }
}
