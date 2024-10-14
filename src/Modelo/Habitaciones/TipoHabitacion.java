package Modelo.Habitaciones;

import Exceptions.BadOptionException;

public enum TipoHabitacion {
    REGULAR("Regular"),
    SUITE("Suite"),
    PRESIDENCIAL("Presidencial");
    private String estado;

    TipoHabitacion(String estado) {
        this.estado = estado;
    }

    public String getTipo() {
        return this.estado;
    }

    public static String retornarValoresDeEnum()
    {
        String todos = "";
        for(TipoHabitacion valor : TipoHabitacion.values())
        {
            todos = todos.concat(valor.ordinal()+1 + ". " + valor.getTipo() + "\n");
        }
        return todos;
    }

    public static boolean verificarEntrada(int tipo) throws BadOptionException
    {
        if(tipo > TipoHabitacion.values().length || tipo < 0)
        {
            throw new BadOptionException("Esa opcion no existe!");
        }
        return true;
    }


}
