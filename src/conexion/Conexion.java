package conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Conexion {

    private static final String URL = "jdbc:mysql://localhost:3306/banco_academico?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USUARIO = "root";

    public static Connection conectar() {
        try {
            // Cargar driver explícitamente para compatibilidad
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ignored) {
            // Driver moderno se autocarga si está en el classpath
        }

        String contrasena = System.getenv("DB_PASSWORD");
        if (contrasena == null) {
            contrasena = ""; // Contraseña vacía por defecto si no está definida en el entorno
        }

        try {
            Connection conexion = DriverManager.getConnection(URL, USUARIO, contrasena);
            return conexion;
        } catch (SQLException e) {
            System.err.println("Error al conectar con MySQL: " + e.getMessage());
            return null;
        }
    }

    public static boolean probarConexion() {
        try (Connection con = conectar()) {
            return con != null && !con.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    public static void cerrar(Connection con) {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar conexión: " + e.getMessage());
            }
        }
    }

    public static void cerrar(Connection con, Statement stmt, ResultSet rs) {
        if (rs != null) {
            try { rs.close(); } catch (SQLException ignored) {}
        }
        if (stmt != null) {
            try { stmt.close(); } catch (SQLException ignored) {}
        }
        cerrar(con);
    }
}
