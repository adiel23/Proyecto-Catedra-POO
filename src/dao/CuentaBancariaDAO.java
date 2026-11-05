package dao;

import conexion.Conexion;
import modelo.CuentaBancaria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CuentaBancariaDAO {

    public boolean insertar(CuentaBancaria cuenta) throws SQLException {
        String sql = "INSERT INTO cuentas_bancarias (numero_cuenta, tipo_cuenta, saldo, estado, id_cliente) " +
                     "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = Conexion.conectar()) {
            if (con == null) throw new SQLException("No se pudo conectar a la base de datos.");

            try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, cuenta.getNumeroCuenta());
                ps.setString(2, cuenta.getTipoCuenta());
                ps.setDouble(3, cuenta.getSaldo());
                ps.setString(4, cuenta.getEstadoCuenta());
                ps.setInt(5, cuenta.getIdCliente());

                int filas = ps.executeUpdate();
                if (filas > 0) {
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (rs.next()) {
                            cuenta.setIdCuenta(rs.getInt(1));
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public boolean actualizar(CuentaBancaria cuenta) throws SQLException {
        String sql = "UPDATE cuentas_bancarias SET tipo_cuenta = ?, saldo = ?, estado = ? " +
                     "WHERE numero_cuenta = ? OR id_cuenta = ?";

        try (Connection con = Conexion.conectar()) {
            if (con == null) throw new SQLException("No se pudo conectar a la base de datos.");

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, cuenta.getTipoCuenta());
                ps.setDouble(2, cuenta.getSaldo());
                ps.setString(3, cuenta.getEstadoCuenta());
                ps.setString(4, cuenta.getNumeroCuenta());
                ps.setInt(5, cuenta.getIdCuenta());

                return ps.executeUpdate() > 0;
            }
        }
    }

    public boolean eliminar(int idCuenta) throws SQLException {
        String sql = "DELETE FROM cuentas_bancarias WHERE id_cuenta = ?";

        try (Connection con = Conexion.conectar()) {
            if (con == null) throw new SQLException("No se pudo conectar a la base de datos.");

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, idCuenta);
                return ps.executeUpdate() > 0;
            }
        }
    }

    public List<CuentaBancaria> listarTodas() throws SQLException {
        List<CuentaBancaria> lista = new ArrayList<>();
        String sql = "SELECT c.id_cuenta, c.numero_cuenta, c.tipo_cuenta, c.saldo, c.estado, c.id_cliente, " +
                     "cli.codigo_cliente, cli.nombre_completo " +
                     "FROM cuentas_bancarias c " +
                     "INNER JOIN clientes cli ON c.id_cliente = cli.id_cliente " +
                     "ORDER BY c.id_cuenta DESC";

        try (Connection con = Conexion.conectar()) {
            if (con == null) throw new SQLException("No se pudo conectar a la base de datos.");

            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    CuentaBancaria cuenta = new CuentaBancaria(
                        rs.getInt("id_cuenta"),
                        rs.getString("numero_cuenta"),
                        rs.getString("tipo_cuenta"),
                        rs.getDouble("saldo"),
                        rs.getString("estado"),
                        rs.getInt("id_cliente"),
                        rs.getString("codigo_cliente"),
                        rs.getString("nombre_completo")
                    );
                    lista.add(cuenta);
                }
            }
        }
        return lista;
    }

    public CuentaBancaria buscarPorNumero(String numeroCuenta) throws SQLException {
        String sql = "SELECT c.id_cuenta, c.numero_cuenta, c.tipo_cuenta, c.saldo, c.estado, c.id_cliente, " +
                     "cli.codigo_cliente, cli.nombre_completo " +
                     "FROM cuentas_bancarias c " +
                     "INNER JOIN clientes cli ON c.id_cliente = cli.id_cliente " +
                     "WHERE c.numero_cuenta = ?";

        try (Connection con = Conexion.conectar()) {
            if (con == null) throw new SQLException("No se pudo conectar a la base de datos.");

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, numeroCuenta);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new CuentaBancaria(
                            rs.getInt("id_cuenta"),
                            rs.getString("numero_cuenta"),
                            rs.getString("tipo_cuenta"),
                            rs.getDouble("saldo"),
                            rs.getString("estado"),
                            rs.getInt("id_cliente"),
                            rs.getString("codigo_cliente"),
                            rs.getString("nombre_completo")
                        );
                    }
                }
            }
        }
        return null;
    }
}
