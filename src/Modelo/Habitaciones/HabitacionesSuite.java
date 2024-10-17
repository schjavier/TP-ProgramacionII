package Modelo.Habitaciones;

import Exceptions.HabitacionNoEncontradaException;

import java.util.ArrayList;

public class HabitacionesSuite extends Habitaciones<HabitacionSuite> {
    public HabitacionesSuite() {
        super(TipoHabitacion.SUITE);
    }

    @Override
    public ArrayList<HabitacionSuite> getListaHabitaciones() {
        return super.getListaHabitaciones();
    }

    @Override
    public void agregarHabitacion(HabitacionSuite habitacion) {
        super.agregarHabitacion(habitacion);
    }

    @Override
    public boolean eliminarHabitacionSegunNumero(int numHabitacion) {
        return super.eliminarHabitacionSegunNumero(numHabitacion);
    }

    @Override
    public StringBuilder listarHabitaciones() {
        return super.listarHabitaciones();
    }

    @Override
    public StringBuilder listarHabitacionesSegunEstado(EstadoHabitacion estado) {
        return super.listarHabitacionesSegunEstado(estado);
    }

    @Override
    public void asignarEstadoAtodas(EstadoHabitacion estado) {
        super.asignarEstadoAtodas(estado);
    }

    @Override
    public HabitacionSuite traerHabitacionSegunId(int numHabitacion) throws HabitacionNoEncontradaException {
        return super.traerHabitacionSegunId(numHabitacion);
    }

    @Override
    public boolean verSiElNumeroEstaDisponible(int numhabitacion) {
        return super.verSiElNumeroEstaDisponible(numhabitacion);
    }

    @Override
    public String contarCantidadHabitacionesSegunEstado() {
        return super.contarCantidadHabitacionesSegunEstado();
    }

    public void marcarTodasHabitacionesComoRevisadas()
    {
        for (HabitacionSuite habitacion : super.getListaHabitaciones())
        {
            habitacion.marcarMantenimientoHechoEnCocina();
        }
    }
}
