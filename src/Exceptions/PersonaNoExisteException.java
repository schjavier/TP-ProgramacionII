package Exceptions;


/**
 * Una excepcion que controla en el caso de que no exista una persona con ese dni.
 */
public class PersonaNoExisteException extends Exception {
    public PersonaNoExisteException(String message) {
        super(message);
    }
}
