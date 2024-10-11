import java.time.LocalDate;
import java.util.ArrayList;

// en gral creo que podrian ponerse algunos atributos finales

public class Reserva {
    private static int contadorIdReserva = 0; // id auto incremental
    private int id;
    private int referente;
    private ArrayList<Integer> pasajeros;
    private boolean activa = false; // porque activa?
    private int habitacion;
    private LocalDate fechaInicio;
    private LocalDate fechaFinal;
    private int guardadoPor;

    public Reserva(int referente, ArrayList<Integer> pasajeros, boolean activa, int habitacion, LocalDate fechaInicio, LocalDate fechaFinal, int guardadoPor) {
        this.id = ++contadorIdReserva;
        this.referente = referente;
        this.pasajeros = pasajeros;
        this.activa = activa;
        this.habitacion = habitacion;
        this.fechaInicio = fechaInicio;
        this.fechaFinal = fechaFinal;
        this.guardadoPor = guardadoPor;
    }

    public static int getContadorIdReserva() {
        return contadorIdReserva;
    }

    public int getId() {
        return id;
    }

    public int getReferente() {
        return referente;
    }

    public void setReferente(int referente) {
        this.referente = referente;
    }

    public ArrayList<Integer> getPasajeros() {
        return pasajeros;
    }

    public void setPasajeros(ArrayList<Integer> pasajeros) {
        this.pasajeros = pasajeros; // aca podria ponerse una excepcion si la reserva no esta activa
    }

    public int getHabitacion() {
        return habitacion;
    }

    public void setHabitacion(int habitacion) {
        this.habitacion = habitacion; // aca podria ponerse una excepcion si la reserva no esta activa
    }

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio; // aca podria ponerse una excepcion
    }

    public LocalDate getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(LocalDate fechaFinal) {
        this.fechaFinal = fechaFinal; // aca podria ponerse una excepcion
    }
}
