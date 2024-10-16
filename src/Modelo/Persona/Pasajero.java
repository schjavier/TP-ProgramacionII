package Modelo.Persona;

public class Pasajero extends Persona {
    private String domicilio;

    public Pasajero(String nombre, String apellido, int dni, String domicilio) {
        super(nombre, apellido, dni);
        this.domicilio = domicilio;
    }

    public String getDomicilio() {
        return this.domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    @Override
    public String toString() {
        return super.toString() +
                "domicilio: " + domicilio;
    }
}
