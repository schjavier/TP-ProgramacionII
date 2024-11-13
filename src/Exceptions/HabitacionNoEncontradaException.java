package Exceptions;

/**
 * Esta excepcion es lanzada cuando no se encunetran habitaciones.
 *
 */
public class HabitacionNoEncontradaException extends Exception {
    public HabitacionNoEncontradaException(String message) {
        super(message);
    }
}
