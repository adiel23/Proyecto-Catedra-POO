<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="modelo.Cliente" %>
<%
    String rol = (String) session.getAttribute("rol");
    if (rol == null || !"ADMIN".equalsIgnoreCase(rol)) {
        session.setAttribute("flash_error", "Acceso denegado: Se requieren permisos de Administrador.");
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
    List<Cliente> clientes = (List<Cliente>) request.getAttribute("listaClientes");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Apertura de Cuenta Bancaria - Banco Académico UDB</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/styles.css">
</head>
<body>
    <jsp:include page="/jsp/common/navbar.jsp" />

    <div class="container">
        <div class="page-header">
            <div>
                <h1>Apertura de Nueva Cuenta Bancaria</h1>
                <p>Asignación de cuenta corriente o de ahorros a un cliente registrado.</p>
            </div>
            <div>
                <a href="<%= request.getContextPath() %>/jsp/admin/cuentas-lista.jsp" class="btn btn-outline">&larr; Volver a Cuentas</a>
            </div>
        </div>

        <jsp:include page="/jsp/common/alerts.jsp" />

        <div class="card" style="max-width: 750px; margin: 0 auto;">
            <div class="card-header">
                <span>Datos de la Nueva Cuenta</span>
            </div>
            <div class="card-body">
                <form action="<%= request.getContextPath() %>/CuentaServlet" method="POST">
                    <input type="hidden" name="accion" value="insertar">

                    <div class="form-grid">
                        <div class="form-group">
                            <label class="form-label" for="numeroCuenta">Número de Cuenta *</label>
                            <input type="text" id="numeroCuenta" name="numeroCuenta" class="form-control" 
                                   placeholder="Ej. 7249971 o 0012345678" required>
                            <small style="color: var(--text-muted);">Debe ser único en el sistema.</small>
                        </div>

                        <div class="form-group">
                            <label class="form-label" for="tipoCuenta">Tipo de Cuenta *</label>
                            <select id="tipoCuenta" name="tipoCuenta" class="form-control" required>
                                <option value="Ahorro">Cuenta de Ahorro</option>
                                <option value="Corriente">Cuenta Corriente</option>
                            </select>
                        </div>

                        <div class="form-group">
                            <label class="form-label" for="saldoInicial">Saldo Inicial ($) *</label>
                            <input type="number" id="saldoInicial" name="saldoInicial" class="form-control" 
                                   step="0.01" min="0.00" value="0.00" required>
                            <small style="color: var(--text-muted);">Monto inicial de apertura.</small>
                        </div>

                        <div class="form-group">
                            <label class="form-label" for="idCliente">Cliente Titular (ID) *</label>
                            <% if (clientes != null && !clientes.isEmpty()) { %>
                                <select id="idCliente" name="idCliente" class="form-control" required>
                                    <% for (Cliente c : clientes) { %>
                                        <option value="<%= c.getIdCliente() %>">
                                            <%= c.getCodigoCliente() %> - <%= c.getNombreCompleto() %> (DUI: <%= c.getDui() %>)
                                        </option>
                                    <% } %>
                                </select>
                            <% } else { %>
                                <input type="number" id="idCliente" name="idCliente" class="form-control" 
                                       placeholder="Ingrese el ID del Cliente (Ej. 1)" required>
                                <small style="color: var(--text-muted);">Si no ve la lista desplegable, ingrese el ID numérico.</small>
                            <% } %>
                        </div>
                    </div>

                    <div class="form-actions">
                        <a href="<%= request.getContextPath() %>/jsp/admin/cuentas-lista.jsp" class="btn btn-outline">Cancelar</a>
                        <button type="submit" class="btn btn-primary">Aperturar Cuenta</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <jsp:include page="/jsp/common/footer.jsp" />
</body>
</html>
