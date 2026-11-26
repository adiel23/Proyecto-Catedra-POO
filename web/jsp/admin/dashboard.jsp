<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    String rol = (String) session.getAttribute("rol");
    if (rol == null || !"ADMIN".equalsIgnoreCase(rol)) {
        session.setAttribute("flash_error", "Acceso denegado: Se requieren permisos de Administrador.");
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Panel de Administración - Banco Académico UDB</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/styles.css">
</head>
<body>
    <jsp:include page="/jsp/common/navbar.jsp" />

    <div class="container">
        <div class="page-header">
            <div>
                <h1>Panel de Control del Administrador</h1>
                <p>Gestión global de clientes, cuentas bancarias y reportes consolidados.</p>
            </div>
            <div>
                <span class="badge badge-active" style="padding: 0.5rem 1rem; font-size: 0.9rem;">Sesión Activa: Administrador</span>
            </div>
        </div>

        <jsp:include page="/jsp/common/alerts.jsp" />

        <div class="stats-grid">
            <div class="stat-card">
                <span class="stat-title">Módulo de Clientes</span>
                <span class="stat-value" style="font-size: 1.35rem; color: var(--primary-color);">Gestión Clientes</span>
                <p style="color: var(--text-muted); font-size: 0.9rem;">Altas, bajas, modificaciones y consulta del listado de clientes.</p>
                <div style="margin-top: 1rem;">
                    <a href="<%= request.getContextPath() %>/jsp/admin/clientes-lista.jsp" class="btn btn-sm btn-primary">Ir a Clientes &rarr;</a>
                </div>
            </div>

            <div class="stat-card">
                <span class="stat-title">Módulo de Cuentas</span>
                <span class="stat-value" style="font-size: 1.35rem; color: var(--secondary-color);">Cuentas Bancarias</span>
                <p style="color: var(--text-muted); font-size: 0.9rem;">Apertura de cuentas (Ahorro/Corriente), bloqueo y consulta de saldos.</p>
                <div style="margin-top: 1rem;">
                    <a href="<%= request.getContextPath() %>/jsp/admin/cuentas-lista.jsp" class="btn btn-sm btn-primary">Ir a Cuentas &rarr;</a>
                </div>
            </div>

            <div class="stat-card">
                <span class="stat-title">Módulo de Reportes</span>
                <span class="stat-value" style="font-size: 1.35rem; color: var(--accent-color);">Consultas & Métricas</span>
                <p style="color: var(--text-muted); font-size: 0.9rem;">Los 8 reportes tabulares y totales acumulados de la Fase 2.</p>
                <div style="margin-top: 1rem;">
                    <a href="<%= request.getContextPath() %>/jsp/admin/reportes.jsp" class="btn btn-sm btn-primary">Ver Reportes &rarr;</a>
                </div>
            </div>
        </div>

        <div class="card">
            <div class="card-header">
                <span>Acciones Rápidas del Administrador</span>
            </div>
            <div class="card-body">
                <div style="display: flex; gap: 1rem; flex-wrap: wrap;">
                    <a href="<%= request.getContextPath() %>/jsp/admin/cliente-form.jsp" class="btn btn-primary">
                        + Registrar Nuevo Cliente
                    </a>
                    <a href="<%= request.getContextPath() %>/jsp/admin/cuenta-form.jsp" class="btn btn-secondary">
                        + Aperturar Cuenta Bancaria
                    </a>
                    <a href="<%= request.getContextPath() %>/jsp/admin/reportes.jsp" class="btn btn-outline">
                        Exportar / Consultar Reportes
                    </a>
                </div>
            </div>
        </div>
    </div>

    <jsp:include page="/jsp/common/footer.jsp" />
</body>
</html>
