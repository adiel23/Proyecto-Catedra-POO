<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Página no encontrada (404) - Banco Académico UDB</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/styles.css">
</head>
<body>
    <jsp:include page="/jsp/common/navbar.jsp" />

    <div class="container" style="text-align: center; margin-top: 4rem;">
        <h1 style="font-size: 5rem; color: var(--primary-color);">404</h1>
        <h2>Página no encontrada</h2>
        <p style="color: var(--text-muted); margin: 1.5rem 0;">El recurso o página web bancaria solicitada no existe o fue movida.</p>
        <a href="<%= request.getContextPath() %>/" class="btn btn-primary">Volver al Inicio</a>
    </div>

    <jsp:include page="/jsp/common/footer.jsp" />
</body>
</html>
