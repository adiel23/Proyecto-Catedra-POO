<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    String rol = (String) session.getAttribute("rol");
    if (rol == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    } else if ("ADMIN".equalsIgnoreCase(rol)) {
        response.sendRedirect(request.getContextPath() + "/jsp/admin/dashboard.jsp");
        return;
    } else if ("CAJERO".equalsIgnoreCase(rol)) {
        response.sendRedirect(request.getContextPath() + "/jsp/cajero/dashboard.jsp");
        return;
    } else if ("CLIENTE".equalsIgnoreCase(rol)) {
        response.sendRedirect(request.getContextPath() + "/jsp/cliente/dashboard.jsp");
        return;
    }
%>
