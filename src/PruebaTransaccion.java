import java.math.BigDecimal;

public class PruebaTransaccion {

    public static void main(String[] args) {
        TransaccionService s = new TransaccionService();

        probar(s, "Deposito normal", true, "100000003", "100");
        probar(s, "Retiro normal", false, "100000003", "50");
        probar(s, "Monto cero", true, "100000003", "0");
        probar(s, "Retiro mayor al saldo", false, "100000003", "9999999");
        probar(s, "Cuenta inexistente", true, "999999999", "10");
    }

    static void probar(TransaccionService s, String caso, boolean deposito, String cuenta, String monto) {
        try {
            if (deposito) s.depositar(cuenta, new BigDecimal(monto));
            else s.retirar(cuenta, new BigDecimal(monto));
            System.out.println(caso + " -> OK");
        } catch (TransaccionService.TransaccionException e) {
            System.out.println(caso + " -> " + e.getMessage());
        }
    }
}