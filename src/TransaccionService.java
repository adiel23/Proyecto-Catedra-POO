import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import conexion.Conexion;

public class TransaccionService {

    private static final Logger logger = LogManager.getLogger(TransaccionService.class);

    private static final String TIPO_DEPOSITO = "Deposito";
    private static final String TIPO_RETIRO = "Retiro";
    
    public static class TransaccionException extends Exception {
        public TransaccionException(String msg) { super(msg); }
        public TransaccionException(String msg, Throwable causa) { super(msg, causa); }
    }

    public void depositar(String numeroCuenta, BigDecimal monto) throws TransaccionException {
        operar(numeroCuenta, monto, TIPO_DEPOSITO);
    }

    public void retirar(String numeroCuenta, BigDecimal monto) throws TransaccionException {
        operar(numeroCuenta, monto, TIPO_RETIRO);
    }

    private void operar(String numeroCuenta, BigDecimal monto, String tipo) throws TransaccionException {
        logger.info("Inicio {} | cuenta={} | monto={}", tipo, numeroCuenta, monto);

        // Regla: monto mayor que cero
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
            logger.warn("{} rechazado: monto invalido {}", tipo, monto);
            throw new TransaccionException("El monto debe ser mayor que cero.");
        }

        Connection con = Conexion.conectar();
        if (con == null) {
            throw new TransaccionException("No se pudo conectar a la base de datos.");
        }

        try {
            con.setAutoCommit(false);

            // Regla: la cuenta debe existir y estar activa
            int idCuenta;
            BigDecimal saldo;
            String sqlCuenta = "SELECT id_cuenta, saldo, estado FROM cuentas_bancarias "
                    + "WHERE numero_cuenta = ? FOR UPDATE";
            try (PreparedStatement ps = con.prepareStatement(sqlCuenta)) {
                ps.setString(1, numeroCuenta);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        throw new TransaccionException("La cuenta no existe.");
                    }
                    String estado = rs.getString("estado");
                    if (estado == null || !estado.toLowerCase().startsWith("activ")) {
                        throw new TransaccionException("La cuenta no esta activa.");
                    }
                    idCuenta = rs.getInt("id_cuenta");
                    saldo = rs.getBigDecimal("saldo");
                }
            }

            // Regla: en retiro debe haber saldo suficiente
            BigDecimal nuevoSaldo;
            if (tipo.equals(TIPO_RETIRO)) {
                if (saldo.compareTo(monto) < 0) {
                    throw new TransaccionException("Saldo insuficiente.");
                }
                nuevoSaldo = saldo.subtract(monto);
            } else {
                nuevoSaldo = saldo.add(monto);
            }

            // Actualizar saldo
            try (PreparedStatement ps = con.prepareStatement(
                    "UPDATE cuentas_bancarias SET saldo = ? WHERE id_cuenta = ?")) {
                ps.setBigDecimal(1, nuevoSaldo);
                ps.setInt(2, idCuenta);
                ps.executeUpdate();
            }

            // Registrar el movimiento
            String sqlMov = "INSERT INTO movimientos "
                    + "(tipo_movimiento, monto, descripcion, id_cuenta, id_cuenta_destino) "
                    + "VALUES (?, ?, ?, ?, NULL)";
            try (PreparedStatement ps = con.prepareStatement(sqlMov)) {
                ps.setString(1, tipo);
                ps.setBigDecimal(2, monto);
                ps.setString(3, tipo + " realizado");
                ps.setInt(4, idCuenta);
                ps.executeUpdate();
            }

            con.commit();
            logger.info("{} exitoso | cuenta={} | saldo nuevo={}", tipo, numeroCuenta, nuevoSaldo);

        } catch (TransaccionException e) {
            rollback(con);
            logger.warn("{} rechazado: {}", tipo, e.getMessage());
            throw e;
        } catch (SQLException e) {
            rollback(con);
            logger.error("Error SQL en " + tipo, e);
            throw new TransaccionException("Error en la base de datos: " + e.getMessage(), e);
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                logger.error("Error al cerrar la conexion.", e);
            }
        }
    }

    private void rollback(Connection con) {
        try {
            con.rollback();
        } catch (SQLException e) {
            logger.error("Error en rollback.", e);
        }
    }
}