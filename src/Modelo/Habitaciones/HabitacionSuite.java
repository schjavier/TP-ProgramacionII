package Modelo.Habitaciones;

import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HabitacionSuite extends Habitacion implements TieneCocina {
    private LocalDateTime ultimaRevisionCocina;

    public HabitacionSuite(int capacidadMaxima) {
        super(capacidadMaxima);
        this.ultimaRevisionCocina = LocalDateTime.now();
    }


    public LocalDateTime getUltimaRevisionCocina() {
        return ultimaRevisionCocina;
    }

    @Override
    public void marcarMantenimientoHechoEnCocina() {
        this.ultimaRevisionCocina = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return super.toString() + "Tipo: Suite\n" +
                "Ultima revisión de la cocina: " + ultimaRevisionCocina.toLocalDate() + "\n";
    }

    @Override
    public JSONObject toJson() {
        JSONObject habitacion = super.toJson();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        habitacion.put("tipoHabitacion", "suite");
        habitacion.put("ultimaRevisionCocina", ultimaRevisionCocina.format(formatter));
        return habitacion;
    }
}
