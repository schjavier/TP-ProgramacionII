package Exceptions;

/**
 * Si una reserva ya existente va a exceder la capacidad de una habitacion, se lanzaria
 */
public class MuchasPersonasException extends RuntimeException {
    public MuchasPersonasException(String message) {
        super(message);
    }
}
