package JSONCreator;

import java.io.*;

/**
 * Clase final que permite trabajar con JSON
 */

public final class CreadorAJSON {

    private CreadorAJSON() {}

    /**
     * Metodo que permite escribir en un archivo el contenido en formato json.
     * @param nombre {@code String} que representa el nombre del archivo.
     * @param contenido un {@code String} que representa el contenido que se va a persistir
     * @throws IOException Lanza esta excepcion si ocurre algun error con los archivos.
     */
    public static void uploadJSON(String nombre, String contenido) throws IOException
    {
        try {
            File archivo = new File("src/Persistencia/" + nombre);
            BufferedWriter salida = new BufferedWriter(new FileWriter(archivo,false));
            salida.write(contenido);
            salida.flush();
            salida.close();
        } catch (IOException e) {
            throw new IOException(e);
        }
    }

    /**
     * Metodo que [ermite descargar el contenido de un JSON al sistema
     * @param nombre {@code String} que reprensenta el nombre del archivo
     * @return devuelve un {@code String} con toda la informacion del json.
     * @throws IOException Lanza esta excepcion si hay promblemas con el archivo
     */
    public static String downloadJSON(String nombre) throws IOException
    {
        String datos;

        try {
            File archivo = new File("src/Persistencia/" + nombre);
            BufferedReader lectura = new BufferedReader(new FileReader(archivo));
            datos = lectura.readLine();
        } catch (IOException e) {
            throw new IOException(e);
        }

        return datos;
    }


}
