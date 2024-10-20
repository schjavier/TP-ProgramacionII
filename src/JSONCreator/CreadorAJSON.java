package JSONCreator;


import Exceptions.NullNameException;

import java.io.*;

public final class CreadorAJSON {

    private CreadorAJSON() {}

    public static void uploadJSON(String nombre, String contenido) throws NullNameException
    {
        if(nombre == null)
        {
            throw new NullNameException("No puede tener un nombre nulo");
        }

        try {
            File archivo = new File(nombre);
            BufferedWriter salida = new BufferedWriter(new FileWriter(archivo,false));
            salida.write(contenido);
            salida.flush();
            salida.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String downloadJSON(String nombre) throws NullNameException, IOException
    {
        if(nombre == null)
        {
            throw new NullNameException("El nombre no puede ser nulo!");
        }

        String datos;

        try {
            File archivo = new File(nombre);
            BufferedReader lectura = new BufferedReader(new FileReader(archivo));
            datos = lectura.readLine();
        } catch (IOException e) {
            throw new IOException(e);
        }

        return datos;
    }

    public static void cleanJSON(String nombre) throws IOException
    {
        if(nombre == null)
        {
            throw new NullNameException("El nombre no puede ser nulo!");
        }

        try {
            File archivo = new File(nombre);
            BufferedWriter escritura = new BufferedWriter(new FileWriter(archivo,false));
            escritura.write("");
            escritura.flush();
            escritura.close();
        } catch (IOException e) {
            throw new IOException(e);
        }
    }

}
