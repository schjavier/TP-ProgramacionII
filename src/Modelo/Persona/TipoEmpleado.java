package Modelo.Persona;

import Modelo.Habitaciones.EstadoHabitacion;

public enum TipoEmpleado {

    ADMINISTRADOR("Admin"),
    RECEPCIONISTA("Recepcionista");

    private String tipoEmpleado;

    TipoEmpleado(String tipoEmpleado) {
        this.tipoEmpleado = tipoEmpleado;
    }

    public static String retornarValoresDeEnum() {
        String todos = "";
        for (TipoEmpleado valor : TipoEmpleado.values()) {
            todos = todos.concat(valor.ordinal()+1  + ". " + valor.getTipoEmpleado() + "\n");
        }
        return todos;
    }

    public String getTipoEmpleado() {
        return tipoEmpleado;
    }

}
