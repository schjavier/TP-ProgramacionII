package Exceptions;

public class PersonaNoExisteException extends RuntimeException {
    public PersonaNoExisteException(String message) {
        super(message);
    }
}
