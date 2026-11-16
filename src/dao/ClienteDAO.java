package dao;

import conexion.Conexion;
import modelo.Cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    public boolean insertar(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO clientes (codigo_cliente, dui, nombre_completo, telefono, correo_electronico, direccion, estado) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = Conexion.conectar()) {
            if (con == null) throw new SQLException("No se pudo conectar a la base de datos.");

            try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, cliente.getCodigoCliente());
                ps.setString(2, cliente.getDui());
                ps.setString(3, cliente.getNombreCompleto());
                ps.setString(4, cliente.getTelefono());
                ps.setString(5, cliente.getCorreoElectronico());
                ps.setString(6, cliente.getDireccion());
                ps.setString(7, cliente.getEstado());

                int filas = ps.executeUpdate();
                if (filas > 0) {
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (rs.next()) {
                            cliente.setIdCliente(rs.getInt(1));
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public boolean actualizar(Cliente cliente) throws SQLException {
        String sql = "UPDATE clientes SET dui = ?, nombre_completo = ?, telefono = ?, correo_electronico = ?, direccion = ?, estado = ? " +
                     "WHERE codigo_cliente = ? OR id_cliente = ?";

        try (Connection con = Conexion.conectar()) {
            if (con == null) throw new SQLException("No se pudo conectar a la base de datos.");

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, cliente.getDui());
                ps.setString(2, cliente.getNombreCompleto());
                ps.setString(3, cliente.getTelefono());
                ps.setString(4, cliente.getCorreoElectronico());
                ps.setString(5, cliente.getDireccion());
                ps.setString(6, cliente.getEstado());
                ps.setString(7, cliente.getCodigoCliente());
                ps.setInt(8, cliente.getIdCliente());

                return ps.executeUpdate() > 0;
            }
        }
    }

    public boolean eliminar(int idCliente) throws SQLException {
        String sql = "DELETE FROM clientes WHERE id_cliente = ?";

        try (Connection con = Conexion.conectar()) {
            if (con == null) throw new SQLException("No se pudo conectar a la base de datos.");

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, idCliente);
                return ps.executeUpdate() > 0;
            }
        }
    }

    public List<Cliente> listarTodos() throws SQLException {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT id_cliente, codigo_cliente, dui, nombre_completo, telefono, correo_electronico, direccion, estado " +
                     "FROM clientes ORDER BY id_cliente DESC";

        try (Connection con = Conexion.conectar()) {
            if (con == null) throw new SQLException("No se pudo conectar a la base de datos.");

            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    Cliente c = new Cliente(
                        rs.getInt("id_cliente"),
                        rs.getString("codigo_cliente"),
                        rs.getString("dui"),
                        rs.getString("nombre_completo"),
                        rs.getString("telefono"),
                        rs.getString("correo_electronico"),
                        rs.getString("direccion"),
                        rs.getString("estado")
                    );
                    lista.add(c);
                }
            }
        }
        return lista;
    }

    public Cliente buscarPorCodigo(String codigoCliente) throws SQLException {
        String sql = "SELECT id_cliente, codigo_cliente, dui, nombre_completo, telefono, correo_electronico, direccion, estado " +
                     "FROM clientes WHERE codigo_cliente = ?";

        try (Connection con = Conexion.conectar()) {
            if (con == null) throw new SQLException("No se pudo conectar a la base de datos.");

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, codigoCliente);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new Cliente(
                            rs.getInt("id_cliente"),
                            rs.getString("codigo_cliente"),
                            rs.getString("dui"),
                            rs.getString("nombre_completo"),
                            rs.getString("telefono"),
                            rs.getString("correo_electronico"),
                            rs.getString("direccion"),
                            rs.getString("estado")
                        );
                    }
                }
            }
        }
        return null;
    }

    public Cliente buscarPorId(int idCliente) throws SQLException {
        String sql = "SELECT id_cliente, codigo_cliente, dui, nombre_completo, telefono, correo_electronico, direccion, estado " +
                     "FROM clientes WHERE id_cliente = ?";

        try (Connection con = Conexion.conectar()) {
            if (con == null) throw new SQLException("No se pudo conectar a la base de datos.");

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, idCliente);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new Cliente(
                            rs.getInt("id_cliente"),
                            rs.getString("codigo_cliente"),
                            rs.getString("dui"),
                            rs.getString("nombre_completo"),
                            rs.getString("telefono"),
                            rs.getString("correo_electronico"),
                            rs.getString("direccion"),
                            rs.getString("estado")
                        );
                    }
                }
            }
        }
        return null;
    }

    public Cliente buscarPorDui(String dui) throws SQLException {
        String sql = "SELECT id_cliente, codigo_cliente, dui, nombre_completo, telefono, correo_electronico, direccion, estado " +
                     "FROM clientes WHERE dui = ?";

        try (Connection con = Conexion.conectar()) {
            if (con == null) throw new SQLException("No se pudo conectar a la base de datos.");

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, dui);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new Cliente(
                            rs.getInt("id_cliente"),
                            rs.getString("codigo_cliente"),
                            rs.getString("dui"),
                            rs.getString("nombre_completo"),
                            rs.getString("telefono"),
                            rs.getString("correo_electronico"),
                            rs.getString("direccion"),
                            rs.getString("estado")
                        );
                    }
                }
            }
        }
        return null;
    }
}
