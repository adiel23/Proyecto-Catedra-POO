package excepciones;

/**
 * Excepción lanzada cuando la cuenta involucrada no se encuentra en estado 'Activa'.
 */
public class CuentaInactivaException extends TransaccionBancariaException {
    public CuentaInactivaException(String mensaje) {
        super(mensaje);
    }

    public CuentaInactivaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
