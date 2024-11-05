package Modelo.Reserva;

import Modelo.Persona.Pasajero;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

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

        reserva.put("Id", this.id);
        reserva.put("DniTitular", this.dniTitular);

        for (Integer dniPasajero : this.pasajeros){
            dniPasajeros.put(dniPasajero);
        }
        reserva.put("Pasajeros", dniPasajeros);
        reserva.put("Activa", this.activa);
        reserva.put("Habitacion", this.habitacion);
        reserva.put("FechaInicio", this.fechaInicio);
        reserva.put("FechaFinal", this.fechaFinal);
        reserva.put("GuardadoPor", this.guardadoPor);

        return reserva;
    }

}
