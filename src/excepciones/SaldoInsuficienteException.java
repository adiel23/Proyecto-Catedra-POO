package excepciones;

/**
 * Excepción lanzada cuando el saldo disponible en la cuenta origen es insuficiente para cubrir el monto solicitado.
 */
public class SaldoInsuficienteException extends TransaccionBancariaException {
    public SaldoInsuficienteException(String mensaje) {
        super(mensaje);
    }

    public SaldoInsuficienteException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
