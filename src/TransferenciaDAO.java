import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransferenciaDAO {

    // Metodo para realizar la transferencia cumpliendo las reglas de negocio
    public boolean realizarTransferencia(String cuentaOrigen, String cuentaDestino, double monto) {
        // Regla: El monto debe ser mayor que cero
        if (monto <= 0) {
            return false;
        }

        // Regla: No se puede transferir a la misma cuenta
        if (cuentaOrigen.equals(cuentaDestino)) {
            return false;
        }

        // ====================================================================================
        // Aquí es importante usar una transacción SQL (conn.setAutoCommit(false)) para asegurar que
        // si falla el descuento o el depósito, se haga un rollback general.
        // =====================================================================================

        /*
        Connection conn = null;
        try {
            // conn = ClaseConexion.obtenerConexion(); <- Proporcionada por el Integrante 4
            conn.setAutoCommit(false);

            // 1. Validar existencia y estado de la cuenta origen
            // 2. Validar existencia y estado de la cuenta destino
            // 3. Validar saldo suficiente en origen
            // 4. Actualizar saldo origen (Restar)
            // 5. Actualizar saldo destino (Sumar)
            // 6. Registrar en la tabla de movimientos/historial

            // conn.commit();
            // return true;
        } catch (SQLException e) {
            // if (conn != null) conn.rollback();
            // e.printStackTrace();
            // return false;
        }
        */

        // Simulación para pruebas de la interfaz:
        return true;
    }

    // Consultar historial de movimientos generales o por cuenta
    public List<MovimientoBancario> obtenerHistorialMovimientos(String numeroCuentaFiltro) {
        List<MovimientoBancario> listaMovimientos = new ArrayList<>();

        // =====================================================================================
        // [INTEGRANTE 4]: SELECT A LA TABLA DE MOVIMIENTOS
        // Consulta SQL para obtener el historial filtrado por cuenta o general para los reportes.
        // String sql = "SELECT * FROM movimiento WHERE cuenta_origen = ? OR cuenta_destino = ?";
        // =====================================================================================

        return listaMovimientos;
    }
}
