package Modelo.Habitaciones;

import JSONCreator.CreadorAJSON;
import Persistencia.InterfacePersistencia;

import java.io.IOException;

public class HabitacionService implements InterfacePersistencia {
    private final String nombreArchivo = "Habitaciones.json";

    public HabitacionService() {
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

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
