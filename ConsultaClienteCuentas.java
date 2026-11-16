import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConsultaClienteCuentas {

    public static void main(String[] args) {

        String sql = "SELECT " +
                "clientes.nombre_completo, " +
                "clientes.dui, " +
                "cuentas_bancarias.numero_cuenta, " +
                "cuentas_bancarias.tipo_cuenta, " +
                "cuentas_bancarias.saldo " +
                "FROM clientes " +
                "INNER JOIN cuentas_bancarias " +
                "ON clientes.id_cliente = cuentas_bancarias.id_cliente";

        try (Connection conexion = Conexion.conectar()) {

            if (conexion == null) {
                System.out.println("No se pudo conectar con la base de datos.");
                return;
            }

            try (PreparedStatement consulta = conexion.prepareStatement(sql);
                 ResultSet resultado = consulta.executeQuery()) {

                System.out.println("CLIENTES Y SUS CUENTAS BANCARIAS");
                System.out.println("--------------------------------");

                while (resultado.next()) {

                    System.out.println("Nombre: " +
                            resultado.getString("nombre_completo"));

                    System.out.println("DUI: " +
                            resultado.getString("dui"));

                    System.out.println("Número de cuenta: " +
                            resultado.getString("numero_cuenta"));

                    System.out.println("Tipo de cuenta: " +
                            resultado.getString("tipo_cuenta"));

                    System.out.println("Saldo: $" +
                            resultado.getBigDecimal("saldo"));

                    System.out.println("--------------------------------");
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al consultar clientes y cuentas: "
                    + e.getMessage());
        }
    }
}