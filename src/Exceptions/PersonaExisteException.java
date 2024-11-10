package Exceptions;


/**
 * Una excepcion que controla en el caso de que exista una persona con ese dni.
 */
public class PersonaExisteException extends Exception {
    public PersonaExisteException(String message) {
        super(message);
    }
}
