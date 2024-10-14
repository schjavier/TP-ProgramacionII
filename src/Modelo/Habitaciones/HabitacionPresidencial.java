package Modelo.Habitaciones;

import java.time.LocalDateTime;

public class HabitacionPresidencial extends Habitacion implements TieneCocina {
    private LocalDateTime ultimarevisioncocina;
    private LocalDateTime ultimarevisionjacuzzi;

    public HabitacionPresidencial(int capacidadMaxima) {
        super(capacidadMaxima,TipoHabitacion.PRESIDENCIAL);
        this.ultimarevisioncocina = LocalDateTime.now();
        this.ultimarevisionjacuzzi = LocalDateTime.now();
    }

    @Override
    public void marcarMantenimientoHechoEnCocina() {
        this.ultimarevisioncocina = LocalDateTime.now();
    }

    public void marcarMantenimientoEnJacuzzi()
    {
        this.ultimarevisionjacuzzi = LocalDateTime.now();
    }

}
