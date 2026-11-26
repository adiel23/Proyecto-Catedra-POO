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
    <title>Retiro de Efectivo - Banco Académico UDB</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/styles.css">
</head>
<body>
    <jsp:include page="/jsp/common/navbar.jsp" />

    <div class="container">
        <div class="page-header">
            <div>
                <h1>Retiro Bancario de Fondos</h1>
                <p>Retiro por ventanilla con validación de saldo disponible y cuenta activa.</p>
            </div>
            <div>
                <a href="<%= request.getContextPath() %>/jsp/cajero/dashboard.jsp" class="btn btn-outline">&larr; Volver al Menú de Caja</a>
            </div>
        </div>

        <jsp:include page="/jsp/common/alerts.jsp" />

        <div class="card" style="max-width: 600px; margin: 0 auto;">
            <div class="card-header" style="background-color: #fee2e2; color: #991b1b;">
                <span>Formulario de Retiro</span>
            </div>
            <div class="card-body">
                <form action="<%= request.getContextPath() %>/TransaccionServlet" method="POST">
                    <input type="hidden" name="tipoTransaccion" value="Retiro">

                    <div class="form-group" style="margin-bottom: 1.25rem;">
                        <label class="form-label" for="numeroCuenta">Número de Cuenta Origen *</label>
                        <input type="text" id="numeroCuenta" name="numeroCuenta" class="form-control" 
                               placeholder="Ej. 7249971" required autofocus>
                        <small style="color: var(--text-muted);">La cuenta debe tener saldo suficiente y estar activa.</small>
                    </div>

                    <div class="form-group" style="margin-bottom: 1.25rem;">
                        <label class="form-label" for="monto">Monto a Retirar ($) *</label>
                        <input type="number" id="monto" name="monto" class="form-control" 
                               step="0.01" min="0.01" placeholder="0.00" required>
                        <small style="color: var(--text-muted);">No puede exceder el saldo disponible.</small>
                    </div>

                    <div class="form-group" style="margin-bottom: 1.75rem;">
                        <label class="form-label" for="descripcion">Concepto / Descripción</label>
                        <input type="text" id="descripcion" name="descripcion" class="form-control" 
                               placeholder="Ej. Retiro en efectivo por ventanilla" value="Retiro en ventanilla">
                    </div>

                    <div class="form-actions">
                        <a href="<%= request.getContextPath() %>/jsp/cajero/dashboard.jsp" class="btn btn-outline">Cancelar</a>
                        <button type="submit" class="btn btn-danger" data-confirm="¿Confirma dispensar el monto ingresado?">
                            Procesar Retiro
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <jsp:include page="/jsp/common/footer.jsp" />
</body>
</html>
