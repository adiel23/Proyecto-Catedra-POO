<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="modelo.ReporteMovimiento" %>
<%@ page import="modelo.Cliente" %>
<%@ page import="modelo.CuentaBancaria" %>
<%
    String rol = (String) session.getAttribute("rol");
    if (rol == null || !"ADMIN".equalsIgnoreCase(rol)) {
        session.setAttribute("flash_error", "Acceso denegado: Se requieren permisos de Administrador.");
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    // Totales calculados (SUM(monto)) de la Fase 2
    Double totalDepositado = (Double) request.getAttribute("totalDepositado");
    Double totalRetirado = (Double) request.getAttribute("totalRetirado");
    Double totalTransferido = (Double) request.getAttribute("totalTransferido");

    if (totalDepositado == null) totalDepositado = 0.00;
    if (totalRetirado == null) totalRetirado = 0.00;
    if (totalTransferido == null) totalTransferido = 0.00;

    String tipoReporte = (String) request.getAttribute("tipoReporte");
    if (tipoReporte == null) tipoReporte = "1";

    String numCuentaFiltro = (String) request.getAttribute("numCuentaFiltro");
    if (numCuentaFiltro == null) numCuentaFiltro = "";

    // Listas resultantes
    List<?> resultados = (List<?>) request.getAttribute("resultadosReporte");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Módulo de Consultas y Reportes - Banco Académico UDB</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/styles.css">
</head>
<body>
    <jsp:include page="/jsp/common/navbar.jsp" />

    <div class="container">
        <div class="page-header">
            <div>
                <h1>Módulo de Consultas y Reportes Bancarios</h1>
                <p>Consolidado financiero multitabla y reportes tabulares de la Fase 2.</p>
            </div>
            <div>
                <a href="<%= request.getContextPath() %>/ReporteServlet?accion=totales" class="btn btn-outline">
                    &#x21bb; Actualizar Totales
                </a>
            </div>
        </div>

        <jsp:include page="/jsp/common/alerts.jsp" />

        <!-- Tarjetas Superiores de Totales Acumulados -->
        <div class="stats-grid">
            <div class="stat-card deposit">
                <span class="stat-title">Total Depositado</span>
                <span class="stat-value">$ <%= String.format("%.2f", totalDepositado) %></span>
                <small style="color: var(--text-muted);">Suma acumulada de depósitos</small>
            </div>

            <div class="stat-card withdraw">
                <span class="stat-title">Total Retirado</span>
                <span class="stat-value">$ <%= String.format("%.2f", totalRetirado) %></span>
                <small style="color: var(--text-muted);">Suma acumulada de retiros</small>
            </div>

            <div class="stat-card transfer">
                <span class="stat-title">Total Transferido</span>
                <span class="stat-value">$ <%= String.format("%.2f", totalTransferido) %></span>
                <small style="color: var(--text-muted);">Suma acumulada de transferencias</small>
            </div>
        </div>

        <!-- Filtro y Selector de Reportes -->
        <div class="card">
            <div class="card-header">
                <span>Generador de Reportes</span>
            </div>
            <div class="card-body">
                <form action="<%= request.getContextPath() %>/ReporteServlet" method="GET">
                    <input type="hidden" name="accion" value="generar">
                    <div class="form-grid" style="align-items: flex-end;">
                        <div class="form-group" style="flex: 2;">
                            <label class="form-label" for="tipoReporte">Seleccione el Reporte:</label>
                            <select id="tipoReporte" name="tipoReporte" class="form-control" onchange="toggleCuentaInput(this.value)">
                                <option value="1" <%= "1".equals(tipoReporte) ? "selected" : "" %>>1. Cuentas Activas</option>
                                <option value="2" <%= "2".equals(tipoReporte) ? "selected" : "" %>>2. Cuentas Bloqueadas</option>
                                <option value="3" <%= "3".equals(tipoReporte) ? "selected" : "" %>>3. Clientes Registrados</option>
                                <option value="4" <%= "4".equals(tipoReporte) ? "selected" : "" %>>4. Historial General de Transacciones</option>
                                <option value="5" <%= "5".equals(tipoReporte) ? "selected" : "" %>>5. Transacciones por Cuenta</option>
                            </select>
                        </div>

                        <div class="form-group" id="grupoCuentaFiltro" style="<%= "5".equals(tipoReporte) ? "" : "display: none;" %>">
                            <label class="form-label" for="numeroCuenta">N° de Cuenta para Filtrar:</label>
                            <input type="text" id="numeroCuenta" name="numeroCuenta" class="form-control" 
                                   value="<%= numCuentaFiltro %>" placeholder="Ej. 7249971">
                        </div>

                        <div class="form-group">
                            <button type="submit" class="btn btn-primary" style="height: 42px;">
                                Generar Reporte
                            </button>
                        </div>
                    </div>
                </form>
            </div>
        </div>

        <!-- Tabla de Resultados -->
        <div class="card">
            <div class="card-header">
                <span>Resultados del Reporte</span>
                <span style="font-size: 0.85rem; font-weight: normal; color: var(--text-muted);">
                    Registros encontrados: <%= (resultados != null) ? resultados.size() : 0 %>
                </span>
            </div>
            <div class="card-body">
                <div class="table-responsive">
                    <% if ("1".equals(tipoReporte) || "2".equals(tipoReporte)) { %>
                        <!-- Reporte de Cuentas (Activas o Bloqueadas) -->
                        <table class="bank-table">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>N° Cuenta</th>
                                    <th>Cód. Cliente</th>
                                    <th>Titular</th>
                                    <th>Tipo Cuenta</th>
                                    <th>Saldo ($)</th>
                                    <th>Estado</th>
                                </tr>
                            </thead>
                            <tbody>
                                <% if (resultados != null && !resultados.isEmpty()) {
                                    for (Object obj : resultados) {
                                        CuentaBancaria cb = (CuentaBancaria) obj; %>
                                        <tr>
                                            <td><%= cb.getIdCuenta() %></td>
                                            <td><strong><%= cb.getNumeroCuenta() %></strong></td>
                                            <td><%= cb.getIdCliente() %></td>
                                            <td>Titular Asociado</td>
                                            <td><%= cb.getTipoCuenta() %></td>
                                            <td><strong style="color: var(--success-color);">$ <%= String.format("%.2f", cb.getSaldo()) %></strong></td>
                                            <td>
                                                <span class="badge <%= "Activa".equalsIgnoreCase(cb.getEstadoCuenta()) ? "badge-active" : "badge-blocked" %>">
                                                    <%= cb.getEstadoCuenta() %>
                                                </span>
                                            </td>
                                        </tr>
                                <%  }
                                   } else { %>
                                    <tr><td colspan="7" style="text-align: center; color: var(--text-muted); padding: 1.5rem;">No se encontraron registros para este reporte.</td></tr>
                                <% } %>
                            </tbody>
                        </table>

                    <% } else if ("3".equals(tipoReporte)) { %>
                        <!-- Reporte de Clientes Registrados -->
                        <table class="bank-table">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Código</th>
                                    <th>DUI</th>
                                    <th>Nombre Completo</th>
                                    <th>Teléfono</th>
                                    <th>Correo Electrónico</th>
                                    <th>Dirección</th>
                                    <th>Estado</th>
                                </tr>
                            </thead>
                            <tbody>
                                <% if (resultados != null && !resultados.isEmpty()) {
                                    for (Object obj : resultados) {
                                        Cliente cl = (Cliente) obj; %>
                                        <tr>
                                            <td><%= cl.getIdCliente() %></td>
                                            <td><strong><%= cl.getCodigoCliente() %></strong></td>
                                            <td><%= cl.getDui() %></td>
                                            <td><%= cl.getNombreCompleto() %></td>
                                            <td><%= cl.getTelefono() %></td>
                                            <td><%= cl.getCorreoElectronico() %></td>
                                            <td><%= cl.getDireccion() %></td>
                                            <td><span class="badge badge-active"><%= cl.getEstado() %></span></td>
                                        </tr>
                                <%  }
                                   } else { %>
                                    <tr><td colspan="8" style="text-align: center; color: var(--text-muted); padding: 1.5rem;">No se encontraron clientes registrados.</td></tr>
                                <% } %>
                            </tbody>
                        </table>

                    <% } else { %>
                        <!-- Reporte 4 y 5: Historial y Movimientos de Cuenta -->
                        <table class="bank-table">
                            <thead>
                                <tr>
                                    <th>ID Mov.</th>
                                    <th>Tipo</th>
                                    <th>Cuenta Origen</th>
                                    <th>Cuenta Destino</th>
                                    <th>Monto ($)</th>
                                    <th>Descripción</th>
                                    <th>Fecha</th>
                                </tr>
                            </thead>
                            <tbody>
                                <% if (resultados != null && !resultados.isEmpty()) {
                                    for (Object obj : resultados) {
                                        ReporteMovimiento rm = (ReporteMovimiento) obj; %>
                                        <tr>
                                            <td><%= rm.getIdMovimiento() %></td>
                                            <td>
                                                <span class="badge badge-<%= rm.getTipoMovimiento().toLowerCase() %>">
                                                    <%= rm.getTipoMovimiento() %>
                                                </span>
                                            </td>
                                            <td><strong><%= rm.getNumeroCuentaOrigen() %></strong></td>
                                            <td><%= rm.getNumeroCuentaDestino() != null ? rm.getNumeroCuentaDestino() : "N/A" %></td>
                                            <td><strong>$ <%= String.format("%.2f", rm.getMonto()) %></strong></td>
                                            <td><%= rm.getDescripcion() != null ? rm.getDescripcion() : "-" %></td>
                                            <td><%= rm.getFechaMovimiento() %></td>
                                        </tr>
                                <%  }
                                   } else { %>
                                    <tr><td colspan="7" style="text-align: center; color: var(--text-muted); padding: 1.5rem;">No se encontraron movimientos registrados.</td></tr>
                                <% } %>
                            </tbody>
                        </table>
                    <% } %>
                </div>
            </div>
        </div>
    </div>

    <script>
        function toggleCuentaInput(val) {
            const grupo = document.getElementById('grupoCuentaFiltro');
            if (val === '5') {
                grupo.style.display = 'flex';
                document.getElementById('numeroCuenta').focus();
            } else {
                grupo.style.display = 'none';
            }
        }
    </script>

    <jsp:include page="/jsp/common/footer.jsp" />
</body>
</html>
