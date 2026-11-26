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
    <title>Transferencia entre Cuentas - Banco Académico UDB</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/styles.css">
</head>
<body>
    <jsp:include page="/jsp/common/navbar.jsp" />

    <div class="container">
        <div class="page-header">
            <div>
                <h1>Transferencia entre Cuentas</h1>
                <p>Transferencia atómica entre cuentas bancarias activas del banco.</p>
            </div>
            <div>
                <a href="<%= request.getContextPath() %>/jsp/cajero/dashboard.jsp" class="btn btn-outline">&larr; Volver al Menú de Caja</a>
            </div>
        </div>

        <jsp:include page="/jsp/common/alerts.jsp" />

        <div class="card" style="max-width: 650px; margin: 0 auto;">
            <div class="card-header" style="background-color: #dbeafe; color: #1e40af;">
                <span>Formulario de Transferencia</span>
            </div>
            <div class="card-body">
                <form action="<%= request.getContextPath() %>/TransaccionServlet" method="POST">
                    <input type="hidden" name="tipoTransaccion" value="Transferencia">

                    <div class="form-grid">
                        <div class="form-group">
                            <label class="form-label" for="numeroCuentaOrigen">Cuenta Origen *</label>
                            <input type="text" id="numeroCuentaOrigen" name="numeroCuenta" class="form-control" 
                                   placeholder="Cuenta que transfiere" required autofocus>
                        </div>

                        <div class="form-group">
                            <label class="form-label" for="numeroCuentaDestino">Cuenta Destino *</label>
                            <input type="text" id="numeroCuentaDestino" name="numeroCuentaDestino" class="form-control" 
                                   placeholder="Cuenta que recibe" required>
                        </div>

                        <div class="form-group col-full">
                            <label class="form-label" for="monto">Monto a Transferir ($) *</label>
                            <input type="number" id="monto" name="monto" class="form-control" 
                                   step="0.01" min="0.01" placeholder="0.00" required>
                        </div>

                        <div class="form-group col-full">
                            <label class="form-label" for="descripcion">Concepto / Motivo</label>
                            <input type="text" id="descripcion" name="descripcion" class="form-control" 
                                   placeholder="Ej. Pago de cuota, transferencia de fondos" value="Transferencia de fondos">
                        </div>
                    </div>

                    <div class="form-actions">
                        <a href="<%= request.getContextPath() %>/jsp/cajero/dashboard.jsp" class="btn btn-outline">Cancelar</a>
                        <button type="submit" class="btn btn-primary" data-confirm="¿Confirma debitar de la cuenta origen y abonar a la cuenta destino?">
                            Ejecutar Transferencia
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <jsp:include page="/jsp/common/footer.jsp" />
</body>
</html>
