package Modelo.Reserva;

import Modelo.Persona.Pasajero;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Clase que representa una Reserva de habitacion en el sistema
 */

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

    public int getHabitacion() {
        return habitacion;
    }

    public void setHabitacion(int habitacion) {
        this.habitacion = habitacion;
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

    public LocalDate getFechaFinal() {
        return fechaFinal;
    }

    /**
     * Metodo que permite agregar personas a la reserva
     * @param dni un {@code int} que representa el numero de dodumento de la persona
     */
    public void agregarPersonaAReserva(Integer dni)
    {
        pasajeros.add(dni);
    }

    /**
     * Metodo que devueve la cantidad de personas en la reserva
     * @return un {@code int} que represenra la cantidad de personas
     */
    public int getCantidadPersonasEnReserva()
    {
        return pasajeros.size();
    }

    /**
     * Metodo que asigna una habitacion a la reserva
     * @param numhabitacion un {@code int} que representa el numero de habitacion
     */
    public void asignarHabitacionAReservaYLlenarDatosFaltantes(int numhabitacion)
    {
        this.id = ++contadorIdReserva;
        setHabitacion(numhabitacion);
    }

    @Override
    public String toString() {
        return "Reserva: " + id + "\n" +
                "DNI titular: " + dniTitular + "\n" +
                "DNIs pasajeros: " + pasajeros + "\n" +
                "Activa? " + activa + "\n" +
                "Nro. habitacion: " + habitacion + "\n" +
                "Fecha de inicio: " + fechaInicio + "\n" +
                "Fecha de finalizacion: " + fechaFinal + "\n";
    }

    /**
     * Metodo que comvierte una {@code Reserva} en un JSON
     * @return devuelve un {@code JSONObject} que representa la reserva.
     */

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
