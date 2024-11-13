package Persistencia;

/**
 * Interface de persistencia, contiene un solo metodo que se debe implementar para poder persistir los datos.
 *
 * @author JulianAlonso
 * @author TomasSilva
 * @author JavierSchettini
 *
 */

public interface InterfacePersistencia {


    /**
     * Metodo para persistir los datos
     *
     * @param contenido un <code>String</code> que representa el contenido a persistir
     * @return <code>true</code> o <code>false</code>
     */
    boolean persistir(String contenido);

}
