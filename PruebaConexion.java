import java.sql.Connection;

public class PruebaConexion {

    public static void main(String[] args) {

        Connection conexion = Conexion.conectar();

        if (conexion != null) {
            System.out.println("Java esta conectado correctamente con MySQL.");

            try {
                conexion.close();
                System.out.println("Conexion cerrada correctamente.");
            } catch (Exception e) {
                System.out.println("Error al cerrar la conexion.");
            }
        } else {
            System.out.println("No se pudo establecer la conexion.");
        }
    }
}