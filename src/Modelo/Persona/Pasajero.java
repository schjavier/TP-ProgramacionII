package Modelo.Persona;

import org.json.JSONObject;


/**
 * Clase que representa a un pasajero que hace reservas y esta en habitaciones
 */
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

    /**
     * Clase que convierte un pasajero a JSON
     *
     * @return devuelve {@code JSONObject} que representa el Pasajero
     */

    public JSONObject toJSON()
    {
        JSONObject persona = new JSONObject();
        persona.put("Nombre",getNombre());
        persona.put("Apellido",getApellido());
        persona.put("Dni",getDni());
        persona.put("Domicilio",getDomicilio());
        return persona;
    }
}
