<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    String rol = (String) session.getAttribute("rol");
    if (rol == null || !"CAJERO".equalsIgnoreCase(rol)) {
        session.setAttribute("flash_error", "Acceso denegado: Se requieren permisos de Cajero Bancario.");
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Depósito en Cuenta - Banco Académico UDB</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/styles.css">
</head>
<body>
    <jsp:include page="/jsp/common/navbar.jsp" />

    <div class="container">
        <div class="page-header">
            <div>
                <h1>Depósito Bancario en Efectivo</h1>
                <p>Abono inmediato a cuentas bancarias activas.</p>
            </div>
            <div>
                <a href="<%= request.getContextPath() %>/jsp/cajero/dashboard.jsp" class="btn btn-outline">&larr; Volver al Menú de Caja</a>
            </div>
        </div>

        <jsp:include page="/jsp/common/alerts.jsp" />

        <div class="card" style="max-width: 600px; margin: 0 auto;">
            <div class="card-header" style="background-color: #dcfce7; color: #166534;">
                <span>Formulario de Depósito</span>
            </div>
            <div class="card-body">
                <form action="<%= request.getContextPath() %>/TransaccionServlet" method="POST">
                    <input type="hidden" name="tipoTransaccion" value="Deposito">

                    <div class="form-group" style="margin-bottom: 1.25rem;">
                        <label class="form-label" for="numeroCuenta">Número de Cuenta Destino *</label>
                        <input type="text" id="numeroCuenta" name="numeroCuenta" class="form-control" 
                               placeholder="Ej. 7249971" required autofocus>
                        <small style="color: var(--text-muted);">La cuenta debe existir y estar en estado 'Activa'.</small>
                    </div>

                    <div class="form-group" style="margin-bottom: 1.25rem;">
                        <label class="form-label" for="monto">Monto a Depositar ($) *</label>
                        <input type="number" id="monto" name="monto" class="form-control" 
                               step="0.01" min="0.01" placeholder="0.00" required>
                        <small style="color: var(--text-muted);">El monto debe ser estrictamente mayor a 0.</small>
                    </div>

                    <div class="form-group" style="margin-bottom: 1.75rem;">
                        <label class="form-label" for="descripcion">Concepto / Descripción</label>
                        <input type="text" id="descripcion" name="descripcion" class="form-control" 
                               placeholder="Ej. Depósito en efectivo por ventanilla" value="Depósito en ventanilla">
                    </div>

                    <div class="form-actions">
                        <a href="<%= request.getContextPath() %>/jsp/cajero/dashboard.jsp" class="btn btn-outline">Cancelar</a>
                        <button type="submit" class="btn btn-success" data-confirm="¿Confirma procesar este depósito?">
                            Procesar Depósito
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <jsp:include page="/jsp/common/footer.jsp" />
</body>
</html>
