import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegistrarCuenta {

    public static void main(String[] args) {

        String sql = "INSERT INTO cuentas_bancarias " +
                "(numero_cuenta, tipo_cuenta, saldo, id_cliente) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection conexion = Conexion.conectar()) {

            if (conexion == null) {
                System.out.println("No se pudo conectar con la base de datos.");
                return;
            }

            try (PreparedStatement consulta = conexion.prepareStatement(sql)) {

                consulta.setString(1, "100000003");
                consulta.setString(2, "Ahorro");
                consulta.setBigDecimal(3, new java.math.BigDecimal("300.00"));
                consulta.setInt(4, 1);

                int filas = consulta.executeUpdate();

                if (filas > 0) {
                    System.out.println("Cuenta bancaria registrada correctamente.");
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al registrar cuenta: " + e.getMessage());
        }
    }
}
