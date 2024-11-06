package Modelo.Reserva;

import Modelo.Persona.Pasajero;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

// en gral creo que podrian ponerse algunos atributos finales

public class Reserva {
    private static int contadorIdReserva = 0; // id auto incremental
    private int id;
    private int dniTitular;
    private ArrayList<Integer> pasajeros;
    private boolean activa;
    private int habitacion;
    private LocalDate fechaInicio;
    private LocalDate fechaFinal;
    private int guardadoPor; // empleado id

    public Reserva(int dniTitular, int habitacion, LocalDate fechaInicio, LocalDate fechaFinal, int guardadoPor) {
        this.id = ++contadorIdReserva;
        this.dniTitular = dniTitular;
        this.pasajeros = new ArrayList<>();
        pasajeros.add(dniTitular);
        this.activa = true;
        this.habitacion = habitacion;
        this.fechaInicio = fechaInicio;
        this.fechaFinal = fechaFinal;
        this.guardadoPor = guardadoPor;
    }

    public Reserva(int dniTitular, LocalDate fechaInicio, LocalDate fechaFinal, int guardadoPor)
    {
        this.dniTitular = dniTitular;
        this.pasajeros = new ArrayList<>();
        pasajeros.add(dniTitular);
        this.fechaInicio = fechaInicio;
        this.fechaFinal = fechaFinal;
        this.guardadoPor = guardadoPor;
    }

    // en la clase reserva agregarmos los pasajeros, con un metodo para eso.

    public int getId() {
        return id;
    }

    public int getDniTitular() {
        return dniTitular;
    }

    public void setDniTitular(int dniTitular) {
        this.dniTitular = dniTitular;
    }

    public ArrayList<Integer> getPasajeros() {
        return pasajeros;
    }

    public void agregarPasajero(int dniPasajero){
        this.pasajeros.add(dniPasajero);
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

    public void agregarPersonaAReserva(Integer dni)
    {
        pasajeros.add(dni);
    }

    public int getCantidadPersonasEnReserva()
    {
        return pasajeros.size();
    }

    public void asignarHabitacionAReservaYLlenarDatosFaltantes(int numhabitacion)
    {
        this.id = ++contadorIdReserva;
        setHabitacion(numhabitacion);
    }

    @Override
    public String toString() {
        return "Reserva {" +
                "id=" + id +
                ", dniTitular=" + dniTitular +
                ", pasajeros=" + pasajeros +
                ", activa=" + activa +
                ", habitacion=" + habitacion +
                ", fechaInicio=" + fechaInicio +
                ", fechaFinal=" + fechaFinal +
                '}';
    }

    public JSONObject toJson(){
        JSONObject reserva = new JSONObject();
        JSONArray dniPasajeros = new JSONArray();

        reserva.put("id", this.id);
        reserva.put("dniTitular", this.dniTitular);

        for (Integer dniPasajero : this.pasajeros){
            dniPasajeros.put(dniPasajero);
        }
        reserva.put("pasajeros", dniPasajeros);
        reserva.put("activa", this.activa);
        reserva.put("habitacion", this.habitacion);
        reserva.put("fechaInicio", this.fechaInicio);
        reserva.put("fechaFinal", this.fechaFinal);
        reserva.put("guardadoPor", this.guardadoPor);

        return reserva;
    }

}
