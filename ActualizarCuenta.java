import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ActualizarCuenta {

    public static void main(String[] args) {

        String sql = "UPDATE cuentas_bancarias SET saldo = ? WHERE id_cuenta = ?";

        try (Connection conexion = Conexion.conectar()) {

            if (conexion == null) {
                System.out.println("No se pudo conectar con la base de datos.");
                return;
            }

            try (PreparedStatement consulta = conexion.prepareStatement(sql)) {

                consulta.setBigDecimal(1, new java.math.BigDecimal("1200.00"));
                consulta.setInt(2, 2);

                int filas = consulta.executeUpdate();

                if (filas > 0) {
                    System.out.println("Cuenta bancaria actualizada correctamente.");
                } else {
                    System.out.println("No se encontró la cuenta bancaria.");
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al actualizar cuenta: " + e.getMessage());
        }
    }
}
