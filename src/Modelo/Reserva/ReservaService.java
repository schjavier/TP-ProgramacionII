package Modelo.Reserva;

import Exceptions.NullNameException;
import JSONCreator.CreadorAJSON;
import Persistencia.InterfacePersistecia;

/**
 *
 * Clase que se encargara de llevar y traer informacion desde los archivos a la memoria, y viceversa
 *
 */
public class ReservaService implements InterfacePersistecia {

    private final String nombreArchivo = "reservas.json";

    public ReservaService(){
    }


    /**
     * @param contenido
     * @return
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
