package Exceptions;

/**
 * Representa un error en selección de opcion. Ya sea en menu o de carga.
 */
public class BadOptionException extends Exception {
    public BadOptionException(String message) {
        super(message);
    }
}
