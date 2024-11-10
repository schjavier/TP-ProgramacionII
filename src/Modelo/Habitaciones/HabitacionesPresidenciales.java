package Modelo.Habitaciones;

import org.json.JSONArray;
import org.json.JSONObject;

public class HabitacionesPresidenciales extends Habitaciones<HabitacionPresidencial>{
    public HabitacionesPresidenciales() {
        super(TipoHabitacion.PRESIDENCIAL);
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

    public JSONArray habitacionesAJson() {
        JSONArray jsonArray = new JSONArray();
        for (HabitacionPresidencial habitacion : this.getListaHabitaciones()) {
            JSONObject jsonObject = habitacion.toJson();
            jsonArray.put(jsonObject);
        }
        return jsonArray;
    }
}
