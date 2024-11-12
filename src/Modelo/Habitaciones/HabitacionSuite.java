package Modelo.Habitaciones;

import org.json.JSONObject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class HabitacionSuite extends Habitacion implements TieneCocina {
    private LocalDate ultimaRevisionCocina;

    public HabitacionSuite(int nroHabitacion, int capacidadMaxima, EstadoHabitacion estado, LocalDate ultimaRevisionCocina) {
        super(nroHabitacion, capacidadMaxima, estado);
        this.ultimaRevisionCocina = ultimaRevisionCocina;
    }

    public HabitacionSuite(int capacidadMaxima) {
        super(capacidadMaxima);
        this.ultimaRevisionCocina = LocalDate.now();
    }

    @Override
    public void marcarMantenimientoHechoEnCocina() {
        this.ultimaRevisionCocina = LocalDate.now();
    }


    @Override
    public String toString() {
        return super.toString() + "Tipo: Suite\n" +
                "Ultima revisión de la cocina: " + ultimaRevisionCocina + "\n";
    }

    @Override
    public JSONObject toJson() {
        JSONObject habitacion = super.toJson();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        habitacion.put("tipoHabitacion", TipoHabitacion.SUITE);
        habitacion.put("ultimaRevisionCocina", ultimaRevisionCocina.format(formatter));
        return habitacion;
    }
}
