package Modelo.Persona;

import JSONCreator.CreadorAJSON;
import Persistencia.InterfacePersistencia;

import java.io.IOException;

/**
 * Clase de servicio que se encarga de guardar en un archivo los datos en memoria
 */
public class EmpleadoService implements InterfacePersistencia {
    private final String nombreArchivo = "Empleados.json";

    public EmpleadoService() {
    }

    /**
     * Metodo para obtener el nombre de archivo
     * @return un {@code String} que representa el nombre de archivo
     */
    public String getNombreArchivo() {
        return nombreArchivo;
    }

    /**
     *
     * @param contenido un <code>String</code> que representa el contenido a persistir
     * @return {@code true} si se pudo persistir, {@code flase} de otro modo.
     */
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
