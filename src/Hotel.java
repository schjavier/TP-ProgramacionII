import java.util.ArrayList;
import java.util.HashMap;

public class Hotel { // ESTO ES EL WRAPPER CLASS
    ArrayList<Integer> pasajeros = new ArrayList<>();
    Habitacion habitacion1 = new Habitacion(12,4,EstadoHabitacion.DISPONIBLE,pasajeros);
    Habitacion habitacion2 = new Habitacion(15,4,EstadoHabitacion.OCUPADA,pasajeros);
    Habitacion habitacion3 = new Habitacion(11,4,EstadoHabitacion.OCUPADA,pasajeros);
    Habitacion habitacion4 = new Habitacion(18,4,EstadoHabitacion.LIMPIEZA,pasajeros);
    Habitacion habitacion5 = new Habitacion(9,4,EstadoHabitacion.DISPONIBLE,pasajeros);

    Habitaciones<Habitacion> habitaciones = new Habitaciones<>("test");

    public Hotel() { // test
        habitaciones.agregarHabitacion(habitacion1);
        habitaciones.agregarHabitacion(habitacion2);
        habitaciones.agregarHabitacion(habitacion3);
        habitaciones.agregarHabitacion(habitacion4);
        habitaciones.agregarHabitacion(habitacion5);
    }

    public HashMap contarEstadoHabitaciones(int tipohabitacion)
    {
        return selectorDeTipoHabitacion(tipohabitacion).contarCantidadHabitacionesSegunEstado();
    }


    public StringBuilder listarHabitaciones(int tipohabitacion)
    {
        return selectorDeTipoHabitacion(tipohabitacion).listarTodos();
    }

    public boolean eliminarHabitacion(int tipohabitacion,int habitacion)
    {
        return selectorDeTipoHabitacion(tipohabitacion).eliminarSegunNumeroHabitacion(habitacion);
    }


    /**
     *
     * @param tipohabitacion Un numero del 1 al x siendo x el ultimo tipo de habitacion que haya
     * @return La lista que en la que se trabajara
     * @throws BadOption En el caso de que el id de tipo habitacion (lista) no este dentro del switch
     */
    public Habitaciones selectorDeTipoHabitacion(int tipohabitacion) throws BadOption
    {
        return switch (tipohabitacion) {
            case 1 -> habitaciones;
            default -> throw new BadOption("Elegir una opcion correcta!!");
        };
    }

    public StringBuilder listarSegunEstado(int tipohabitacion,EstadoHabitacion estado)
    {
        return selectorDeTipoHabitacion(tipohabitacion).listarTodosSegunEstado(estado);
    }




}
