package Exceptions;

public class ReservaExisteException extends Exception {

    public ReservaExisteException(String msg) {
        super(msg);
    }

    public String getMsg(){
        return getMessage();
    }
}
