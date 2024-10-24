package Modelo.Habitaciones;

import org.json.JSONObject;

public class HabitacionStandard extends Habitacion { // la dejo vacia asi cuando se arregle esto de los pasajeros lo arranco

    public HabitacionStandard(int capacidadMaxima) {
        super(capacidadMaxima);
    }

    @Override
    public String toString() {
        return super.toString() + "Tipo: Standard\n";
    }

    //    public JSONObject toJSON() {
//        JSONObject habitacion = new JSONObject();
//        habitacion.put("")
//
//        return habitacion;
//    }

}
