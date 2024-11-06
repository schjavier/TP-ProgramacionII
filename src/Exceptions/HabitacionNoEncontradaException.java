package Exceptions;

/**
 *
 * No deberia extender de Exception, para que aparezcan como chequedas!?
 *
 */

public class HabitacionNoEncontradaException extends Exception {
    public HabitacionNoEncontradaException(String message) {
        super(message);
    }
}
