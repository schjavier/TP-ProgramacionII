package Modelo.Persona;

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
