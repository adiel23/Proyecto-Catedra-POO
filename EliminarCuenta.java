import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EliminarCuenta {

    public static void main(String[] args) {

        String sql = "DELETE FROM cuentas_bancarias WHERE id_cuenta = ?";

        try (Connection conexion = Conexion.conectar()) {

            if (conexion == null) {
                System.out.println("No se pudo conectar con la base de datos.");
                return;
            }

            try (PreparedStatement consulta = conexion.prepareStatement(sql)) {

                consulta.setInt(1, 3);

                int filas = consulta.executeUpdate();

                if (filas > 0) {
                    System.out.println("Cuenta bancaria eliminada correctamente.");
                } else {
                    System.out.println("No se encontró la cuenta bancaria.");
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al eliminar cuenta: " + e.getMessage());
        }
    }
}