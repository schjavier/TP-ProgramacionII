package JSONCreator;

import java.io.*;

public final class CreadorAJSON {

    private CreadorAJSON() {}

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

    public static void cleanJSON(String nombre) throws IOException
    {
        try {
            File archivo = new File("src/Persistencia/" + nombre);
            BufferedWriter escritura = new BufferedWriter(new FileWriter(archivo,false));
            escritura.write("");
            escritura.flush();
            escritura.close();
        } catch (IOException e) {
            throw new IOException(e);
        }
    }

}
