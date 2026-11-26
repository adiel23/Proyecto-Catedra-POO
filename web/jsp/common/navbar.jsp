<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    String userRol = (String) session.getAttribute("rol");
    String userName = (String) session.getAttribute("usuario");
    if (userRol == null) {
        userRol = "INVITADO";
    }
    if (userName == null) {
        userName = "Usuario";
    }
    String contextPath = request.getContextPath();
%>
<nav class="bank-navbar">
    <a href="<%= contextPath %>/" class="bank-logo">
        <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M3 21h18"/><path d="M3 10h18"/><path d="M5 6l7-3 7 3"/><path d="M4 10v11"/><path d="M20 10v11"/><path d="M8 14v4"/><path d="M12 14v4"/><path d="M16 14v4"/></svg>
        Banco Académico UDB
        <span class="badge-phase">Fase 3 Web</span>
    </a>

    <ul class="nav-links">
        <% if ("ADMIN".equalsIgnoreCase(userRol)) { %>
            <li><a href="<%= contextPath %>/jsp/admin/dashboard.jsp">Dashboard</a></li>
            <li><a href="<%= contextPath %>/jsp/admin/clientes-lista.jsp">Clientes</a></li>
            <li><a href="<%= contextPath %>/jsp/admin/cuentas-lista.jsp">Cuentas</a></li>
            <li><a href="<%= contextPath %>/jsp/admin/reportes.jsp">Reportes</a></li>
        <% } else if ("CAJERO".equalsIgnoreCase(userRol)) { %>
            <li><a href="<%= contextPath %>/jsp/cajero/dashboard.jsp">Operaciones</a></li>
            <li><a href="<%= contextPath %>/jsp/cajero/deposito.jsp">Depósito</a></li>
            <li><a href="<%= contextPath %>/jsp/cajero/retiro.jsp">Retiro</a></li>
            <li><a href="<%= contextPath %>/jsp/cajero/transferencia.jsp">Transferencia</a></li>
        <% } else if ("CLIENTE".equalsIgnoreCase(userRol)) { %>
            <li><a href="<%= contextPath %>/jsp/cliente/dashboard.jsp">Mis Cuentas</a></li>
            <li><a href="<%= contextPath %>/jsp/cliente/movimientos.jsp">Movimientos</a></li>
            <li><a href="<%= contextPath %>/jsp/cliente/transferencia.jsp">Transferir</a></li>
        <% } %>
    </ul>

    <div class="user-session-info">
        <% if (!"INVITADO".equalsIgnoreCase(userRol)) { %>
            <div class="user-badge">
                <strong><%= userName %></strong>
                <span class="role-tag role-<%= userRol.toLowerCase() %>"><%= userRol %></span>
            </div>
            <a href="<%= contextPath %>/LogoutServlet" class="btn-logout">Cerrar Sesión</a>
        <% } else { %>
            <a href="<%= contextPath %>/login.jsp" class="btn btn-sm btn-primary">Iniciar Sesión</a>
        <% } %>
    </div>
</nav>
