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
}
