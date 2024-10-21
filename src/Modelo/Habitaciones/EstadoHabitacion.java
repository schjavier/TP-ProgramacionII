package Modelo.Habitaciones;

public enum EstadoHabitacion {
    DISPONIBLE("Disponible"),
    OCUPADA("Ocupada"),
    MANTENIMIENTO("Mantenimiento"),
    LIMPIEZA("Limpieza");

    private String estado;

    EstadoHabitacion(String estado) {
        this.estado = estado;
    }

    public String getEstado() {
        return this.estado;
    }

    public static String retornarValoresDeEnum() {
        String todos = "";
        for (EstadoHabitacion valor : EstadoHabitacion.values()) {
            todos = todos.concat(valor.ordinal() + ". " + valor.getEstado() + "\n");
        }
        return todos;
    }
}
