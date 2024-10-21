package Modelo.Persona;

public enum TipoEmpleado {

    ADMINISTRADOR("Admin"),
    RECEPCIONISTA("Recepcionista");

    private String tipoEmpleado;

    TipoEmpleado(String tipoEmpleado) {
        this.tipoEmpleado = tipoEmpleado;
    }

    public String getTipoEmpleado() {
        return tipoEmpleado;
    }

}
