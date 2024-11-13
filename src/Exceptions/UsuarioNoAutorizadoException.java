package Exceptions;

/**
 * Esta excepcion es lanzada cuando intenta ingresar al sistema
 */

public class UsuarioNoAutorizadoException extends Exception {
    public UsuarioNoAutorizadoException(String message) {
        super(message);
    }
}
