package Exceptions;


/**
 * Revisa si el nombre de un archivo es nulo, para evitar errores inesperados.
 */
public class NullNameException extends BadDataException {
    public NullNameException(String message) {
        super(message);
    }
}
