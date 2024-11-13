package Exceptions;

/**
 * Esta Excepcion es lanzada cuando la reserva ya existe.
 */
public class ReservaExisteException extends Exception {

    public ReservaExisteException(String msg) {
        super(msg);
    }

    public String getMsg(){
        return getMessage();
    }
}
