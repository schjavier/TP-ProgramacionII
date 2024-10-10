import java.util.ArrayList;

public class Habitacion {
    private final int nroHabitacion; // esto deberia ser unico pero ni idea
    private final int capacidadMaxima;
    private EstadoHabitacion estado;
    private ArrayList<Integer> ocupantes;

    public Habitacion(int nroHabitacion, int capacidadMaxima, EstadoHabitacion estado, ArrayList<Integer> ocupantes) {
        this.nroHabitacion = nroHabitacion;
        this.capacidadMaxima = capacidadMaxima;
        this.estado = estado;
        this.ocupantes = ocupantes;
    }

    public Habitacion(int nroHabitacion, int capacidadMaxima, ArrayList<Integer> ocupantes) {
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
}
