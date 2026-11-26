<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="modelo.ReporteMovimiento" %>
<%
    String rol = (String) session.getAttribute("rol");
    if (rol == null || !"CLIENTE".equalsIgnoreCase(rol)) {
        session.setAttribute("flash_error", "Acceso denegado: Se requiere sesión de Cliente.");
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    String cuentaSeleccionada = (String) request.getAttribute("cuentaSeleccionada");
    if (cuentaSeleccionada == null) {
        cuentaSeleccionada = request.getParameter("cuenta");
        if (cuentaSeleccionada == null) cuentaSeleccionada = "";
    }

    List<ReporteMovimiento> movimientos = (List<ReporteMovimiento>) request.getAttribute("listaMovimientos");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Historial de Movimientos - Banco Académico UDB</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/styles.css">
</head>
<body>
    <jsp:include page="/jsp/common/navbar.jsp" />

    <div class="container">
        <div class="page-header">
            <div>
                <h1>Historial de Movimientos de Cuenta</h1>
                <p>Consulte en tiempo real los débitos y créditos aplicados a sus cuentas.</p>
            </div>
            <div>
                <a href="<%= request.getContextPath() %>/jsp/cliente/dashboard.jsp" class="btn btn-outline">&larr; Volver a Mis Cuentas</a>
            </div>
        </div>

        <jsp:include page="/jsp/common/alerts.jsp" />

        <div class="card">
            <div class="card-header">
                <span>Filtrar por Número de Cuenta</span>
            </div>
            <div class="card-body">
                <form action="<%= request.getContextPath() %>/ClientePortalServlet" method="GET">
                    <input type="hidden" name="accion" value="movimientos">
                    <div style="display: flex; gap: 1rem; align-items: center; max-width: 500px;">
                        <input type="text" name="cuenta" class="form-control" placeholder="Ingrese su número de cuenta" 
                               value="<%= cuentaSeleccionada %>" required>
                        <button type="submit" class="btn btn-primary">Consultar</button>
                    </div>
                </form>
            </div>
        </div>

        <div class="card">
            <div class="card-header">
                <span>Movimientos Registrados <%= !cuentaSeleccionada.isEmpty() ? "para Cuenta: " + cuentaSeleccionada : "" %></span>
            </div>
            <div class="card-body">
                <div class="table-responsive">
                    <table class="bank-table">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Tipo</th>
                                <th>Cuenta Origen</th>
                                <th>Cuenta Destino</th>
                                <th>Monto ($)</th>
                                <th>Descripción</th>
                                <th>Fecha y Hora</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% if (movimientos != null && !movimientos.isEmpty()) {
                                for (ReporteMovimiento m : movimientos) { %>
                                    <tr>
                                        <td><%= m.getIdMovimiento() %></td>
                                        <td>
                                            <span class="badge badge-<%= m.getTipoMovimiento().toLowerCase() %>">
                                                <%= m.getTipoMovimiento() %>
                                            </span>
                                        </td>
                                        <td><strong><%= m.getNumeroCuentaOrigen() %></strong></td>
                                        <td><%= m.getNumeroCuentaDestino() != null ? m.getNumeroCuentaDestino() : "N/A" %></td>
                                        <td>
                                            <% if ("Deposito".equalsIgnoreCase(m.getTipoMovimiento())) { %>
                                                <strong style="color: var(--success-color);">+ $ <%= String.format("%.2f", m.getMonto()) %></strong>
                                            <% } else if ("Retiro".equalsIgnoreCase(m.getTipoMovimiento())) { %>
                                                <strong style="color: var(--danger-color);">- $ <%= String.format("%.2f", m.getMonto()) %></strong>
                                            <% } else { %>
                                                <strong style="color: var(--info-color);">$ <%= String.format("%.2f", m.getMonto()) %></strong>
                                            <% } %>
                                        </td>
                                        <td><%= m.getDescripcion() != null ? m.getDescripcion() : "-" %></td>
                                        <td><%= m.getFechaMovimiento() %></td>
                                    </tr>
                            <%  }
                               } else { %>
                                    <tr>
                                        <td colspan="7" style="text-align: center; color: var(--text-muted); padding: 2rem;">
                                            No se han encontrado movimientos o no ha seleccionado una cuenta bancaria.
                                        </td>
                                    </tr>
                            <% } %>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>

    <jsp:include page="/jsp/common/footer.jsp" />
</body>
</html>
