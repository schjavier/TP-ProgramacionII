package Modelo.Habitaciones;

import JSONCreator.CreadorAJSON;
import Persistencia.InterfacePersistencia;

import java.io.IOException;

public class HabitacionService implements InterfacePersistencia {
    private final String nombreArchivo = "Habitaciones.json";

    public HabitacionService() {
    }

    /**
     * Metodo que devuelde el nombre de archivo.
     * @return un {@code String} quae representa el nombre del archivo/
     */
    public String getNombreArchivo() {
        return nombreArchivo;
    }


    /**
     *
     * @param contenido un <code>String</code> que representa el contenido a persistir
     * @return {@code true} si se pudo persistir con exit, {@code false} en cualquier otra forma
     */
    @Override
    public boolean persistir(String contenido) {
        boolean respuesta = false;
        try {
            CreadorAJSON.uploadJSON(nombreArchivo,contenido);
        } catch (IOException e) {
            System.out.println("Error en persistir habitaciones!");
        }
        return respuesta;
    }
}
