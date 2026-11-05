package excepciones;

public class ValidadorBanco {

    // Validar que los textos obligatorios no vayan vacíos
    public static void validarTextoObligatorio(String texto, String nombreCampo) throws ValidacionBancariaException {
        if (texto == null || texto.trim().isEmpty()) {
            throw new ValidacionBancariaException("El campo '" + nombreCampo + "' es obligatorio.");
        }
    }

    // Validar formato del DUI (ejemplo: 00000000-0)
    public static void validarDui(String dui) throws ValidacionBancariaException {
        validarTextoObligatorio(dui, "DUI");
        if (!dui.matches("\\d{8}-\\d{1}")) {
            throw new ValidacionBancariaException("El formato del DUI debe ser 00000000-0.");
        }
    }

    // Validar saldo inicial positivo
    public static void validarSaldoInicial(double saldo) throws ValidacionBancariaException {
        if (saldo < 0) {
            throw new ValidacionBancariaException("El saldo inicial no puede ser un monto negativo.");
        }
    }
}
