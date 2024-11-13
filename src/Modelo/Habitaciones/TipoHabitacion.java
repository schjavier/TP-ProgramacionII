package Modelo.Habitaciones;

import Exceptions.BadOptionException;

/**
 * Enum que representa los tipos de habitaciones que tiene el hotel.
 */
public enum TipoHabitacion {
    REGULAR("Regular"),
    SUITE("Suite"),
    PRESIDENCIAL("Presidencial");
    private String estado;

    TipoHabitacion(String estado) {
        this.estado = estado;
    }

    /**
     * Metodo que devuelve el tipo de la habitacion
     * @return devuelve un {@code String} que representa el estado,
     */

    public String getTipo() {
        return this.estado;
    }

    /**
     * Metodo para devolver los valores de los Enum
     * @return un {@code String} con los valores de enum.
     */

    public static String retornarValoresDeEnum()
    {
        String todos = "";
        for(TipoHabitacion valor : TipoHabitacion.values())
        {
            todos = todos.concat(valor.ordinal()+1 + ". " + valor.getTipo() + "\n");
        }
        return todos;
    }

    /**
     *
     * @param tipo entero que representa el tipo de la habitacion
     * @return {@code true} si se pudo verificar, {@code false} de cualuier otra forma
     * @throws BadOptionException Lanza esta excepcion cuando la opcion es incorrecta.
     */

    public static boolean verificarEntrada(int tipo) throws BadOptionException
    {
        if(tipo > TipoHabitacion.values().length || tipo < 0)
        {
            throw new BadOptionException("Esa opcion no existe!");
        }
        return true;
    }


}
