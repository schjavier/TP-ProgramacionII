package Exceptions;


public class BadOptionException extends RuntimeException {
    public BadOptionException(String message) {
        super(message);
    }
}
