package Modelo.Persona;

/**
 * Enum que representa que tipo de permisos tiene el empleado.
 */
public enum TipoEmpleado {

    ADMINISTRADOR("Admin"),
    RECEPCIONISTA("Recepcionista");

    private String tipoempleado;

    TipoEmpleado(String tipoempleado) {
        this.tipoempleado = tipoempleado;
    }

    public String getTipoempleado() {
        return tipoempleado;
    }

}
