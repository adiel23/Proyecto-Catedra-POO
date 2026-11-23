package excepciones;

/**
 * Excepción base para errores de reglas de negocio o persistencia durante operaciones bancarias.
 */
public class TransaccionBancariaException extends Exception {
    public TransaccionBancariaException(String mensaje) {
        super(mensaje);
    }

    public TransaccionBancariaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
