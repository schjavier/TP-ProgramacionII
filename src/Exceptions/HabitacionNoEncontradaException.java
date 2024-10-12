package Exceptions;

/**
 *
 * No deberia extender de Exception, para que aparezcan como chequedas!?
 *
 */

public class HabitacionNoEncontradaException extends RuntimeException {
    public HabitacionNoEncontradaException(String message) {
        super(message);
    }
}
