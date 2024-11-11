package Modelo.Habitaciones;

import org.json.JSONObject;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class HabitacionPresidencial extends Habitacion implements TieneCocina {
    private LocalDate ultimaRevisionCocina;
    private LocalDate ultimaRevisionJacuzzi;

    public HabitacionPresidencial(int nroHabitacion, int capacidadMaxima, EstadoHabitacion estado, LocalDate ultimaRevisionCocina, LocalDate ultimaRevisionJacuzzi) {
        super(nroHabitacion, capacidadMaxima, estado);
        this.ultimaRevisionCocina = ultimaRevisionCocina;
        this.ultimaRevisionJacuzzi = ultimaRevisionJacuzzi;
    }

    public HabitacionPresidencial(int capacidadMaxima) {
        super(capacidadMaxima);
        this.ultimaRevisionCocina = LocalDate.now();
        this.ultimaRevisionJacuzzi = LocalDate.now();
    }

    @Override
    public void marcarMantenimientoHechoEnCocina() {
        this.ultimaRevisionCocina = LocalDate.now();
    }

    public void marcarMantenimientoEnJacuzzi() {
        this.ultimaRevisionJacuzzi = LocalDate.now();
    }

    @Override
    public String toString() {
        return super.toString() + "Tipo: Presidencial\n" +
                "Última revisión de la cocina: " + ultimaRevisionCocina + "\n" +
                "Última revisión del jacuzzi: " + ultimaRevisionJacuzzi + "\n";
    }

    @Override
    public JSONObject toJson() {
        JSONObject habitacion = super.toJson();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        habitacion.put("tipoHabitacion", TipoHabitacion.PRESIDENCIAL);
        habitacion.put("ultimaRevisionCocina", ultimaRevisionCocina.format(formatter));
        habitacion.put("ultimaRevisionJacuzzi", ultimaRevisionJacuzzi.format(formatter));
        return habitacion;
    }
}
