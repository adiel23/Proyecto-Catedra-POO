package excepciones;

import java.math.BigDecimal;

public class ValidadorBanco {

    // Validar que los textos obligatorios no vayan vacíos ni nulos
    public static void validarTextoObligatorio(String texto, String nombreCampo) throws ValidacionBancariaException {
        if (texto == null || texto.trim().isEmpty()) {
            throw new ValidacionBancariaException("El campo '" + nombreCampo + "' es obligatorio.");
        }
    }

    // Validar formato del DUI de El Salvador (ejemplo: 00000000-0)
    public static void validarDui(String dui) throws ValidacionBancariaException {
        validarTextoObligatorio(dui, "DUI");
        if (!dui.matches("^\\d{8}-\\d{1}$")) {
            throw new ValidacionBancariaException("El formato del DUI debe ser 00000000-0.");
        }
    }

    // Validar formato de teléfono (8 dígitos, opcionalmente con guion en medio: 0000-0000 o 00000000)
    public static void validarTelefono(String telefono) throws ValidacionBancariaException {
        if (telefono != null && !telefono.trim().isEmpty()) {
            if (!telefono.matches("^(\\d{4}-\\d{4}|\\d{8})$")) {
                throw new ValidacionBancariaException("El teléfono debe tener 8 dígitos (ej: 7000-0000 o 70000000).");
            }
        }
    }

    // Validar formato de correo electrónico
    public static void validarCorreo(String correo) throws ValidacionBancariaException {
        if (correo != null && !correo.trim().isEmpty()) {
            if (!correo.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                throw new ValidacionBancariaException("El formato del correo electrónico no es válido.");
            }
        }
    }

    // Validar formato y longitud del número de cuenta
    public static void validarNumeroCuenta(String numeroCuenta) throws ValidacionBancariaException {
        validarTextoObligatorio(numeroCuenta, "Número de cuenta");
        if (!numeroCuenta.matches("^[0-9]{6,20}$")) {
            throw new ValidacionBancariaException("El número de cuenta debe contener solo dígitos (entre 6 y 20 caracteres).");
        }
    }

    // Validar saldo inicial positivo o cero
    public static void validarSaldoInicial(double saldo) throws ValidacionBancariaException {
        if (saldo < 0) {
            throw new ValidacionBancariaException("El saldo inicial no puede ser un monto negativo.");
        }
    }

    /**
     * Valida que el texto ingresado represente un monto numérico válido y estrictamente mayor que cero.
     */
    public static BigDecimal validarMontoPositivo(String montoTexto) throws ValidacionBancariaException {
        validarTextoObligatorio(montoTexto, "Monto");
        try {
            BigDecimal monto = new BigDecimal(montoTexto.trim());
            if (monto.compareTo(BigDecimal.ZERO) <= 0) {
                throw new ValidacionBancariaException("El monto debe ser mayor que cero.");
            }
            return monto;
        } catch (NumberFormatException ex) {
            throw new ValidacionBancariaException("El monto debe ser un valor numérico válido.", ex);
        }
    }

    /**
     * Valida que el texto ingresado represente un identificador numérico entero positivo de movimiento.
     */
    public static int validarIdMovimiento(String idTexto) throws ValidacionBancariaException {
        validarTextoObligatorio(idTexto, "ID de Movimiento");
        try {
            int id = Integer.parseInt(idTexto.trim());
            if (id <= 0) {
                throw new ValidacionBancariaException("El ID de movimiento debe ser mayor que cero.");
            }
            return id;
        } catch (NumberFormatException ex) {
            throw new ValidacionBancariaException("El ID de movimiento debe ser un número entero válido.", ex);
        }
    }
}
