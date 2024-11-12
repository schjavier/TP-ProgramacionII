package Modelo.Persona;

import JSONCreator.CreadorAJSON;
import Persistencia.InterfacePersistencia;

import java.io.IOException;

public class EmpleadoService implements InterfacePersistencia {
    private final String nombreArchivo = "Empleados.json";

    public EmpleadoService() {
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
            System.out.println("Error en persistir empleados!");
        }
        return respuesta;
    }
}
