package conexion;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Conexion {

    private static final Logger logger = LogManager.getLogger(Conexion.class);
    private static final String URL = "jdbc:mysql://localhost:3306/banco_academico?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USUARIO = "root";

    public static Connection conectar() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ignored) {
        }

        String contrasena = System.getenv("DB_PASSWORD");
        if (contrasena == null) {
            contrasena = "";
        }

        try {
            Connection conexion = DriverManager.getConnection(URL, USUARIO, contrasena);
            logger.info("Conexión exitosa con la base de datos banco_academico.");
            return conexion;
        } catch (SQLException e) {
            logger.error("Error al conectar con MySQL: {}", e.getMessage(), e);
            return null;
        }
    }

    public static boolean probarConexion() {
        try (Connection con = conectar()) {
            return con != null && !con.isClosed();
        } catch (SQLException e) {
            logger.error("Fallo al probar conexión JDBC: {}", e.getMessage());
            return false;
        }
    }

    public static void cerrar(Connection con) {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                logger.error("Error al cerrar conexión: {}", e.getMessage());
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