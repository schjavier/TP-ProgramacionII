import java.util.ArrayList;
import java.util.HashMap;

public class Habitaciones<T extends Habitacion>{
    ArrayList<T> lista = new ArrayList<>();
    String tipohabitacion;

    public Habitaciones(String tipohabitacion) {
        this.tipohabitacion = tipohabitacion;
    }

    public void agregarHabitacion(T habitacion)
    {
        lista.add(habitacion);
    }

    public boolean eliminarSegunNumeroHabitacion(int numhabitacion)
    {
        return lista.removeIf(habitacion -> habitacion.getNroHabitacion() == numhabitacion);
        // estaria bueno borrar reservas hechas de esa habitacion (aca no)
    }

    public StringBuilder listarTodos()
    {
        StringBuilder todos = new StringBuilder();
        for(T habitacion : lista)
        {
            todos.append(habitacion.toString() + "\n");
        }
        return todos;
    }

    public StringBuilder listarTodosSegunEstado(EstadoHabitacion estado)
    {
        StringBuilder todos = new StringBuilder();

        for(T habitacion : lista)
        {
            if(habitacion.getEstado() == estado)
            {
                todos.append(habitacion + "\n");
            }
        }

        return todos;
    }

    public void asignarEstadoAtodas(EstadoHabitacion estado)
    {
        for(T habitacion : lista)
        {
            habitacion.setEstado(estado);
        }
    }

    public T traerHabitacionSegunId(int numhabitacion) throws HabitacionNoEncontradaException
    {
        T room = null;
        for(T habitacion : lista)
        {
            if(habitacion.getNroHabitacion() == numhabitacion)
            {
                room = habitacion;
            }
        }

        if(room == null)
        {
            throw new HabitacionNoEncontradaException("No hay ninguna habitación con ese numero");
        }

        return room;
    }

    public boolean verSiElNumeroEstaDisponible(int numhabitacion) // esto es para hacer que sean unicas (creo que puede ser un static id, haciendo esto innecesario)
    {
        boolean respuesta = true;
        for(T habitacion : lista)
        {
            if(habitacion.getNroHabitacion() == numhabitacion)
            {
                respuesta = false;
                break;
            }
        }
        return respuesta;
    }

    public HashMap<EstadoHabitacion,Integer> contarCantidadHabitacionesSegunEstado()
    {
        HashMap<EstadoHabitacion,Integer> cantidad = new HashMap<>();

        for(EstadoHabitacion valor : EstadoHabitacion.values())
        {
            cantidad.put(valor,0);
        }

        for(T habitacion : lista)
        {
            EstadoHabitacion estado = habitacion.getEstado();
            cantidad.put(estado, cantidad.get(estado) + 1);
        }

        return cantidad;
    }


}
