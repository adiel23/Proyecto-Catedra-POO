package excepciones;

/**
 * Excepción lanzada cuando una cuenta bancaria requerida en la transacción no existe en la base de datos.
 */
public class CuentaNoEncontradaException extends TransaccionBancariaException {
    public CuentaNoEncontradaException(String mensaje) {
        super(mensaje);
    }

    public CuentaNoEncontradaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
