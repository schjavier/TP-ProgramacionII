package Modelo.Habitaciones;

import java.time.LocalDateTime;

public class HabitacionPresidencial extends Habitacion implements TieneCocina {
    private LocalDateTime ultimaRevisionCocina;
    private LocalDateTime ultimaRevisionJacuzzi;

    public HabitacionPresidencial(int capacidadMaxima) {
        super(capacidadMaxima,TipoHabitacion.PRESIDENCIAL);
        this.ultimaRevisionCocina = LocalDateTime.now();
        this.ultimaRevisionJacuzzi = LocalDateTime.now();
    }

    @Override
    public void marcarMantenimientoHechoEnCocina() {
        this.ultimaRevisionCocina = LocalDateTime.now();
    }

    public void marcarMantenimientoEnJacuzzi()
    {
        this.ultimaRevisionJacuzzi = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Habitacion Presidencial: \n" + super.toString() +
                "Ultima revision cocina=" + ultimaRevisionCocina +
                ", Ultima revision jacuzzi=" + ultimaRevisionJacuzzi +
                '}';
    }
}
