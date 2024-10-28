package Modelo.Persona;

import org.json.JSONObject;


/**
 * Un empleado del sistema.
 */
public class Empleado extends Persona {
    private static int contadorIdEmpleado = 0; // id auto incremental
    private int idEmpleado;
    private String usuario;
    private String clave;
    private String email;
    private TipoEmpleado tipo;

    public Empleado(String nombre, String apellido, int dni, String usuario, String email, String clave, TipoEmpleado tipoEmpleado) {
        super(nombre, apellido, dni);
        this.idEmpleado = ++contadorIdEmpleado;
        this.usuario = usuario;
        this.email = email;
        this.clave = clave;
        this.tipo = tipoEmpleado;
    }

    public static int getContadorIdEmpleado() {
        return contadorIdEmpleado;
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public TipoEmpleado getTipo() {
        return tipo;
    }

    @Override
    public String toString() {
        return super.toString() +
                "idEmpleado: " + idEmpleado + '\n' +
                "usuario: " + usuario + '\n' +
                "clave: " + clave + '\n' +
                "email: " + email + '\n' +
                "tipo: " + tipo.getTipoempleado();
    }

    public JSONObject toJSON()
    {
        JSONObject empleado = new JSONObject();
        // datos base
        empleado.put("Nombre",getNombre());
        empleado.put("Apellido",getApellido());
        empleado.put("Dni",getDni());

        // datos del empleado
        empleado.put("Usuario",getUsuario());
        empleado.put("Clave",getClave());
        empleado.put("Email",getEmail());
        empleado.put("TipoEmpleado",tipo);
        return empleado;
    }



}
