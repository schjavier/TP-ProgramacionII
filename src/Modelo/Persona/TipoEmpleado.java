package Modelo.Persona;

import Modelo.Habitaciones.EstadoHabitacion;

/**
 * Enum que representa que tipo de permisos tiene el empleado.
 */
public enum TipoEmpleado {

    ADMINISTRADOR("Administrador"),
    RECEPCIONISTA("Recepcionista");

    private String tipoEmpleado;

    TipoEmpleado(String tipoEmpleado) {
        this.tipoEmpleado = tipoEmpleado;
    }

    public static String retornarValoresDeEnum() {
        String todos = "";
        for (TipoEmpleado valor : TipoEmpleado.values()) {
            todos = todos.concat(valor.getTipoEmpleado() + "\n");
        }
        return todos;
    }

    public String getTipoEmpleado() {
        return tipoEmpleado;
    }

}
