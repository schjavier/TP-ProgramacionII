package Modelo.Persona;

public class Empleado extends Persona {
    private static int contadorIdEmpleado = 0; // id auto incremental
    private int idEmpleado;
    private String usuario;
    private String clave;
    private String email;

    public Empleado(String nombre, String apellido, int dni, String usuario, String email, String clave) {
        super(nombre, apellido, dni);
        this.idEmpleado = ++contadorIdEmpleado;
        this.usuario = usuario;
        this.email = email;
        this.clave = clave;
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
}
