package servlet;

import dao.ReporteDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

@WebServlet("/ReporteServlet")
public class ReporteServlet extends HttpServlet {

    private final ReporteDAO reporteDAO = new ReporteDAO();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        // Verificar que el usuario tenga sesión de administrador
        HttpSession session = request.getSession(false);

        if (session == null || !"ADMIN".equals(session.getAttribute("rol"))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN,
                    "No tienes permisos para acceder a los reportes.");
            return;
        }

        String accion = request.getParameter("accion");

        // Valores iniciales
        String tipoReporte = request.getParameter("tipoReporte");
        String numeroCuenta = request.getParameter("numeroCuenta");

        if (tipoReporte == null || tipoReporte.isEmpty()) {
            tipoReporte = "1";
        }

        if (numeroCuenta == null) {
            numeroCuenta = "";
        }

        try {
            // Obtener los totales de movimientos
            double totalDepositado =
                    reporteDAO.obtenerTotalPorTipoMovimiento("Deposito");

            double totalRetirado =
                    reporteDAO.obtenerTotalPorTipoMovimiento("Retiro");

            double totalTransferido =
                    reporteDAO.obtenerTotalPorTipoMovimiento("Transferencia");

            request.setAttribute("totalDepositado", totalDepositado);
            request.setAttribute("totalRetirado", totalRetirado);
            request.setAttribute("totalTransferido", totalTransferido);

            // Guardar los filtros seleccionados
            request.setAttribute("tipoReporte", tipoReporte);
            request.setAttribute("numCuentaFiltro", numeroCuenta);

            // Preparar los resultados
            List<?> resultados = Collections.emptyList();

            // Generar el reporte seleccionado
            if ("generar".equals(accion)) {

                switch (tipoReporte) {

                    case "1":
                        // Cuentas activas
                        resultados = reporteDAO.listarCuentasPorEstado("Activa");
                        break;

                    case "2":
                        // Cuentas bloqueadas
                        resultados = reporteDAO.listarCuentasPorEstado("Bloqueada");
                        break;

                    case "3":
                        // Clientes registrados
                        resultados = reporteDAO.listarClientesRegistrados();
                        break;

                    case "4":
                        // Historial general de transacciones
                        resultados = reporteDAO.listarHistorialTransacciones();
                        break;

                    case "5":
                        // Transacciones por número de cuenta
                        if (numeroCuenta.trim().isEmpty()) {
                            request.setAttribute("mensajeError",
                                    "Debes ingresar un número de cuenta.");
                        } else {
                            resultados = reporteDAO.listarTransaccionesPorCuenta(
                                    numeroCuenta.trim());
                        }
                        break;

                    default:
                        request.setAttribute("mensajeError",
                                "El tipo de reporte seleccionado no es válido.");
                        tipoReporte = "1";
                        request.setAttribute("tipoReporte", tipoReporte);
                        break;
                }
            }

            request.setAttribute("resultadosReporte", resultados);

            // Mostrar la página de reportes
            request.getRequestDispatcher("/jsp/admin/reportes.jsp")
                    .forward(request, response);

        } catch (SQLException e) {
            throw new ServletException(
                    "Error al consultar la información de los reportes.", e);
        }
    }
}


