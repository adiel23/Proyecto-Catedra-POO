package dao;

import conexion.Conexion;
import excepciones.CuentaInactivaException;
import excepciones.CuentaNoEncontradaException;
import excepciones.SaldoInsuficienteException;
import excepciones.TransaccionBancariaException;
import modelo.MovimientoBancario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO transaccional encargado de depósitos, retiros, transferencias y consultas de movimientos.
 */
public class TransaccionDAO {

    private static final Logger logger = LogManager.getLogger(TransaccionDAO.class);

    public static final String TIPO_DEPOSITO = "Deposito";
    public static final String TIPO_RETIRO = "Retiro";
    public static final String TIPO_TRANSFERENCIA = "Transferencia";

    private static final String BASE_SELECT_MOVIMIENTO =
            "SELECT m.id_movimiento, m.tipo_movimiento, m.monto, m.descripcion, m.fecha_movimiento, " +
            "m.id_cuenta, m.id_cuenta_destino, co.numero_cuenta AS cuenta_origen, " +
            "COALESCE(cd.numero_cuenta, 'N/A') AS cuenta_destino " +
            "FROM movimientos m " +
            "INNER JOIN cuentas_bancarias co ON m.id_cuenta = co.id_cuenta " +
            "LEFT JOIN cuentas_bancarias cd ON m.id_cuenta_destino = cd.id_cuenta ";

    private static class CuentaLock {
        final int idCuenta;
        final BigDecimal saldo;
        final String estado;

        CuentaLock(int idCuenta, BigDecimal saldo, String estado) {
            this.idCuenta = idCuenta;
            this.saldo = saldo;
            this.estado = estado;
        }
    }

    /**
     * Realiza un depósito atómico en una cuenta activa verificando monto > 0.
     */
    public MovimientoBancario realizarDeposito(String numeroCuenta, BigDecimal monto, String descripcion)
            throws TransaccionBancariaException {
        return ejecutarOperacionSimple(numeroCuenta, monto, descripcion, TIPO_DEPOSITO);
    }

    /**
     * Realiza un retiro atómico en una cuenta activa verificando monto > 0 y saldo suficiente.
     */
    public MovimientoBancario realizarRetiro(String numeroCuenta, BigDecimal monto, String descripcion)
            throws TransaccionBancariaException {
        return ejecutarOperacionSimple(numeroCuenta, monto, descripcion, TIPO_RETIRO);
    }

