package Modelo.Habitaciones;

import Exceptions.HabitacionNoEncontradaException;

import java.util.ArrayList;

public class HabitacionesStandard extends Habitaciones<HabitacionStandard>{
    public HabitacionesStandard() {
        super(TipoHabitacion.REGULAR);
    }

    @Override
    public String contarCantidadHabitacionesSegunEstado() {
        return super.contarCantidadHabitacionesSegunEstado();
    }

    @Override
    public boolean verSiElNumeroEstaDisponible(int numhabitacion) {
        return super.verSiElNumeroEstaDisponible(numhabitacion);
    }

    @Override
    public HabitacionStandard traerHabitacionSegunId(int numHabitacion) throws HabitacionNoEncontradaException {
        return super.traerHabitacionSegunId(numHabitacion);
    }

    @Override
    public void asignarEstadoAtodas(EstadoHabitacion estado) {
        super.asignarEstadoAtodas(estado);
    }

    @Override
    public StringBuilder listarHabitacionesSegunEstado(EstadoHabitacion estado) {
        return super.listarHabitacionesSegunEstado(estado);
    }

    @Override
    public StringBuilder listarHabitaciones() {
        return super.listarHabitaciones();
    }

    @Override
    public boolean eliminarHabitacionSegunNumero(int numHabitacion) {
        return super.eliminarHabitacionSegunNumero(numHabitacion);
    }

    @Override
    public void agregarHabitacion(HabitacionStandard habitacion) {
        super.agregarHabitacion(habitacion);
    }

    @Override
    public ArrayList<HabitacionStandard> getListaHabitaciones() {
        return super.getListaHabitaciones();
    }
}
