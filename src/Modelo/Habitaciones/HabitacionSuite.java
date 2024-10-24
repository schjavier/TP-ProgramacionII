package Modelo.Habitaciones;

import java.time.LocalDateTime;

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
}
