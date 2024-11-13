package Modelo.Habitaciones;

/**
 * Enum que representa los estados que pueden adquirir las habitaciones.
 */
public enum EstadoHabitacion {
    DISPONIBLE("Disponible"),
    OCUPADA("Ocupada"),
    MANTENIMIENTO("Mantenimiento"),
    LIMPIEZA("Limpieza");

    private String estado;

    EstadoHabitacion(String estado) {
        this.estado = estado;
    }

    /**
     * Metodo para devolver un estado en forma de String
     * @return un {@code String}  que representa el estado de la habitacion
     */
    public String getEstado() {
        return this.estado;
    }

    /**
     * Metodo para devolver los valores de los Enum
     * @return un {@code String} con los valores de enum.
     */

    public static String retornarValoresDeEnum() {
        String todos = "";
        for (EstadoHabitacion valor : EstadoHabitacion.values()) {
            todos = todos.concat(valor.ordinal()+1 + ". " + valor.getEstado() + "\n");
        }
        return todos;
    }
}
