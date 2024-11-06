package Modelo.Habitaciones;

import java.time.LocalDateTime;

public class HabitacionSuite extends Habitacion implements TieneCocina {
    private LocalDateTime ultimaRevisionCocina;

    public HabitacionSuite(int capacidadMaxima) {
        super(capacidadMaxima,TipoHabitacion.SUITE);
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
        return "Habitacion Suite: \n" + super.toString() +
                "Ultima revision cocina=" + ultimaRevisionCocina +
                '}';
    }
}
