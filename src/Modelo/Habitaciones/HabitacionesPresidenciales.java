package Modelo.Habitaciones;

import Exceptions.NullNameException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Clase de habitaciones que reporesentan las habitaciones mas lujosas.
 */
public class HabitacionesPresidenciales extends Habitaciones<HabitacionPresidencial>{

    public HabitacionesPresidenciales() {
        super(TipoHabitacion.PRESIDENCIAL);
    }

    /**
     * Metodo que marca todas las habitaciones como revidadas.
     */
    public void marcarTodasHabitacionesComoRevisadas() {
        for (HabitacionPresidencial habitacion : super.getListaHabitaciones()) {
            habitacion.marcarMantenimientoHechoEnCocina();
        }

    }


    /**
     * metodo que marca todas las habitacioens con javuzzi como <em>revisadas</em>
     */
    public void marcarTodasHabitacionesComoRevisadasJacuzzi() {
        for (HabitacionPresidencial habitacion : super.getListaHabitaciones()) {
            habitacion.marcarMantenimientoEnJacuzzi();
        }
    }

    /**
     * Metodo que permite pasar de una lista de habitaciones aun JSONArray
     * @return un objeto de tipo {@code JSONArray}
     */

    public JSONArray habitacionesAJson() {
        JSONArray jsonArray = new JSONArray();
        for (HabitacionPresidencial habitacion : this.getListaHabitaciones()) {
            JSONObject jsonObject = habitacion.toJson();
            jsonArray.put(jsonObject);
        }
        return jsonArray;
    }


}
