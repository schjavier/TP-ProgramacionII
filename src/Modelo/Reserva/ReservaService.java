package Modelo.Reserva;

import Exceptions.NullNameException;
import JSONCreator.CreadorAJSON;
import Persistencia.InterfacePersistencia;

import java.io.IOException;

/**
 * Clase de servicio que se encarga de guardar en un archivo los datos en memoria
 */

public class ReservaService implements InterfacePersistencia {

    private final String nombreArchivo = "Reservas.json";

    public ReservaService(){
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    /**
     *
     * @param contenido un <code>String</code> que representa el contenido a persistir
     * @return {@code true} si pudo persistir los datos, {@code false} de cualquier otra forma
     */
    @Override
    public boolean persistir(String contenido) {
        boolean respuesta = false;
        try {
            CreadorAJSON.uploadJSON(nombreArchivo, contenido);
        } catch (IOException e) {
            System.out.println("Error en persistir reserva!");
        }

        return respuesta;
    }
}
