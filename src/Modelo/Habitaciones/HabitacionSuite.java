package Modelo.Habitaciones;

import java.time.LocalDateTime;

public class HabitacionSuite extends Habitacion implements TieneCocina {
    private LocalDateTime ultimarevisioncocina;

    public HabitacionSuite(int capacidadMaxima) {
        super(capacidadMaxima,TipoHabitacion.SUITE);
    }


    public LocalDateTime getUltimarevisioncocina() {
        return ultimarevisioncocina;
    }

    @Override
    public void marcarMantenimientoHechoEnCocina() {
        this.ultimarevisioncocina = LocalDateTime.now();
    }
}
