import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ActualizarCliente {

    public static void main(String[] args) {

        String sql = "UPDATE clientes SET telefono = ? WHERE id_cliente = ?";

        try (Connection conexion = Conexion.conectar()) {

            if (conexion == null) {
                System.out.println("No se pudo conectar con la base de datos.");
                return;
            }

            try (PreparedStatement consulta = conexion.prepareStatement(sql)) {

                consulta.setString(1, "75556666");
                consulta.setInt(2, 1);

                int filas = consulta.executeUpdate();

                if (filas > 0) {
                    System.out.println("Cliente actualizado correctamente.");
                } else {
                    System.out.println("No se encontró el cliente.");
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al actualizar cliente: " + e.getMessage());
        }
    }
}
