package Exceptions;

/**
 * Esta excepcion es lanzada cauado la reserva no existe en el sistema.
 */
public class ReservaNoExisteException extends Exception {
    public ReservaNoExisteException(String msg){
        super(msg);
    }


}
