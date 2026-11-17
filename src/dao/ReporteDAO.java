package dao;

import conexion.Conexion;
import modelo.Cliente;
import modelo.CuentaBancaria;
import modelo.ReporteMovimiento;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReporteDAO {

    private static final Logger logger = LogManager.getLogger(ReporteDAO.class);

    // 1 y 2. Reporte de Cuentas por Estado ("Activa" o "Bloqueada")
    public List<CuentaBancaria> listarCuentasPorEstado(String estado) throws SQLException {
        List<CuentaBancaria> lista = new ArrayList<>();
        String sql = "SELECT c.id_cuenta, c.numero_cuenta, c.tipo_cuenta, c.saldo, c.estado, c.id_cliente, " +
                "cli.codigo_cliente, cli.nombre_completo " +
                "FROM cuentas_bancarias c " +
                "INNER JOIN clientes cli ON c.id_cliente = cli.id_cliente " +
                "WHERE c.estado = ? " +
                "ORDER BY c.id_cuenta DESC";

        try (Connection con = Conexion.conectar()) {
            if (con == null) {
                throw new SQLException("No se pudo establecer conexión con la base de datos.");
            }
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, estado);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        lista.add(new CuentaBancaria(
                                rs.getInt("id_cuenta"),
                                rs.getString("numero_cuenta"),
                                rs.getString("tipo_cuenta"),
                                rs.getDouble("saldo"),
                                rs.getString("estado"),
                                rs.getInt("id_cliente"),
                                rs.getString("codigo_cliente"),
                                rs.getString("nombre_completo")
                        ));
                    }
                }
            }
            logger.info("Reporte generado: Cuentas con estado '{}' (Registros: {}).", estado, lista.size());
        } catch (SQLException e) {
            logger.error("Error al generar reporte de cuentas con estado '{}': {}", estado, e.getMessage(), e);
            throw e;
        }
        return lista;
    }

    // 3. Reporte de Clientes Registrados
    public List<Cliente> listarClientesRegistrados() throws SQLException {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT id_cliente, codigo_cliente, dui, nombre_completo, telefono, correo_electronico, direccion, estado " +
                "FROM clientes ORDER BY id_cliente ASC";

        try (Connection con = Conexion.conectar()) {
            if (con == null) {
                throw new SQLException("No se pudo establecer conexión con la base de datos.");
            }
            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Cliente(
                            rs.getInt("id_cliente"),
                            rs.getString("codigo_cliente"),
                            rs.getString("dui"),
                            rs.getString("nombre_completo"),
                            rs.getString("telefono"),
                            rs.getString("correo_electronico"),
                            rs.getString("direccion"),
                            rs.getString("estado")
                    ));
                }
            }
            logger.info("Reporte generado: Clientes registrados (Total: {}).", lista.size());
        } catch (SQLException e) {
            logger.error("Error al generar reporte de clientes registrados: {}", e.getMessage(), e);
            throw e;
        }
        return lista;
    }

    // 4. Reporte de Historial General de Transacciones
    public List<ReporteMovimiento> listarHistorialTransacciones() throws SQLException {
        List<ReporteMovimiento> lista = new ArrayList<>();
        String sql = "SELECT m.id_movimiento, m.tipo_movimiento, m.monto, m.descripcion, m.fecha_movimiento, " +
                "co.numero_cuenta AS cuenta_origen, " +
                "COALESCE(cd.numero_cuenta, 'N/A') AS cuenta_destino " +
                "FROM movimientos m " +
                "INNER JOIN cuentas_bancarias co ON m.id_cuenta = co.id_cuenta " +
                "LEFT JOIN cuentas_bancarias cd ON m.id_cuenta_destino = cd.id_cuenta " +
                "ORDER BY m.fecha_movimiento DESC, m.id_movimiento DESC";

        try (Connection con = Conexion.conectar()) {
            if (con == null) {
                throw new SQLException("No se pudo establecer conexión con la base de datos.");
            }
            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new ReporteMovimiento(
                            rs.getInt("id_movimiento"),
                            rs.getString("tipo_movimiento"),
                            rs.getDouble("monto"),
                            rs.getString("descripcion"),
                            rs.getString("fecha_movimiento"),
                            rs.getString("cuenta_origen"),
                            rs.getString("cuenta_destino")
                    ));
                }
            }
            logger.info("Reporte generado: Historial general de transacciones (Registros: {}).", lista.size());
        } catch (SQLException e) {
            logger.error("Error al generar historial general de transacciones: {}", e.getMessage(), e);
            throw e;
        }
        return lista;
    }

    // 5. Reporte de Transacciones por Cuenta (como origen o como destino)
    public List<ReporteMovimiento> listarTransaccionesPorCuenta(String numeroCuenta) throws SQLException {
        List<ReporteMovimiento> lista = new ArrayList<>();
        String sql = "SELECT m.id_movimiento, m.tipo_movimiento, m.monto, m.descripcion, m.fecha_movimiento, " +
                "co.numero_cuenta AS cuenta_origen, " +
                "COALESCE(cd.numero_cuenta, 'N/A') AS cuenta_destino " +
                "FROM movimientos m " +
                "INNER JOIN cuentas_bancarias co ON m.id_cuenta = co.id_cuenta " +
                "LEFT JOIN cuentas_bancarias cd ON m.id_cuenta_destino = cd.id_cuenta " +
                "WHERE co.numero_cuenta = ? OR cd.numero_cuenta = ? " +
                "ORDER BY m.fecha_movimiento DESC, m.id_movimiento DESC";

        try (Connection con = Conexion.conectar()) {
            if (con == null) {
                throw new SQLException("No se pudo establecer conexión con la base de datos.");
            }
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, numeroCuenta);
                ps.setString(2, numeroCuenta);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        lista.add(new ReporteMovimiento(
                                rs.getInt("id_movimiento"),
                                rs.getString("tipo_movimiento"),
                                rs.getDouble("monto"),
                                rs.getString("descripcion"),
                                rs.getString("fecha_movimiento"),
                                rs.getString("cuenta_origen"),
                                rs.getString("cuenta_destino")
                        ));
                    }
                }
            }
            logger.info("Reporte generado: Transacciones para la cuenta '{}' (Registros: {}).", numeroCuenta, lista.size());
        } catch (SQLException e) {
            logger.error("Error al consultar transacciones de la cuenta '{}': {}", numeroCuenta, e.getMessage(), e);
            throw e;
        }
        return lista;
    }

    // 6, 7 y 8. Totales acumulados por tipo de movimiento ("DEPOSITO", "RETIRO", "TRANSFERENCIA")
    public double obtenerTotalPorTipoMovimiento(String tipoMovimiento) throws SQLException {
        String sql = "SELECT COALESCE(SUM(monto), 0) AS total FROM movimientos WHERE UPPER(tipo_movimiento) = UPPER(?)";

        try (Connection con = Conexion.conectar()) {
            if (con == null) {
                throw new SQLException("No se pudo establecer conexión con la base de datos.");
            }
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, tipoMovimiento);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        double total = rs.getDouble("total");
                        logger.info("Total calculado para '{}': ${}", tipoMovimiento, String.format("%.2f", total));
                        return total;
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("Error al calcular total de '{}': {}", tipoMovimiento, e.getMessage(), e);
            throw e;
        }
        return 0.0;
    }
}