    /**
     * Ejecuta una transferencia atómica entre cuenta origen y cuenta destino cumpliendo las 7 reglas de negocio.
     */
    public MovimientoBancario realizarTransferencia(String cuentaOrigen, String cuentaDestino,
                                                    BigDecimal monto, String descripcion)
            throws TransaccionBancariaException {
        String origen = normalizarTexto(cuentaOrigen);
        String destino = normalizarTexto(cuentaDestino);

        if (origen.isEmpty() || destino.isEmpty()) {
            logger.warn("Transferencia rechazada: cuenta origen o destino vacía.");
            throw new TransaccionBancariaException("La cuenta origen y la cuenta destino son obligatorias.");
        }
        if (origen.equals(destino)) {
            logger.warn("Transferencia rechazada: cuenta origen y destino iguales ({}).", origen);
            throw new TransaccionBancariaException("La cuenta origen y la cuenta destino no pueden ser iguales.");
        }
        validarMonto(monto, TIPO_TRANSFERENCIA);

        String descFinal = (descripcion == null || descripcion.trim().isEmpty())
                ? "Transferencia entre cuentas"
                : descripcion.trim();

        logger.info("Inicio Transferencia | origen={} | destino={} | monto={}", origen, destino, monto);

        Connection con = Conexion.conectar();
        if (con == null) {
            logger.error("Transferencia fallida: no se pudo establecer conexión con MySQL.");
            throw new TransaccionBancariaException("No se pudo conectar a la base de datos.");
        }

        try {
            con.setAutoCommit(false);

            // Bloquear ambas filas en orden determinístico para prevenir interbloqueos
            CuentaLock lockOrigen;
            CuentaLock lockDestino;
            if (origen.compareTo(destino) < 0) {
                lockOrigen = bloquearCuentaActiva(con, origen, "origen");
                lockDestino = bloquearCuentaActiva(con, destino, "destino");
            } else {
                lockDestino = bloquearCuentaActiva(con, destino, "destino");
                lockOrigen = bloquearCuentaActiva(con, origen, "origen");
            }

            if (lockOrigen.saldo.compareTo(monto) < 0) {
                throw new SaldoInsuficienteException(
                        String.format("Saldo insuficiente en la cuenta origen '%s'. Disponible: $%.2f",
                                origen, lockOrigen.saldo));
            }

            BigDecimal nuevoSaldoOrigen = lockOrigen.saldo.subtract(monto);
            BigDecimal nuevoSaldoDestino = lockDestino.saldo.add(monto);

            actualizarSaldo(con, lockOrigen.idCuenta, nuevoSaldoOrigen);
            actualizarSaldo(con, lockDestino.idCuenta, nuevoSaldoDestino);

            int idGenerado = insertarMovimiento(
                    con, TIPO_TRANSFERENCIA, monto, descFinal, lockOrigen.idCuenta, lockDestino.idCuenta);

            con.commit();
            logger.info("Transferencia exitosa | idMov={} | origen={} (saldo={}) | destino={} (saldo={}) | monto={}",
                    idGenerado, origen, nuevoSaldoOrigen, destino, nuevoSaldoDestino, monto);

            return buscarMovimientoEnConexion(con, idGenerado);
        } catch (TransaccionBancariaException e) {
            hacerRollback(con);
            logger.warn("Transferencia rechazada | origen={} | destino={} | motivo={}", origen, destino, e.getMessage());
            throw e;
        } catch (SQLException e) {
            hacerRollback(con);
            logger.error("Error SQL durante transferencia ({} -> {}): {}", origen, destino, e.getMessage(), e);
            throw new TransaccionBancariaException("Error en la base de datos al procesar la transferencia.", e);
        } finally {
            cerrarConexionTransaccional(con);
        }
    }

