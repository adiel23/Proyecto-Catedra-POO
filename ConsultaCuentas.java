import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConsultaCuentas {

    public static void main(String[] args) {

        String sql = "SELECT * FROM cuentas_bancarias";

        try (Connection conexion = Conexion.conectar()) {

            if (conexion == null) {
                System.out.println("No se pudo conectar con la base de datos.");
                return;
            }

            try (PreparedStatement consulta = conexion.prepareStatement(sql);
                 ResultSet resultado = consulta.executeQuery()) {

                System.out.println("LISTA DE CUENTAS BANCARIAS");
                System.out.println("-------------------------");

                while (resultado.next()) {

                    System.out.println("ID de cuenta: " + resultado.getInt("id_cuenta"));
                    System.out.println("Número de cuenta: " + resultado.getString("numero_cuenta"));
                    System.out.println("Tipo de cuenta: " + resultado.getString("tipo_cuenta"));
                    System.out.println("Saldo: $" + resultado.getBigDecimal("saldo"));
                    System.out.println("Estado: " + resultado.getString("estado"));
                    System.out.println("ID del cliente: " + resultado.getInt("id_cliente"));

                    System.out.println("-------------------------");
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al consultar cuentas: " + e.getMessage());
        }
    }
}
