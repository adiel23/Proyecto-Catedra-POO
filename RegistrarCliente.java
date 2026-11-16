import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegistrarCliente {

    public static void main(String[] args) {

        String sql = "INSERT INTO clientes " +
                "(codigo_cliente, dui, nombre_completo, telefono, correo_electronico, direccion) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conexion = Conexion.conectar()) {

            if (conexion == null) {
                System.out.println("No se pudo conectar con la base de datos.");
                return;
            }

            try (PreparedStatement consulta = conexion.prepareStatement(sql)) {

                consulta.setString(1, "C003");
                consulta.setString(2, "01234567-8");
                consulta.setString(3, "Maria Lopez");
                consulta.setString(4, "70002222");
                consulta.setString(5, "maria.prueba@gmail.com");
                consulta.setString(6, "San Salvador");

                int filas = consulta.executeUpdate();

                if (filas > 0) {
                    System.out.println("Cliente registrado correctamente.");
                }

            }

        } catch (SQLException e) {
            System.out.println("Error al registrar cliente: " + e.getMessage());
        }
    }
}
