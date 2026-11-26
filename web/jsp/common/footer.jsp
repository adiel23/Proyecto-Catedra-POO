<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<footer class="bank-footer">
    <p>&copy; <%= java.time.Year.now().getValue() %> Banco Académico UDB - Programación Orientada a Objetos (Fase 3: Servlets, JSP & JavaBeans).</p>
    <p style="font-size: 0.75rem; margin-top: 0.25rem; color: #64748b;">
        Compatible con Apache Tomcat 10.1.x &bull; MySQL &bull; JDBC &bull; Arquitectura MVC
    </p>
</footer>
<script src="<%= request.getContextPath() %>/js/main.js"></script>
