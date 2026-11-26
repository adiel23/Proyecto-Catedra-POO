<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="modelo.CuentaBancaria" %>
<%
    String rol = (String) session.getAttribute("rol");
    if (rol == null || !"ADMIN".equalsIgnoreCase(rol)) {
        session.setAttribute("flash_error", "Acceso denegado: Se requieren permisos de Administrador.");
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
    List<CuentaBancaria> cuentas = (List<CuentaBancaria>) request.getAttribute("listaCuentas");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestión de Cuentas Bancarias - Banco Académico UDB</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/styles.css">
</head>
<body>
    <jsp:include page="/jsp/common/navbar.jsp" />

    <div class="container">
        <div class="page-header">
            <div>
                <h1>Gestión de Cuentas Bancarias</h1>
                <p>Apertura, consulta de saldos y control de estados (Activa, Inactiva, Bloqueada).</p>
            </div>
            <div>
                <a href="<%= request.getContextPath() %>/jsp/admin/cuenta-form.jsp" class="btn btn-primary">
                    + Nueva Cuenta
                </a>
            </div>
        </div>

        <jsp:include page="/jsp/common/alerts.jsp" />

        <div class="card">
            <div class="card-header">
                <span>Cuentas Registradas en el Sistema</span>
                <form action="<%= request.getContextPath() %>/CuentaServlet" method="GET" style="display: inline-block;">
                    <input type="hidden" name="accion" value="listar">
                    <button type="submit" class="btn btn-sm btn-outline">Recargar Lista</button>
                </form>
            </div>
            <div class="card-body">
                <div class="table-responsive">
                    <table class="bank-table">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Número de Cuenta</th>
                                <th>Tipo</th>
                                <th>Saldo ($)</th>
                                <th>ID Titular</th>
                                <th>Estado</th>
                                <th>Acciones</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% if (cuentas != null && !cuentas.isEmpty()) {
                                for (CuentaBancaria c : cuentas) { %>
                                    <tr>
                                        <td><%= c.getIdCuenta() %></td>
                                        <td><strong><%= c.getNumeroCuenta() %></strong></td>
                                        <td><%= c.getTipoCuenta() %></td>
                                        <td><strong style="color: var(--success-color);">$ <%= String.format("%.2f", c.getSaldo()) %></strong></td>
                                        <td>Cliente #<%= c.getIdCliente() %></td>
                                        <td>
                                            <% if ("Activa".equalsIgnoreCase(c.getEstadoCuenta())) { %>
                                                <span class="badge badge-active">Activa</span>
                                            <% } else if ("Inactiva".equalsIgnoreCase(c.getEstadoCuenta())) { %>
                                                <span class="badge badge-inactive">Inactiva</span>
                                            <% } else { %>
                                                <span class="badge badge-blocked">Bloqueada</span>
                                            <% } %>
                                        </td>
                                        <td>
                                            <a href="<%= request.getContextPath() %>/CuentaServlet?accion=cambiarEstado&id=<%= c.getIdCuenta() %>&estado=Bloqueada" 
                                               class="btn btn-sm btn-danger" data-confirm="¿Desea bloquear esta cuenta bancaria?">Bloquear</a>
                                            <a href="<%= request.getContextPath() %>/CuentaServlet?accion=cambiarEstado&id=<%= c.getIdCuenta() %>&estado=Activa" 
                                               class="btn btn-sm btn-outline">Activar</a>
                                        </td>
                                    </tr>
                            <%  }
                               } else { %>
                                    <tr>
                                        <td colspan="7" style="text-align: center; color: var(--text-muted); padding: 2rem;">
                                            No hay cuentas cargadas actualmente.<br>
                                            <a href="<%= request.getContextPath() %>/CuentaServlet?accion=listar" class="btn btn-sm btn-primary" style="margin-top: 0.5rem;">Cargar Cuentas desde la Base de Datos</a>
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
