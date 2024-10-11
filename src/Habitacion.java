import java.util.ArrayList;

/* abstract */ public class Habitacion {
    private final int nroHabitacion; // esto deberia ser unico pero ni idea
    private final int capacidadMaxima;
    private EstadoHabitacion estado;
    private ArrayList<Integer> ocupantes;

    public Habitacion(int nroHabitacion, int capacidadMaxima, EstadoHabitacion estado, ArrayList<Integer> ocupantes) { // Creo que no tiene mucho sentido instanciar una habitacion con sus ocupantes de una
        this.nroHabitacion = nroHabitacion;
        this.capacidadMaxima = capacidadMaxima;
        this.estado = estado;
        this.ocupantes = ocupantes;
    }

    public Habitacion(int nroHabitacion, int capacidadMaxima, ArrayList<Integer> ocupantes) { // Creo que no tiene mucho sentido instanciar una habitacion con sus ocupantes de una
        this.nroHabitacion = nroHabitacion;
        this.capacidadMaxima = capacidadMaxima;
        this.estado = EstadoHabitacion.DISPONIBLE;
        this.ocupantes = ocupantes;
    }

    public Habitacion(int nroHabitacion, int capacidadMaxima) {
        this.nroHabitacion = nroHabitacion;
        this.capacidadMaxima = capacidadMaxima;
        this.estado = EstadoHabitacion.DISPONIBLE;
        this.ocupantes = new ArrayList<Integer>();
    }

    public EstadoHabitacion getEstado() {
        return estado;
    }

    public void setEstado(EstadoHabitacion estado) {
        this.estado = estado;
    }

    public ArrayList<Integer> getOcupantes() {
        return ocupantes;
    }

    public void setOcupantes(ArrayList<Integer> ocupantes) {
        this.ocupantes = ocupantes;
    }

    public int getNroHabitacion() {
        return nroHabitacion;
    }

    public int getCapacidadMaxima() {
        return capacidadMaxima;
    }

    @Override
    public String toString() {
        return "Habitacion{" +
                "nroHabitacion=" + nroHabitacion +
                ", capacidadMaxima=" + capacidadMaxima +
                ", estado=" + estado +
                ", ocupantes=" + ocupantes +
                '}';
    }
}
