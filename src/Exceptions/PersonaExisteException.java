package Exceptions;

public class PersonaExisteException extends RuntimeException {
    public PersonaExisteException(String message) {
        super(message);
    }
}
