<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="modelo.CuentaBancaria" %>
<%
    String rol = (String) session.getAttribute("rol");
    if (rol == null || !"CLIENTE".equalsIgnoreCase(rol)) {
        session.setAttribute("flash_error", "Acceso denegado: Se requiere sesión de Cliente.");
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    String clienteNombre = (String) session.getAttribute("usuario");
    List<CuentaBancaria> misCuentas = (List<CuentaBancaria>) request.getAttribute("misCuentas");
    Double saldoTotal = 0.0;
    if (misCuentas != null) {
        for (CuentaBancaria c : misCuentas) {
            saldoTotal += c.getSaldo();
        }
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Portal del Cliente - Banco Académico UDB</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/styles.css">
</head>
<body>
    <jsp:include page="/jsp/common/navbar.jsp" />

    <div class="container">
        <div class="page-header">
            <div>
                <h1>Bienvenido(a), <%= clienteNombre %></h1>
                <p>Consulte sus cuentas bancarias, saldos y operaciones en línea.</p>
            </div>
            <div>
                <a href="<%= request.getContextPath() %>/jsp/cliente/transferencia.jsp" class="btn btn-primary">
                    + Transferir Dinero
                </a>
            </div>
        </div>

        <jsp:include page="/jsp/common/alerts.jsp" />

        <!-- Tarjeta de Saldo Consolidado -->
        <div class="stats-grid">
            <div class="stat-card deposit">
                <span class="stat-title">Saldo Total Consolidado</span>
                <span class="stat-value">$ <%= String.format("%.2f", saldoTotal) %></span>
                <small style="color: var(--text-muted);">Total en todas sus cuentas activas</small>
            </div>

            <div class="stat-card transfer">
                <span class="stat-title">Cuentas Registradas</span>
                <span class="stat-value" style="color: var(--info-color);"><%= misCuentas != null ? misCuentas.size() : 0 %></span>
                <small style="color: var(--text-muted);">Cuentas de Ahorro y Corriente</small>
            </div>
        </div>

        <!-- Tabla de Cuentas del Cliente -->
        <div class="card">
            <div class="card-header">
                <span>Mis Cuentas Bancarias</span>
                <form action="<%= request.getContextPath() %>/ClientePortalServlet" method="GET" style="display: inline-block;">
                    <button type="submit" class="btn btn-sm btn-outline">Actualizar Saldos</button>
                </form>
            </div>
            <div class="card-body">
                <div class="table-responsive">
                    <table class="bank-table">
                        <thead>
                            <tr>
                                <th>Número de Cuenta</th>
                                <th>Tipo de Cuenta</th>
                                <th>Saldo Disponible ($)</th>
                                <th>Estado</th>
                                <th>Acción</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% if (misCuentas != null && !misCuentas.isEmpty()) {
                                for (CuentaBancaria c : misCuentas) { %>
                                    <tr>
                                        <td><strong><%= c.getNumeroCuenta() %></strong></td>
                                        <td><%= c.getTipoCuenta() %></td>
                                        <td><strong style="color: var(--success-color); font-size: 1.1rem;">$ <%= String.format("%.2f", c.getSaldo()) %></strong></td>
                                        <td>
                                            <span class="badge <%= "Activa".equalsIgnoreCase(c.getEstadoCuenta()) ? "badge-active" : "badge-blocked" %>">
                                                <%= c.getEstadoCuenta() %>
                                            </span>
                                        </td>
                                        <td>
                                            <a href="<%= request.getContextPath() %>/ClientePortalServlet?accion=movimientos&cuenta=<%= c.getNumeroCuenta() %>" 
                                               class="btn btn-sm btn-outline">Ver Movimientos</a>
                                        </td>
                                    </tr>
                            <%  }
                               } else { %>
                                    <tr>
                                        <td colspan="5" style="text-align: center; color: var(--text-muted); padding: 2rem;">
                                            No se han cargado cuentas para este usuario.<br>
                                            <a href="<%= request.getContextPath() %>/ClientePortalServlet" class="btn btn-sm btn-primary" style="margin-top: 0.5rem;">Cargar Información Bancaria</a>
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
