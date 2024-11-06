package Exceptions;

/**
 * Representa un error en cargar datos, ya sea un dni, un nombre, fecha reserva que no tiene sentido, etc
 */
public class BadDataException extends Exception {
    public BadDataException(String message) {
        super(message);
    }
}
