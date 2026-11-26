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
    <title>Módulo de Operaciones de Caja - Banco Académico UDB</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/styles.css">
</head>
<body>
    <jsp:include page="/jsp/common/navbar.jsp" />

    <div class="container">
        <div class="page-header">
            <div>
                <h1>Módulo de Operaciones de Caja</h1>
                <p>Gestión directa de depósitos, retiros y transferencias en ventanilla.</p>
            </div>
            <div>
                <span class="badge badge-cajero" style="padding: 0.5rem 1rem; font-size: 0.9rem;">Cajero Activo</span>
            </div>
        </div>

        <jsp:include page="/jsp/common/alerts.jsp" />

        <div class="stats-grid">
            <div class="stat-card deposit">
                <span class="stat-title">Operación Ventanilla</span>
                <span class="stat-value" style="font-size: 1.5rem; color: var(--success-color);">Depósito</span>
                <p style="color: var(--text-muted); font-size: 0.9rem;">Abono en efectivo directo a cuentas de ahorro o corriente activas.</p>
                <div style="margin-top: 1rem;">
                    <a href="<%= request.getContextPath() %>/jsp/cajero/deposito.jsp" class="btn btn-sm btn-success">Realizar Depósito &rarr;</a>
                </div>
            </div>

            <div class="stat-card withdraw">
                <span class="stat-title">Operación Ventanilla</span>
                <span class="stat-value" style="font-size: 1.5rem; color: var(--danger-color);">Retiro de Fondos</span>
                <p style="color: var(--text-muted); font-size: 0.9rem;">Dispensación de efectivo con validación de saldo disponible y cuenta activa.</p>
                <div style="margin-top: 1rem;">
                    <a href="<%= request.getContextPath() %>/jsp/cajero/retiro.jsp" class="btn btn-sm btn-danger">Realizar Retiro &rarr;</a>
                </div>
            </div>

            <div class="stat-card transfer">
                <span class="stat-title">Operación Entre Cuentas</span>
                <span class="stat-value" style="font-size: 1.5rem; color: var(--info-color);">Transferencia</span>
                <p style="color: var(--text-muted); font-size: 0.9rem;">Envío transaccional atómico e instantáneo entre dos cuentas activas.</p>
                <div style="margin-top: 1rem;">
                    <a href="<%= request.getContextPath() %>/jsp/cajero/transferencia.jsp" class="btn btn-sm btn-primary">Realizar Transferencia &rarr;</a>
                </div>
            </div>
        </div>
    </div>

    <jsp:include page="/jsp/common/footer.jsp" />
</body>
</html>
