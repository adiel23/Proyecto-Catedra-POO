<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="modelo.Cliente" %>
<%
    String rol = (String) session.getAttribute("rol");
    if (rol == null || !"ADMIN".equalsIgnoreCase(rol)) {
        session.setAttribute("flash_error", "Acceso denegado: Se requieren permisos de Administrador.");
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    Cliente cliente = (Cliente) request.getAttribute("cliente");
    boolean esEdicion = (cliente != null && cliente.getIdCliente() > 0);
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><%= esEdicion ? "Editar Cliente" : "Registrar Cliente" %> - Banco Académico UDB</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/styles.css">
</head>
<body>
    <jsp:include page="/jsp/common/navbar.jsp" />

    <div class="container">
        <div class="page-header">
            <div>
                <h1><%= esEdicion ? "Editar Información del Cliente" : "Registrar Nuevo Cliente" %></h1>
                <p>Complete los datos requeridos bajo las reglas de negocio del banco.</p>
            </div>
            <div>
                <a href="<%= request.getContextPath() %>/jsp/admin/clientes-lista.jsp" class="btn btn-outline">&larr; Volver al Listado</a>
            </div>
        </div>

        <jsp:include page="/jsp/common/alerts.jsp" />

        <div class="card" style="max-width: 800px; margin: 0 auto;">
            <div class="card-header">
                <span>Formulario de Cliente</span>
            </div>
            <div class="card-body">
                <form action="<%= request.getContextPath() %>/ClienteServlet" method="POST">
                    <input type="hidden" name="accion" value="<%= esEdicion ? "actualizar" : "insertar" %>">
                    <% if (esEdicion) { %>
                        <input type="hidden" name="idCliente" value="<%= cliente.getIdCliente() %>">
                    <% } %>

                    <div class="form-grid">
                        <div class="form-group">
                            <label class="form-label" for="codigoCliente">Código de Cliente *</label>
                            <input type="text" id="codigoCliente" name="codigoCliente" class="form-control" 
                                   value="<%= esEdicion ? cliente.getCodigoCliente() : "" %>" 
                                   placeholder="Ej. CL007" required <%= esEdicion ? "readonly" : "" %>>
                            <small style="color: var(--text-muted);">Formato: CL seguido de números</small>
                        </div>

                        <div class="form-group">
                            <label class="form-label" for="dui">DUI (El Salvador) *</label>
                            <input type="text" id="dui" name="dui" class="form-control" data-format="dui"
                                   value="<%= esEdicion ? cliente.getDui() : "" %>" 
                                   placeholder="00000000-0" maxlength="10" required>
                        </div>

                        <div class="form-group col-full">
                            <label class="form-label" for="nombreCompleto">Nombre Completo *</label>
                            <input type="text" id="nombreCompleto" name="nombreCompleto" class="form-control" 
                                   value="<%= esEdicion ? cliente.getNombreCompleto() : "" %>" 
                                   placeholder="Nombre y Apellidos completos" required>
                        </div>

                        <div class="form-group">
                            <label class="form-label" for="telefono">Teléfono</label>
                            <input type="text" id="telefono" name="telefono" class="form-control" data-format="telefono"
                                   value="<%= esEdicion && cliente.getTelefono() != null ? cliente.getTelefono() : "" %>" 
                                   placeholder="0000-0000" maxlength="9">
                        </div>

                        <div class="form-group">
                            <label class="form-label" for="correoElectronico">Correo Electrónico</label>
                            <input type="email" id="correoElectronico" name="correoElectronico" class="form-control" 
                                   value="<%= esEdicion && cliente.getCorreoElectronico() != null ? cliente.getCorreoElectronico() : "" %>" 
                                   placeholder="cliente@correo.com">
                        </div>

                        <div class="form-group col-full">
                            <label class="form-label" for="direccion">Dirección</label>
                            <input type="text" id="direccion" name="direccion" class="form-control" 
                                   value="<%= esEdicion && cliente.getDireccion() != null ? cliente.getDireccion() : "" %>" 
                                   placeholder="Dirección domiciliar completa">
                        </div>

                        <div class="form-group">
                            <label class="form-label" for="estado">Estado *</label>
                            <select id="estado" name="estado" class="form-control" required>
                                <option value="Activo" <%= esEdicion && "Activo".equalsIgnoreCase(cliente.getEstado()) ? "selected" : "" %>>Activo</option>
                                <option value="Inactivo" <%= esEdicion && "Inactivo".equalsIgnoreCase(cliente.getEstado()) ? "selected" : "" %>>Inactivo</option>
                                <option value="Bloqueado" <%= esEdicion && "Bloqueado".equalsIgnoreCase(cliente.getEstado()) ? "selected" : "" %>>Bloqueado</option>
                            </select>
                        </div>
                    </div>

                    <div class="form-actions">
                        <a href="<%= request.getContextPath() %>/jsp/admin/clientes-lista.jsp" class="btn btn-outline">Cancelar</a>
                        <button type="submit" class="btn btn-primary">
                            <%= esEdicion ? "Guardar Cambios" : "Registrar Cliente" %>
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <jsp:include page="/jsp/common/footer.jsp" />
</body>
</html>
