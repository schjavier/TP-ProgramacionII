package Modelo.Habitaciones;

import org.json.JSONObject;

import java.util.ArrayList;

public class HabitacionStandard extends Habitacion { // la dejo vacia asi cuando se arregle esto de los pasajeros lo arranco


    public HabitacionStandard(int nroHabitacion, int capacidadMaxima, EstadoHabitacion estado) {
        super(nroHabitacion, capacidadMaxima, estado);
    }

    public HabitacionStandard(int capacidadMaxima) {
        super(capacidadMaxima);
    }

    @Override
    public String toString() {
        return super.toString() + "Tipo: Standard\n";
    }

    @Override
    public JSONObject toJson() {
        JSONObject habitacion = super.toJson();
        habitacion.put("tipoHabitacion", "standard");
        return habitacion;
    }

}
