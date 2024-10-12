import DataChecks.VerificacionesDeDatos;
import Exceptions.BadDataException;
import Exceptions.PersonaExisteException;
import Modelo.Persona.Pasajero;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    static public Scanner teclado = new Scanner(System.in);
    public static void main(String[] args) {
        Hotel hotel = new Hotel();
        /*
        System.out.println(hotel.contarEstadoHabitaciones(1));
        System.out.println(hotel.listarHabitaciones(1));


        try {
            //System.out.println(hotel.eliminarHabitacion(1,11)); // funciona :)
        } catch (BadOptionException e)
        {
            System.out.println(e.getMessage());
        }

        System.out.println(hotel.listarSegunEstado(1, EstadoHabitacion.OCUPADA));*/

        /*
        System.out.println("Que tipo de habitación quiere reservar?");


        int decision = 1;
        try {
            System.out.println(hotel.listarHabitaciones(decision));
        } catch (BadOptionException e)
        {
            System.out.println(e.getMessage());
        }


        try {
            System.out.println(hotel.existePasajeroConEseDNI(11111111));
        } catch (BadDniException e)
        {
            System.out.println(e.getMessage());
        }

        try{
            System.out.println(hotel.buscarPasajeroConEseDNI(11111111));
        } catch (PersonaNoExisteException e)
        {
            System.out.println(e.getMessage());
        }*/

        /*
        Empleado empleado1 = new Empleado("Julio","Test",22224444,"carlitoslol","carlitos@gmail.com","helloworld");
        System.out.println("\n");
        System.out.println(empleado1);*/

        agregarPasajero(hotel);
    }

    static public void agregarPasajero(Hotel hotel)
    {
        String nombre;
        String apellido;
        int dni;
        String direccion;

        nombre = definirNombreoApe("Nombre");
        apellido = definirNombreoApe("Apellido");
        dni = definirDni(hotel);
        teclado.nextLine();
        System.out.println("Direccion");
        direccion = teclado.nextLine();

        hotel.agregarPasajero(nombre,apellido,dni,direccion);
    }

    static public String definirNombreoApe(String mensaje)
    {
        String dato = "";
        boolean nomver = false;
        while(!nomver)
        {
            try {
                System.out.println(mensaje);
                dato = teclado.nextLine();
                VerificacionesDeDatos.tieneNumeros(dato);
                nomver = true;
            } catch (BadDataException e)
            {
                System.out.println(e.getMessage());
            }
            catch (InputMismatchException e)
            {
                System.out.println("algo paso");
            }
        }
        return dato;
    }

    static public int definirDni(Hotel hotel)
    {
        int dni = 0;
        boolean dniver = false;

        while(!dniver)
        {
            try {
                System.out.println("Dni");
                dni = teclado.nextInt();
                hotel.verSiElDniEstaCargardo(dni);
                dniver = true;
            } catch (BadDataException | PersonaExisteException e)
            {
                System.out.println(e.getMessage());
            }
            catch (InputMismatchException e)
            {
                System.out.println("No puede tener letras!");
            }
        }
        return dni;
    }



}