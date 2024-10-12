package DataChecks;

import Exceptions.BadDataException;

public final class VerificacionesDeDatos {

    private VerificacionesDeDatos() {}

    static public boolean verificarDni(int dni) throws BadDataException
    {
        boolean result = true;
        String err = "";

        String dnicheck = String.valueOf(dni);

        if(dnicheck.length() != 8)
        {
            result = false;
            err = err.concat("Ese dni no es valido porque:\n");
            if(dnicheck.length() < 8)
            {
                err = err.concat("- El dni es muy corto!");
            } else {
                err = err.concat("- El dni es muy largo!");
            }


            throw new BadDataException(err);
        }
        return result;
    }

    static public boolean verificarDni(Integer dni) throws BadDataException
    {
        boolean result = true;
        String err = "";

        String dnicheck = dni.toString();

        if(dnicheck.length() != 8)
        {
            result = false;
            err = err.concat("Ese dni no es valido porque:\n");
            if(dnicheck.length() < 8)
            {
                err = err.concat("- El dni es muy corto!");
            } else {
                err = err.concat("- El dni es muy largo!");
            }

            throw new BadDataException(err);
        }
        return result;
    }

    static public boolean tieneNumeros(String palabra) throws BadDataException
    {
        if(palabra.matches(".*\\d.*"))
        {
            throw new BadDataException("El texto introducido no debe tener numeros");
        }
        return true;
    }


}
