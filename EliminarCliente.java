import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EliminarCliente {

    public static void main(String[] args) {

        String sql = "DELETE FROM clientes WHERE id_cliente = ?";

        try (Connection conexion = Conexion.conectar()) {

            if (conexion == null) {
                System.out.println("No se pudo conectar con la base de datos.");
                return;
            }

            try (PreparedStatement consulta = conexion.prepareStatement(sql)) {

                consulta.setInt(1, 3);

                int filas = consulta.executeUpdate();

                if (filas > 0) {
                    System.out.println("Cliente eliminado correctamente.");
                } else {
                    System.out.println("No se encontró el cliente.");
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al eliminar cliente: " + e.getMessage());
        }
    }
}

