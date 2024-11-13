package DataChecks;

import Exceptions.BadDataException;
import Modelo.Reserva.Reserva;
import java.time.LocalDate;

/**
 * Clase final, que contiene metodos para hacer verificaciones de ditinto tipo de datos.
 */

public final class VerificacionesDeDatos {

    /**
     * Hace revisiones de datos para verificar su validez, maneja nombres y apellidos, dni y fechas de reservas.<p>
     * Esto no se debe instanciar, todos los metodos son estaticos.
     */
    private VerificacionesDeDatos() {}

    /**
     * Verifica si el dni es valido.
     * @param dni El dni de la persona cargado por teclado. (No verifica si tiene letras, no es necesario en este caso).
     * @return Un booleano Siempre es true si es correcto, por si se necesita poner esto en un if.
     * @throws BadDataException Si el dni no tiene 8 caracteres, se lanza un mensaje segun el problema.
     */
    static public boolean verificarDni(int dni) throws BadDataException {
        boolean result = true;
        String err = "";

        String dnicheck = String.valueOf(dni);

        if (dnicheck.length() != 8) {
            result = false;
            err = err.concat("Ese dni no es valido porque:\n");
            if (dnicheck.length() < 8) {
                err = err.concat("- El dni es muy corto!");
            } else {
                err = err.concat("- El dni es muy largo!");
            }


            throw new BadDataException(err);
        }
        return result;
    }

    /**
     * Verifica si el dni es valido.
     * @param dni El dni de la persona cargado por teclado. (No verifica si tiene letras, no es necesario en este caso).
     * @return Un booleano Siempre es true si es correcto, por si se necesita poner esto en un if.
     * @throws BadDataException Si el dni no tiene 8 caracteres, se lanza un mensaje segun el problema.
     */
    static public boolean verificarDni(Integer dni) throws BadDataException {
        boolean result = true;
        String err = "";

        String dnicheck = dni.toString();

        if (dnicheck.length() != 8) {
            result = false;
            err = err.concat("Ese dni no es valido porque:\n");
            if (dnicheck.length() < 8) {
                err = err.concat("- El dni es muy corto!");
            } else {
                err = err.concat("- El dni es muy largo!");
            }

            throw new BadDataException(err);
        }
        return result;
    }

    /**
     * Revisa si un {@code String} no tiene numeros.
     * @param palabra Palabra representa un texto que no deberia tener numeros.
     * @return Un booleano que siempre retorna true por si enecesita poner esto en un if.
     * @throws BadDataException Si la palabra tiene un numero, se lanza esto.
     */
    static public boolean tieneNumeros(String palabra) throws BadDataException {
        if (palabra.matches(".*\\d.*")) {
            throw new BadDataException("El texto introducido no debe tener numeros");
        }
        return true;
    }

    /**
     * Revisa si un {@code String} tiene numeros.
     * @param numeros Numeros representa un texto que contener únicamente numeros para alimentar un Integer.parseInt().
     * @return Un booleano que siempre retorna true por si necesita poner esto en un if.
     * @throws BadDataException Si la palabra contiene cualquier otra cosa que no sea un numero.
     */
    static public boolean esSoloNumeros(String numeros) throws BadDataException {
        if (!numeros.matches("[0-9]+")) {
            throw new BadDataException("El texto contiene letras o está vacío");
        }
        return true;
    }

    /**
     * Revisa si el {@code intento} de una reserva está dentro del periodo de tiempo de una {@code guardada}.
     * @param intento Representa una reserva que se esta cargando en el sistema.
     * @param guardada Representa una reserva ya cargada en el sistema (que está por suceder, o sucediendo).
     * @return Retorna true si la reserva {@code intento} sucede dentro de una {@code guardada}.
     */
    public static boolean intentoReservaEstaDentroDeGuardada(Reserva intento, Reserva guardada) //
    {
        boolean respuesta = false;
        if(intento.getHabitacion() == guardada.getHabitacion())
        {
            if(intento.getFechaInicio().isAfter(guardada.getFechaInicio()) && intento.getFechaFinal().isBefore(guardada.getFechaFinal()))
            {
                respuesta = true;
            }
        }
        return respuesta;
    }


