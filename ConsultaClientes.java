import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConsultaClientes {

    public static void main(String[] args) {

        String sql = "SELECT * FROM clientes";

        try (Connection conexion = Conexion.conectar();
             PreparedStatement consulta = conexion.prepareStatement(sql);
             ResultSet resultado = consulta.executeQuery()) {

            System.out.println("LISTA DE CLIENTES");
            System.out.println("-------------------------");

            while (resultado.next()) {

                System.out.println("ID: " + resultado.getInt("id_cliente"));
                System.out.println("Código: " + resultado.getString("codigo_cliente"));
                System.out.println("Nombre: " + resultado.getString("nombre_completo"));
                System.out.println("Teléfono: " + resultado.getString("telefono"));
                System.out.println("-------------------------");
            }

        } catch (SQLException e) {
            System.out.println("Error al consultar clientes: " + e.getMessage());
        }
    }
}