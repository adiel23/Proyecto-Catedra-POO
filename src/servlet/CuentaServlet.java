package servlet;

import dao.ClienteDAO;
import dao.CuentaBancariaDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modelo.Cliente;
import modelo.CuentaBancaria;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@WebServlet(name = "CuentaServlet", urlPatterns = {"/CuentaServlet"})
public class CuentaServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(CuentaServlet.class);
    private static final List<String> TIPOS = Arrays.asList("Ahorro", "Corriente");
    private static final List<String> ESTADOS = Arrays.asList("Activa", "Inactiva", "Bloqueada");

    private static final String VISTA_LISTA = "/jsp/admin/cuentas-lista.jsp";
    private static final String VISTA_FORM = "/jsp/admin/cuenta-form.jsp";
    private static final String VISTA_CLIENTE = "/jsp/cliente/dashboard.jsp";

    private final CuentaBancariaDAO cuentaDAO = new CuentaBancariaDAO();
    private final ClienteDAO clienteDAO = new ClienteDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accion = request.getParameter("accion");
        if (accion == null || accion.trim().isEmpty()) {
            accion = "listar";
        }

        // El cliente solo puede consultar sus propias cuentas
        if ("misCuentas".equals(accion)) {
            if (!SeguridadUtil.exigirRol(request, response, "CLIENTE")) {
                return;
            }
            misCuentas(request, response);
            return;
        }

        if (!SeguridadUtil.exigirRol(request, response, "ADMIN")) {
            return;
        }

        switch (accion) {
            case "nuevo":
                nueva(request, response);
                break;
            case "cambiarEstado":
                cambiarEstado(request, response);
                break;
            default:
                listar(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!SeguridadUtil.exigirRol(request, response, "ADMIN")) {
            return;
        }
        request.setCharacterEncoding("UTF-8");
        String accion = request.getParameter("accion");
        if ("insertar".equals(accion)) {
            insertar(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/CuentaServlet?accion=listar");
        }
    }

    private void listar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setAttribute("listaCuentas", cuentaDAO.listarTodas());
        } catch (SQLException e) {
            logger.error("No se pudo listar cuentas: {}", e.getMessage());
            SeguridadUtil.guardarMensaje(request, "flash_error",
                    "No se pudo consultar la lista de cuentas. Intenta de nuevo.");
            request.setAttribute("listaCuentas", new ArrayList<CuentaBancaria>());
        }
        request.getRequestDispatcher(VISTA_LISTA).forward(request, response);
    }

    // Cuentas y saldos del cliente conectado (su usuario es el codigo de cliente o el DUI)
    private void misCuentas(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<CuentaBancaria> misCuentas = new ArrayList<>();
        try {
            String usuario = SeguridadUtil.usuarioActual(request);
            usuario = usuario == null ? "" : usuario.trim();

            Cliente cliente = clienteDAO.buscarPorCodigo(usuario.toUpperCase());
            if (cliente == null) {
                cliente = clienteDAO.buscarPorDui(usuario);
            }

            if (cliente == null) {
                SeguridadUtil.guardarMensaje(request, "flash_error",
                        "No se encontro un cliente asociado a tu usuario. Ingresa con tu codigo (ej. CL007) o tu DUI.");
            } else {
                for (CuentaBancaria c : cuentaDAO.listarTodas()) {
                    if (c.getIdCliente() == cliente.getIdCliente()) {
                        misCuentas.add(c);
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("No se pudieron consultar las cuentas del cliente: {}", e.getMessage());
            SeguridadUtil.guardarMensaje(request, "flash_error",
                    "No se pudieron consultar tus cuentas. Intenta de nuevo.");
        }
        request.setAttribute("misCuentas", misCuentas);
        request.getRequestDispatcher(VISTA_CLIENTE).forward(request, response);
    }

    // Abre el formulario de apertura con la lista de clientes activos.
    private void nueva(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Cliente> activos = new ArrayList<>();
        try {
            for (Cliente c : clienteDAO.listarTodos()) {
                if ("Activo".equalsIgnoreCase(c.getEstado())) {
                    activos.add(c);
                }
            }
        } catch (SQLException e) {
            logger.error("No se pudo cargar la lista de clientes: {}", e.getMessage());
            SeguridadUtil.guardarMensaje(request, "flash_error", "No se pudo cargar la lista de clientes.");
        }
        request.setAttribute("listaClientes", activos);
        request.getRequestDispatcher(VISTA_FORM).forward(request, response);
    }

    private void insertar(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String numero = texto(request, "numeroCuenta");
        String tipo = texto(request, "tipoCuenta");
        String saldoTexto = texto(request, "saldoInicial");
        String idTexto = texto(request, "idCliente");
        List<String> errores = new ArrayList<>();
        CuentaBancaria cuenta = new CuentaBancaria();

        try {
            if (!numero.matches("^\\d{6,15}$")) {
                errores.add("El numero de cuenta debe tener entre 6 y 15 digitos.");
            } else if (cuentaDAO.buscarPorNumero(numero) != null) {
                errores.add("Ya existe una cuenta con ese numero.");
            }

            if (!TIPOS.contains(tipo)) {
                errores.add("El tipo de cuenta debe ser Ahorro o Corriente.");
            }

            try {
                BigDecimal saldo = new BigDecimal(saldoTexto.isEmpty() ? "0" : saldoTexto);
                if (saldo.signum() < 0) {
                    errores.add("El saldo inicial no puede ser negativo.");
                } else if (saldo.scale() > 2) {
                    errores.add("El saldo inicial admite maximo 2 decimales.");
                } else {
                    cuenta.setSaldo(saldo.doubleValue());
                }
            } catch (NumberFormatException e) {
                errores.add("El saldo inicial no es un numero valido.");
            }

            try {
                Cliente cliente = clienteDAO.buscarPorId(Integer.parseInt(idTexto));
                if (cliente == null) {
                    errores.add("El cliente seleccionado no existe.");
                } else if (!"Activo".equalsIgnoreCase(cliente.getEstado())) {
                    errores.add("Solo se pueden abrir cuentas a clientes con estado Activo.");
                } else {
                    cuenta.setIdCliente(cliente.getIdCliente());
                    cuenta.setCodigoCliente(cliente.getCodigoCliente());
                    cuenta.setNombreCliente(cliente.getNombreCompleto());
                }
            } catch (NumberFormatException e) {
                errores.add("Debes indicar un cliente titular valido.");
            }

            if (!errores.isEmpty()) {
                SeguridadUtil.guardarMensaje(request, "flash_error", String.join(" ", errores));
                response.sendRedirect(request.getContextPath() + "/CuentaServlet?accion=nuevo");
                return;
            }

            cuenta.setNumeroCuenta(numero);
            cuenta.setTipoCuenta(tipo);
            cuenta.setEstadoCuenta("Activa");

            if (cuentaDAO.insertar(cuenta)) {
                logger.info("Usuario '{}' abrio la cuenta {} para el cliente {}",
                        SeguridadUtil.usuarioActual(request), numero, cuenta.getCodigoCliente());
                SeguridadUtil.guardarMensaje(request, "flash_success", "Cuenta registrada correctamente.");
                response.sendRedirect(request.getContextPath() + "/CuentaServlet?accion=listar");
            } else {
                SeguridadUtil.guardarMensaje(request, "flash_error",
                        "No se pudo registrar la cuenta. Intenta de nuevo.");
                response.sendRedirect(request.getContextPath() + "/CuentaServlet?accion=nuevo");
            }
        } catch (SQLException e) {
            logger.error("Error al registrar cuenta '{}': {}", numero, e.getMessage());
            SeguridadUtil.guardarMensaje(request, "flash_error",
                    "Ocurrio un error con la base de datos. Revisa los datos e intenta de nuevo.");
            response.sendRedirect(request.getContextPath() + "/CuentaServlet?accion=nuevo");
        }
    }

    // Activar, Inactivar o Bloquear una cuenta. El saldo no se modifica.
    private void cambiarEstado(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            String estado = texto(request, "estado");

            if (!ESTADOS.contains(estado)) {
                SeguridadUtil.guardarMensaje(request, "flash_error", "Estado de cuenta no valido.");
            } else {
                CuentaBancaria cuenta = buscarPorId(id);
                if (cuenta == null) {
                    SeguridadUtil.guardarMensaje(request, "flash_error", "La cuenta no existe.");
                } else if (estado.equalsIgnoreCase(cuenta.getEstadoCuenta())) {
                    SeguridadUtil.guardarMensaje(request, "flash_error", "La cuenta ya esta en estado " + estado + ".");
                } else {
                    cuenta.setEstadoCuenta(estado);
                    if (cuentaDAO.actualizar(cuenta)) {
                        logger.info("Usuario '{}' cambio la cuenta {} a estado {}",
                                SeguridadUtil.usuarioActual(request), cuenta.getNumeroCuenta(), estado);
                        SeguridadUtil.guardarMensaje(request, "flash_success",
                                "La cuenta " + cuenta.getNumeroCuenta() + " ahora esta " + estado + ".");
                    } else {
                        SeguridadUtil.guardarMensaje(request, "flash_error",
                                "No se pudo cambiar el estado de la cuenta.");
                    }
                }
            }
        } catch (NumberFormatException e) {
            SeguridadUtil.guardarMensaje(request, "flash_error", "Identificador de cuenta no valido.");
        } catch (SQLException e) {
            logger.error("Error al cambiar el estado de la cuenta: {}", e.getMessage());
            SeguridadUtil.guardarMensaje(request, "flash_error", "No se pudo cambiar el estado de la cuenta.");
        }
        response.sendRedirect(request.getContextPath() + "/CuentaServlet?accion=listar");
    }

    private CuentaBancaria buscarPorId(int idCuenta) throws SQLException {
        for (CuentaBancaria c : cuentaDAO.listarTodas()) {
            if (c.getIdCuenta() == idCuenta) {
                return c;
            }
        }
        return null;
    }

    private String texto(HttpServletRequest request, String nombre) {
        String valor = request.getParameter(nombre);
        return valor == null ? "" : valor.trim();
    }
}