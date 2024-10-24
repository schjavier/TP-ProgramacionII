package Modelo.Habitaciones;

import java.time.LocalDateTime;

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
}