    /**
     * Consulta un movimiento específico por su identificador único.
     */
    public MovimientoBancario consultarMovimientoPorId(int idMovimiento) throws TransaccionBancariaException {
        if (idMovimiento <= 0) {
            throw new TransaccionBancariaException("El ID de movimiento debe ser mayor que cero.");
        }

        String sql = BASE_SELECT_MOVIMIENTO + "WHERE m.id_movimiento = ?";
        try (Connection con = Conexion.conectar()) {
            if (con == null) {
                logger.error("No se pudo consultar el movimiento {}: fallo de conexión.", idMovimiento);
                throw new TransaccionBancariaException("No se pudo conectar a la base de datos.");
            }
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, idMovimiento);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        MovimientoBancario mov = mapearMovimiento(rs);
                        logger.info("Consulta de movimiento ID {} exitosa (Tipo: {}).", idMovimiento, mov.getTipoMovimiento());
                        return mov;
                    }
                }
            }
            logger.warn("Consulta de movimiento ID {}: no encontrado.", idMovimiento);
            return null;
        } catch (SQLException e) {
            logger.error("Error SQL al consultar movimiento ID {}: {}", idMovimiento, e.getMessage(), e);
            throw new TransaccionBancariaException("Error al consultar el movimiento en la base de datos.", e);
        }
    }

    /**
     * Lista el historial de movimientos con filtros opcionales por número de cuenta y/o tipo.
     */
    public List<MovimientoBancario> listarHistorialMovimientos(String numeroCuentaFiltro, String tipoFiltro)
            throws TransaccionBancariaException {
        List<MovimientoBancario> lista = new ArrayList<>();
        String cuenta = normalizarTexto(numeroCuentaFiltro);
        String tipo = normalizarTexto(tipoFiltro);

        StringBuilder sql = new StringBuilder(BASE_SELECT_MOVIMIENTO);
        List<String> condiciones = new ArrayList<>();
        List<Object> parametros = new ArrayList<>();

        if (!cuenta.isEmpty()) {
            condiciones.add("(co.numero_cuenta = ? OR cd.numero_cuenta = ?)");
            parametros.add(cuenta);
            parametros.add(cuenta);
        }
        if (!tipo.isEmpty() && !tipo.equalsIgnoreCase("Todos")) {
            condiciones.add("m.tipo_movimiento = ?");
            parametros.add(tipo);
        }

        if (!condiciones.isEmpty()) {
            sql.append("WHERE ").append(String.join(" AND ", condiciones)).append(" ");
        }
        sql.append("ORDER BY m.fecha_movimiento DESC, m.id_movimiento DESC");

        try (Connection con = Conexion.conectar()) {
            if (con == null) {
                logger.error("No se pudo listar el historial de movimientos: fallo de conexión.");
                throw new TransaccionBancariaException("No se pudo conectar a la base de datos.");
            }
            try (PreparedStatement ps = con.prepareStatement(sql.toString())) {
                for (int i = 0; i < parametros.size(); i++) {
                    ps.setObject(i + 1, parametros.get(i));
                }
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        lista.add(mapearMovimiento(rs));
                    }
                }
            }
            logger.info("Historial de movimientos consultado (cuenta='{}', tipo='{}', registros={}).",
                    cuenta.isEmpty() ? "*" : cuenta,
                    tipo.isEmpty() ? "Todos" : tipo,
                    lista.size());
            return lista;
        } catch (SQLException e) {
            logger.error("Error SQL al listar historial de movimientos: {}", e.getMessage(), e);
            throw new TransaccionBancariaException("Error al consultar el historial de movimientos.", e);
        }
    }

    private MovimientoBancario ejecutarOperacionSimple(String numeroCuenta, BigDecimal monto,
                                                     String descripcion, String tipo)
            throws TransaccionBancariaException {
        String cuenta = normalizarTexto(numeroCuenta);
        if (cuenta.isEmpty()) {
            logger.warn("{} rechazado: número de cuenta vacío.", tipo);
            throw new TransaccionBancariaException("El número de cuenta es obligatorio.");
        }
        validarMonto(monto, tipo);

        String descFinal = (descripcion == null || descripcion.trim().isEmpty())
                ? tipo + " realizado"
                : descripcion.trim();

        logger.info("Inicio {} | cuenta={} | monto={}", tipo, cuenta, monto);

        Connection con = Conexion.conectar();
        if (con == null) {
            logger.error("{} fallido: no se pudo establecer conexión con MySQL.", tipo);
            throw new TransaccionBancariaException("No se pudo conectar a la base de datos.");
        }

        try {
            con.setAutoCommit(false);

            CuentaLock lock = bloquearCuentaActiva(con, cuenta, "");
            BigDecimal nuevoSaldo;
            if (TIPO_RETIRO.equals(tipo)) {
                if (lock.saldo.compareTo(monto) < 0) {
                    throw new SaldoInsuficienteException(
                            String.format("Saldo insuficiente en la cuenta '%s'. Disponible: $%.2f",
                                    cuenta, lock.saldo));
                }
                nuevoSaldo = lock.saldo.subtract(monto);
            } else {
                nuevoSaldo = lock.saldo.add(monto);
            }

            actualizarSaldo(con, lock.idCuenta, nuevoSaldo);
            int idGenerado = insertarMovimiento(con, tipo, monto, descFinal, lock.idCuenta, null);

            con.commit();
            logger.info("{} exitoso | idMov={} | cuenta={} | saldo nuevo={}", tipo, idGenerado, cuenta, nuevoSaldo);

            return buscarMovimientoEnConexion(con, idGenerado);
        } catch (TransaccionBancariaException e) {
            hacerRollback(con);
            logger.warn("{} rechazado | cuenta={} | motivo={}", tipo, cuenta, e.getMessage());
            throw e;
        } catch (SQLException e) {
            hacerRollback(con);
            logger.error("Error SQL durante {} en cuenta {}: {}", tipo, cuenta, e.getMessage(), e);
            throw new TransaccionBancariaException("Error en la base de datos durante el " + tipo.toLowerCase() + ".", e);
        } finally {
            cerrarConexionTransaccional(con);
        }
    }

    private CuentaLock bloquearCuentaActiva(Connection con, String numeroCuenta, String rol)
            throws SQLException, TransaccionBancariaException {
        String prefijo = rol.isEmpty() ? "La cuenta" : "La cuenta " + rol;
        String sql = "SELECT id_cuenta, saldo, estado FROM cuentas_bancarias WHERE numero_cuenta = ? FOR UPDATE";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, numeroCuenta);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    throw new CuentaNoEncontradaException(prefijo + " '" + numeroCuenta + "' no existe.");
                }
                String estado = rs.getString("estado");
                if (estado == null || !estado.equalsIgnoreCase("Activa")) {
                    throw new CuentaInactivaException(
                            prefijo + " '" + numeroCuenta + "' no está activa (estado actual: " + estado + ").");
                }
                return new CuentaLock(
                        rs.getInt("id_cuenta"),
                        rs.getBigDecimal("saldo"),
                        estado
                );
            }
        }
    }

    private void actualizarSaldo(Connection con, int idCuenta, BigDecimal nuevoSaldo) throws SQLException {
        String sql = "UPDATE cuentas_bancarias SET saldo = ? WHERE id_cuenta = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setBigDecimal(1, nuevoSaldo);
            ps.setInt(2, idCuenta);
            ps.executeUpdate();
        }
    }

    private int insertarMovimiento(Connection con, String tipo, BigDecimal monto, String descripcion,
                                   int idCuenta, Integer idCuentaDestino) throws SQLException {
        String sql = "INSERT INTO movimientos (tipo_movimiento, monto, descripcion, id_cuenta, id_cuenta_destino) " +
                     "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, tipo);
            ps.setBigDecimal(2, monto);
            ps.setString(3, descripcion);
            ps.setInt(4, idCuenta);
            if (idCuentaDestino == null) {
                ps.setNull(5, Types.INTEGER);
            } else {
                ps.setInt(5, idCuentaDestino);
            }
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        return 0;
    }

    private MovimientoBancario buscarMovimientoEnConexion(Connection con, int idMovimiento) throws SQLException {
        String sql = BASE_SELECT_MOVIMIENTO + "WHERE m.id_movimiento = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idMovimiento);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearMovimiento(rs);
                }
            }
        }
        return null;
    }

    private MovimientoBancario mapearMovimiento(ResultSet rs) throws SQLException {
        int idDestinoObj = rs.getInt("id_cuenta_destino");
        Integer idDestino = rs.wasNull() ? null : idDestinoObj;
        return new MovimientoBancario(
                rs.getInt("id_movimiento"),
                rs.getString("tipo_movimiento"),
                rs.getBigDecimal("monto"),
                rs.getString("descripcion"),
                rs.getString("fecha_movimiento"),
                rs.getInt("id_cuenta"),
                idDestino,
                rs.getString("cuenta_origen"),
                rs.getString("cuenta_destino")
        );
    }

    private void validarMonto(BigDecimal monto, String tipo) throws TransaccionBancariaException {
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
            logger.warn("{} rechazado: monto inválido ({}).", tipo, monto);
            throw new TransaccionBancariaException("El monto debe ser mayor que cero.");
        }
    }

    private String normalizarTexto(String texto) {
        return texto == null ? "" : texto.trim();
    }

    private void hacerRollback(Connection con) {
        try {
            if (con != null && !con.getAutoCommit()) {
                con.rollback();
            }
        } catch (SQLException e) {
            logger.error("Error al ejecutar rollback de la transacción: {}", e.getMessage(), e);
        }
    }

    private void cerrarConexionTransaccional(Connection con) {
        if (con != null) {
            try {
                if (!con.getAutoCommit()) {
                    con.setAutoCommit(true);
                }
                con.close();
            } catch (SQLException e) {
                logger.error("Error al restaurar y cerrar la conexión: {}", e.getMessage(), e);
            }
        }
    }
}
