<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Error en el Servidor (500) - Banco Académico UDB</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/styles.css">
</head>
<body>
    <jsp:include page="/jsp/common/navbar.jsp" />

    <div class="container" style="text-align: center; margin-top: 4rem;">
        <h1 style="font-size: 5rem; color: var(--danger-color);">500</h1>
        <h2>Error Interno del Servidor</h2>
        <p style="color: var(--text-muted); margin: 1.5rem 0;">Ocurrió una excepción al procesar la operación bancaria.</p>
        
        <% if (exception != null) { %>
            <div class="card" style="max-width: 650px; margin: 1rem auto; text-align: left;">
                <div class="card-header" style="color: var(--danger-color);">Detalle del Error</div>
                <div class="card-body">
                    <code><%= exception.getMessage() != null ? exception.getMessage() : exception.toString() %></code>
                </div>
            </div>
        <% } %>

        <a href="<%= request.getContextPath() %>/" class="btn btn-primary" style="margin-top: 1rem;">Volver al Inicio</a>
    </div>

    <jsp:include page="/jsp/common/footer.jsp" />
</body>
</html>
