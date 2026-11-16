import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Conexion {

    private static final Logger logger = LogManager.getLogger(Conexion.class);

    private static final String URL =  "jdbc:mysql://localhost:3306/banco_academico";
    private static final String USUARIO = "root";
    private static final String CONTRASENA = System.getenv("DB_PASSWORD");

    public static Connection conectar() {

        try {
            Connection conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA);

            logger.info("Conexión exitosa con MySQL.");

            return conexion;

        } catch (SQLException e) {

            logger.error("Error al conectar con MySQL: {}", e.getMessage(), e);
            return null;
        }

    }
}