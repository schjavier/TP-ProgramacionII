package Modelo.Reserva;

import Exceptions.NullNameException;
import JSONCreator.CreadorAJSON;
import Persistencia.InterfacePersistencia;

/**
 *
 * Clase que se encargara de llevar y traer informacion desde los archivos a la memoria, y viceversa
 *
 */
public class ReservaService implements InterfacePersistencia {

    private final String nombreArchivo = "Reservas.json";

    public ReservaService(){
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    /**
     * @param contenido un string que representa el Contenido a presistir
     * @return true si se puso persistir los datos, false de otra forma.
     * @throws NullNameException
     */
    @Override
    public boolean persistir(String contenido) {
        boolean respuesta = false;
        try {
            CreadorAJSON.uploadJSON(nombreArchivo, contenido);

        } catch (NullNameException ex){
            ex.getMessage();
        }

        return respuesta;
    }

}
