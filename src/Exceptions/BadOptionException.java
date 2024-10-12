package Exceptions;

/**
 *
 * No deberia extender de Exception, para que aparezcan como chequedas!?
 * Le modifique el nombre para que tenga exception al final!
 */
public class BadOptionException extends RuntimeException {
    public BadOptionException(String message) {
        super(message);
    }
}
