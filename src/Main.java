public class Main {
    public static void main(String[] args) {
        Hotel hotel = new Hotel();

        System.out.println(hotel.contarEstadoHabitaciones(1));
        System.out.println(hotel.listarHabitaciones(1));


        try {
            //System.out.println(hotel.eliminarHabitacion(1,11)); // funciona :)
        } catch (BadOption e)
        {
            System.out.println(e.getMessage());
        }

        System.out.println(hotel.listarSegunEstado(1,EstadoHabitacion.OCUPADA));
    }
}