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
    <title>Gestión de Clientes - Banco Académico UDB</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/styles.css">
</head>
<body>
    <jsp:include page="/jsp/common/navbar.jsp" />

    <div class="container">
        <div class="page-header">
            <div>
                <h1>Gestión de Clientes (CRUD)</h1>
                <p>Listado y administración de clientes registrados en el sistema bancario.</p>
            </div>
            <div>
                <a href="<%= request.getContextPath() %>/jsp/admin/cliente-form.jsp" class="btn btn-primary">
                    + Nuevo Cliente
                </a>
            </div>
        </div>

        <jsp:include page="/jsp/common/alerts.jsp" />

        <div class="card">
            <div class="card-header">
                <span>Clientes Registrados</span>
                <form action="<%= request.getContextPath() %>/ClienteServlet" method="GET" style="display: inline-block;">
                    <input type="hidden" name="accion" value="listar">
                    <button type="submit" class="btn btn-sm btn-outline">Recargar Lista</button>
                </form>
            </div>
            <div class="card-body">
                <div class="table-responsive">
                    <table class="bank-table">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Código</th>
                                <th>DUI</th>
                                <th>Nombre Completo</th>
                                <th>Teléfono</th>
                                <th>Correo Electrónico</th>
                                <th>Estado</th>
                                <th>Acciones</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% if (clientes != null && !clientes.isEmpty()) {
                                for (Cliente c : clientes) { %>
                                    <tr>
                                        <td><%= c.getIdCliente() %></td>
                                        <td><strong><%= c.getCodigoCliente() %></strong></td>
                                        <td><%= c.getDui() %></td>
                                        <td><%= c.getNombreCompleto() %></td>
                                        <td><%= c.getTelefono() != null ? c.getTelefono() : "-" %></td>
                                        <td><%= c.getCorreoElectronico() != null ? c.getCorreoElectronico() : "-" %></td>
                                        <td>
                                            <% if ("Activo".equalsIgnoreCase(c.getEstado())) { %>
                                                <span class="badge badge-active">Activo</span>
                                            <% } else if ("Inactivo".equalsIgnoreCase(c.getEstado())) { %>
                                                <span class="badge badge-inactive">Inactivo</span>
                                            <% } else { %>
                                                <span class="badge badge-blocked">Bloqueado</span>
                                            <% } %>
                                        </td>
                                        <td>
                                            <a href="<%= request.getContextPath() %>/ClienteServlet?accion=editar&id=<%= c.getIdCliente() %>" class="btn btn-sm btn-outline">Editar</a>
                                            <a href="<%= request.getContextPath() %>/ClienteServlet?accion=eliminar&id=<%= c.getIdCliente() %>" class="btn btn-sm btn-danger" data-confirm="¿Desea cambiar el estado de este cliente?">Baja</a>
                                        </td>
                                    </tr>
                            <%  }
                               } else { %>
                                    <tr>
                                        <td colspan="8" style="text-align: center; color: var(--text-muted); padding: 2rem;">
                                            No hay datos cargados o aún no se ha ejecutado la consulta.<br>
                                            <a href="<%= request.getContextPath() %>/ClienteServlet?accion=listar" class="btn btn-sm btn-primary" style="margin-top: 0.5rem;">Cargar Clientes desde la Base de Datos</a>
                                        </td>
                                    </tr>
                            <% } %>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>

    <jsp:include page="/jsp/common/footer.jsp" />
</body>
</html>
