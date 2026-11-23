package excepciones;

/**
 * Excepción para errores de validación de entrada o formato en formularios y reglas de negocio básicas.
 */
public class ValidacionBancariaException extends Exception {
    public ValidacionBancariaException(String mensaje) {
        super(mensaje);
    }

    public ValidacionBancariaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
