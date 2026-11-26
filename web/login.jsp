<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    String rol = (String) session.getAttribute("rol");
    if (rol != null) {
        if ("ADMIN".equalsIgnoreCase(rol)) {
            response.sendRedirect(request.getContextPath() + "/jsp/admin/dashboard.jsp");
            return;
        } else if ("CAJERO".equalsIgnoreCase(rol)) {
            response.sendRedirect(request.getContextPath() + "/jsp/cajero/dashboard.jsp");
            return;
        } else if ("CLIENTE".equalsIgnoreCase(rol)) {
            response.sendRedirect(request.getContextPath() + "/jsp/cliente/dashboard.jsp");
            return;
        }
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Acceso al Sistema Bancario - Banco Académico UDB</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/styles.css">
</head>
<body style="background: linear-gradient(135deg, #0f172a 0%, #1e3a8a 100%);">

    <div class="login-wrapper">
        <div class="login-card">
            <div class="login-header">
                <h2>Banco Académico UDB</h2>
                <p>Sistema Bancario Web - Fase 3 (JSP & Servlets)</p>
            </div>
            <div class="login-body">
                <jsp:include page="/jsp/common/alerts.jsp" />

                <form action="<%= request.getContextPath() %>/LoginServlet" method="POST">
                    <div class="form-group" style="margin-bottom: 1.25rem;">
                        <label class="form-label" for="username">Usuario / Código / DUI</label>
                        <input type="text" id="username" name="username" class="form-control" placeholder="Ej. CL007, admin o cajero1" required autofocus>
                    </div>

                    <div class="form-group" style="margin-bottom: 1.25rem;">
                        <label class="form-label" for="password">Contraseña</label>
                        <input type="password" id="password" name="password" class="form-control" placeholder="••••••••" required>
                    </div>

                    <div class="form-group" style="margin-bottom: 1.75rem;">
                        <label class="form-label" for="rol">Rol de Acceso</label>
                        <select id="rol" name="rol" class="form-control" required>
                            <option value="ADMIN">Administrador</option>
                            <option value="CAJERO">Cajero Bancario</option>
                            <option value="CLIENTE">Cliente</option>
                        </select>
                    </div>

                    <button type="submit" class="btn btn-primary" style="width: 100%; padding: 0.8rem;">
                        Iniciar Sesión
                    </button>
                </form>

                <div style="margin-top: 1.5rem; text-align: center; font-size: 0.85rem; color: #64748b;">
                    <p>Roles disponibles: <strong>Administrador</strong>, <strong>Cajero</strong> y <strong>Cliente</strong>.</p>
                </div>
            </div>
        </div>
    </div>

    <script src="<%= request.getContextPath() %>/js/main.js"></script>
</body>
</html>
