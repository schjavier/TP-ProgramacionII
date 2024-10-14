package Modelo.Reserva;

import java.time.LocalDate;
import java.util.ArrayList;


public class ReservaService {

    ArrayList<Reserva> historialReservas;

    public ReservaService(){
        historialReservas = new ArrayList<>();
    }

    public boolean generarReserva(int dniTitular, boolean activa, int habitacion, LocalDate fechaInicio, LocalDate fechaFinal, int guardadoPor){
        boolean respuesta = false;
        Reserva reserva = new Reserva(dniTitular, activa, habitacion, fechaInicio, fechaFinal, guardadoPor);

        if (historialReservas.isEmpty()){
            historialReservas.add(reserva);
            respuesta = true;
        } else if (!buscarReservaActivaPorTitular(dniTitular)){
            historialReservas.add(reserva);
            respuesta = true;
        }
        return respuesta;
    }

    public boolean generarReserva(Reserva reserva){
        boolean respuesta = false;
        if (historialReservas.isEmpty()){
            historialReservas.add(reserva);
            respuesta = true;
        } else if (!buscarReservaActivaPorTitular(reserva.getDniTitular())){
            historialReservas.add(reserva);
            respuesta = true;
        }
        return respuesta;
    }

    /**
     *
     * Busca reservas por dni.
     *
     * @param dni El dni del titular de la reserva
     * @return true, si se encontro al menos una reserva con el dni asociado, false en otro caso.
     */
    public boolean buscarReservaPorTitular(int dni){
        boolean respuesta = false;

        for (Reserva reserva : historialReservas){
            if (reserva.getDniTitular() == dni){
                respuesta = true;
            }
        }
        return respuesta;
    }

    /**
     * Busca reservas activas por el dni del titular de la reservas.
     *
     * @param dni El dni del titular de la reserva
     * @return True si encuentra una reserva activa con el ese dni, false en cualquier otro caso.
     */

    public boolean buscarReservaActivaPorTitular(int dni){
        boolean respuesta = false;
        for (Reserva reserva : historialReservas){
            if (reserva.getDniTitular() == dni && reserva.isActiva()){
                respuesta = true;
            }
        }
        return respuesta;
    }

    /**
     *
     * Muestra un historico de reservas filtrado por el dni del titular
     *
     * @param dni El dni del titular de la reserva
     * @return Un String con la informacion de las reservas que tuvo el titular a lo largo del tiempo.
     */
    public String historicoPorTitular(int dni){
        StringBuilder respuesta = new StringBuilder("Reserva - Fecha Inicio - Fecha Final - Habitacion").append("\n");

        for (Reserva reserva : historialReservas){
            if (reserva.getDniTitular() == dni){
                respuesta.append(reserva.getId())
                        .append(reserva.getFechaInicio())
                        .append(reserva.getFechaFinal())
                        .append(reserva.getHabitacion())
                        .append("\n");
            }
        }

        return respuesta.toString();
    }

    public ArrayList<Reserva> filtrarPorActivas(){
        ArrayList<Reserva> reservasActivas = new ArrayList<>();
        for (Reserva reserva : historialReservas){
            if (reserva.isActiva()){
                reservasActivas.add(reserva);
            }
        }
        return reservasActivas;
    }

}