    /**
     * Revisa si el {@code intento} de una reserva esta conteniendo una reserva {@code guardada}.
     * @param intento Representa una reserva que se esta cargando en el sistema.
     * @param guardada Representa una reserva ya cargada en el sistema (que está por suceder, o sucediendo).
     * @return Retorna true si la reserva {@code intento} contiene una reserva {@code guardada}.
     */
    public static boolean intentoReservaContieneUnaReservaGuardada(Reserva intento, Reserva guardada)
    {
        boolean respuesta = false;
        if(intento.getFechaInicio().isBefore(guardada.getFechaInicio()) && intento.getFechaFinal().isAfter(guardada.getFechaFinal()))
        {
            respuesta = true;
        }
        return respuesta;
    }

    /**
     * Revisa si una reserva {@code intento} tiene alguna intersección en sus fechas con una reserva {@code guardada}
     * @param intento Representa una reserva que se esta cargando en el sistema.
     * @param guardada Representa una reserva ya cargada en el sistema (que está por suceder, o sucediendo).
     * @return Retorna true si hay alguna interseción entre las dos.
     */
    public static boolean intentoReservaInterseccionaConUnaGuardada(Reserva intento, Reserva guardada)
    {
        boolean respuesta = false;
        if((intento.getFechaInicio().isBefore(guardada.getFechaInicio()) && intento.getFechaFinal().isAfter(guardada.getFechaInicio())) || (intento.getFechaInicio().isBefore(guardada.getFechaFinal()) && (intento.getFechaFinal().isAfter(guardada.getFechaFinal()))))
        {
            respuesta = true;
        }
        return respuesta;
    }


    /**
     * Revisa si a una reserva esta por suceder (o está sucediendo).
     * @param guardada representa una reserva guardada.
     * @return Retorna true si el final de la reserva es antes del dia de hoy.
     */
    public static boolean reservaEsActiva(Reserva guardada)
    {
        boolean esActiva = false;
        LocalDate hoy = LocalDate.now();

        if(hoy.isBefore(guardada.getFechaFinal()))
        {
            esActiva = true;
        }

        return esActiva;
    }

    /**
     * Revisa Si la reserva esta pasando ahora.
     * @param guardada Representa la reserva guardada.
     * @return Retrona true si está esta pasando ahora.
     */
    public static boolean reservaEstaPasandoAhora(Reserva guardada)
    {
        boolean pasandoAhora = false;
        LocalDate hoy = LocalDate.now();

        if(hoy.isAfter(guardada.getFechaInicio()) && hoy.isBefore(guardada.getFechaFinal()))
        {
            pasandoAhora = true;
        }

        return pasandoAhora;
    }


    /**
     * Revisa se una reserva es coherente.
     * @param intento Representa una reserva que se esta cargando en el sistema.
     * @return Siempre retorna true por si acaso si se necesita usar en un if.
     * @throws BadDataException Lanza una excepcion en el caso de que la reserva este mal.
     */
    public static boolean fechaTieneSentido(Reserva intento) throws BadDataException {
        boolean fechaOk = true;

        LocalDate inicio = intento.getFechaInicio();
        LocalDate finalreserva = intento.getFechaFinal();

        if (inicio.isAfter(finalreserva))
        {
            throw new BadDataException("La fecha de inicio no puede ser despues del final");
        }

        if (finalreserva.isBefore(inicio))
        {
            throw new BadDataException("La fecha de final no puede ser despues del inicio");
        }


        return true;
    }

    /**
     * Metodo que verifica que la fecha pasada por parametro es antes de hoy
     * @param intento la fecha a evaluar
     * @return {@code true} si es antes, {@code false} de otra forma
     */
    public static boolean fechaAntesDeHoy(LocalDate intento)
    {
        boolean antesDeHoy = false;
        if(intento.isBefore(LocalDate.now()))
        {
            antesDeHoy = true;
        }
        return antesDeHoy;
    }
}
