import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MovimientoDAO {

    private static final Logger logger =
            LogManager.getLogger(MovimientoDAO.class);

    // METODO PARA CONSULTAR MOVIMIENTOS
    public void consultarMovimientos() {

        Connection conexion = Conexion.conectar();

        if (conexion == null) {
            logger.error("No se pudieron consultar los movimientos porque fallo la conexión.");
            return;
        }

        String sql = "SELECT * FROM movimientos";
        boolean hayMovimientos = false;

        try (PreparedStatement pst = conexion.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            logger.info("Consulta de movimientos ejecutada correctamente.");

            while (rs.next()) {
                hayMovimientos = true;

                int id = rs.getInt("id_movimiento");
                String tipo = rs.getString("tipo_movimiento");
                double monto = rs.getDouble("monto");
                String fecha = rs.getString("fecha_movimiento");

                System.out.println("ID: " + id);
                System.out.println("Tipo: " + tipo);
                System.out.println("Monto: $" + monto);
                System.out.println("Fecha: " + fecha);
                System.out.println("-------------------------");
            }

            if (!hayMovimientos) {
                System.out.println("No hay movimientos registrados.");
                logger.info("No se encontraron movimientos registrados.");
            }

        } catch (SQLException e) {
            logger.error("Error al consultar los movimientos.", e);

        } finally {
            try {
                conexion.close();
                logger.info("Conexión cerrada correctamente.");
            } catch (SQLException e) {
                logger.error("Error al cerrar la conexión.", e);
            }
        }
    }

    // METODO PARA REGISTRAR MOVIMIENTOS
    public void registrarMovimiento(
            String tipo,
            double monto,
            String descripcion,
            int idCuenta,
            Integer idCuentaDestino) {

        Connection conexion = Conexion.conectar();

        if (conexion == null) {
            logger.error("No se pudo registrar el movimiento por fallo de conexión.");
            return;
        }

        String sql = "INSERT INTO movimientos "
                + "(tipo_movimiento, monto, descripcion, id_cuenta, id_cuenta_destino) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pst = conexion.prepareStatement(sql)) {

            pst.setString(1, tipo);
            pst.setDouble(2, monto);
            pst.setString(3, descripcion);
            pst.setInt(4, idCuenta);

            if (idCuentaDestino == null) {
                pst.setNull(5, java.sql.Types.INTEGER);
            } else {
                pst.setInt(5, idCuentaDestino);
            }

            pst.executeUpdate();

            logger.info("Movimiento registrado correctamente.");

        } catch (SQLException e) {
            logger.error("Error al registrar el movimiento.", e);

        } finally {
            try {
                conexion.close();
                logger.info("Conexión cerrada correctamente.");
            } catch (SQLException e) {
                logger.error("Error al cerrar la conexión.", e);
            }
        }
    }
}