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

    List<CuentaBancaria> misCuentas = (List<CuentaBancaria>) request.getAttribute("misCuentas");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Transferencia en Línea - Banco Académico UDB</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/styles.css">
</head>
<body>
    <jsp:include page="/jsp/common/navbar.jsp" />

    <div class="container">
        <div class="page-header">
            <div>
                <h1>Transferencia entre Cuentas</h1>
                <p>Envíe dinero de forma segura a cualquier cuenta bancaria activa.</p>
            </div>
            <div>
                <a href="<%= request.getContextPath() %>/jsp/cliente/dashboard.jsp" class="btn btn-outline">&larr; Volver a Mis Cuentas</a>
            </div>
        </div>

        <jsp:include page="/jsp/common/alerts.jsp" />

        <div class="card" style="max-width: 650px; margin: 0 auto;">
            <div class="card-header" style="background-color: #dbeafe; color: #1e40af;">
                <span>Datos de la Transferencia Bancaria</span>
            </div>
            <div class="card-body">
                <form action="<%= request.getContextPath() %>/TransaccionServlet" method="POST">
                    <input type="hidden" name="tipoTransaccion" value="Transferencia">

                    <div class="form-grid">
                        <div class="form-group">
                            <label class="form-label" for="numeroCuenta">Seleccione Cuenta de Origen *</label>
                            <% if (misCuentas != null && !misCuentas.isEmpty()) { %>
                                <select id="numeroCuenta" name="numeroCuenta" class="form-control" required>
                                    <% for (CuentaBancaria c : misCuentas) { %>
                                        <option value="<%= c.getNumeroCuenta() %>">
                                            <%= c.getNumeroCuenta() %> - <%= c.getTipoCuenta() %> ($<%= String.format("%.2f", c.getSaldo()) %>)
                                        </option>
                                    <% } %>
                                </select>
                            <% } else { %>
                                <input type="text" id="numeroCuenta" name="numeroCuenta" class="form-control" 
                                       placeholder="Ingrese su número de cuenta" required>
                            <% } %>
                        </div>

                        <div class="form-group">
                            <label class="form-label" for="numeroCuentaDestino">Cuenta de Destino *</label>
                            <input type="text" id="numeroCuentaDestino" name="numeroCuentaDestino" class="form-control" 
                                   placeholder="Número de cuenta que recibirá los fondos" required>
                            <small style="color: var(--text-muted);">Debe ser una cuenta activa diferente a la de origen.</small>
                        </div>

                        <div class="form-group col-full">
                            <label class="form-label" for="monto">Monto a Transferir ($) *</label>
                            <input type="number" id="monto" name="monto" class="form-control" 
                                   step="0.01" min="0.01" placeholder="0.00" required>
                        </div>

                        <div class="form-group col-full">
                            <label class="form-label" for="descripcion">Concepto / Motivo</label>
                            <input type="text" id="descripcion" name="descripcion" class="form-control" 
                                   placeholder="Ej. Transferencia personal, pago de servicios" value="Transferencia Web">
                        </div>
                    </div>

                    <div class="form-actions">
                        <a href="<%= request.getContextPath() %>/jsp/cliente/dashboard.jsp" class="btn btn-outline">Cancelar</a>
                        <button type="submit" class="btn btn-primary" data-confirm="¿Está seguro de confirmar esta transferencia?">
                            Confirmar Transferencia
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <jsp:include page="/jsp/common/footer.jsp" />
</body>
</html>
