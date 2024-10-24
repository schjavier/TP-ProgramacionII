package Modelo.Habitaciones;

import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HabitacionPresidencial extends Habitacion implements TieneCocina {
    private LocalDateTime ultimaRevisionCocina;
    private LocalDateTime ultimaRevisionJacuzzi;

    public HabitacionPresidencial(int capacidadMaxima) {
        super(capacidadMaxima);
        this.ultimaRevisionCocina = LocalDateTime.now();
        this.ultimaRevisionJacuzzi = LocalDateTime.now();
    }

    @Override
    public void marcarMantenimientoHechoEnCocina() {
        this.ultimaRevisionCocina = LocalDateTime.now();
    }

    public void marcarMantenimientoEnJacuzzi() {
        this.ultimaRevisionJacuzzi = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return super.toString() + "Tipo: Presidencial\n" +
                "Última revisión de la cocina: " + ultimaRevisionCocina.toLocalDate() + "\n" +
                "Última revisión del jacuzzi: " + ultimaRevisionJacuzzi.toLocalDate() + "\n";
    }

    @Override
    public JSONObject toJson() {
        JSONObject habitacion = super.toJson();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        habitacion.put("tipoHabitacion", "presidencial");
        habitacion.put("ultimaRevisionCocina", ultimaRevisionCocina.format(formatter));
        habitacion.put("ultimaRevisionJacuzzi", ultimaRevisionJacuzzi.format(formatter));
        return habitacion;
    }
}
