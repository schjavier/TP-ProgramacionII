package Modelo.Habitaciones;

import org.json.JSONArray;
import org.json.JSONObject;

public class HabitacionesSuite extends Habitaciones<HabitacionSuite> {
    public HabitacionesSuite() {
        super(TipoHabitacion.SUITE);
    }

    /**
     * Metodo que permite marcar todas las habitaciones como revisadas.
     */

    public void marcarTodasHabitacionesComoRevisadas() {
        for (HabitacionSuite habitacion : super.getListaHabitaciones()) {
            habitacion.marcarMantenimientoHechoEnCocina();
        }
    }

    /**
     * Metodoq ue permite pasar de una lista de habitaciones aun JSONArray
     * @return un objeto de tipo {@code JSONArray}
     */

    public JSONArray habitacionesAJson() {
        JSONArray jsonArray = new JSONArray();
        for (HabitacionSuite habitacion : this.getListaHabitaciones()) {
            JSONObject jsonObject = habitacion.toJson();
            jsonArray.put(jsonObject);
        }
        return jsonArray;
    }
}
