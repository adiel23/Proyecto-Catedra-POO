package servlet;

import dao.ClienteDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modelo.Cliente;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@WebServlet(name = "ClienteServlet", urlPatterns = {"/ClienteServlet"})
public class ClienteServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(ClienteServlet.class);
    private static final List<String> ESTADOS = Arrays.asList("Activo", "Inactivo", "Bloqueado");

    private static final String VISTA_LISTA = "/jsp/admin/clientes-lista.jsp";
    private static final String VISTA_FORM = "/jsp/admin/cliente-form.jsp";

    private final ClienteDAO clienteDAO = new ClienteDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!SeguridadUtil.exigirRol(request, response, "ADMIN")) {
            return;
        }
        String accion = request.getParameter("accion");
        if (accion == null || accion.trim().isEmpty()) {
            accion = "listar";
        }

        switch (accion) {
            case "editar":
                editar(request, response);
                break;
            case "eliminar":
                darDeBaja(request, response);
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
        if (accion == null) {
            accion = "";
        }

        switch (accion) {
            case "insertar":
                guardar(request, response, false);
                break;
            case "actualizar":
                guardar(request, response, true);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/ClienteServlet?accion=listar");
        }
    }

    private void listar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setAttribute("listaClientes", clienteDAO.listarTodos());
        } catch (SQLException e) {
            logger.error("No se pudo listar clientes: {}", e.getMessage());
            SeguridadUtil.guardarMensaje(request, "flash_error",
                    "No se pudo consultar la lista de clientes. Intenta de nuevo.");
            request.setAttribute("listaClientes", new ArrayList<Cliente>());
        }
        request.getRequestDispatcher(VISTA_LISTA).forward(request, response);
    }

    private void editar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Cliente cliente = clienteDAO.buscarPorId(id);
            if (cliente == null) {
                SeguridadUtil.guardarMensaje(request, "flash_error", "El cliente no existe.");
                response.sendRedirect(request.getContextPath() + "/ClienteServlet?accion=listar");
                return;
            }
            request.setAttribute("cliente", cliente);
            request.getRequestDispatcher(VISTA_FORM).forward(request, response);
        } catch (NumberFormatException e) {
            SeguridadUtil.guardarMensaje(request, "flash_error", "Identificador de cliente no valido.");
            response.sendRedirect(request.getContextPath() + "/ClienteServlet?accion=listar");
        } catch (SQLException e) {
            logger.error("No se pudo cargar el cliente: {}", e.getMessage());
            SeguridadUtil.guardarMensaje(request, "flash_error", "No se pudo cargar el cliente.");
            response.sendRedirect(request.getContextPath() + "/ClienteServlet?accion=listar");
        }
    }

    // "Baja": no borra el registro, cambia el estado del cliente a Inactivo.
    private void darDeBaja(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Cliente cliente = clienteDAO.buscarPorId(id);
            if (cliente == null) {
                SeguridadUtil.guardarMensaje(request, "flash_error", "El cliente no existe.");
            } else if ("Inactivo".equalsIgnoreCase(cliente.getEstado())) {
                SeguridadUtil.guardarMensaje(request, "flash_error", "El cliente ya esta Inactivo.");
            } else {
                cliente.setEstado("Inactivo");
                if (clienteDAO.actualizar(cliente)) {
                    logger.info("Usuario '{}' dio de baja al cliente {}",
                            SeguridadUtil.usuarioActual(request), cliente.getCodigoCliente());
                    SeguridadUtil.guardarMensaje(request, "flash_success",
                            "Cliente dado de baja correctamente (estado Inactivo).");
                } else {
                    SeguridadUtil.guardarMensaje(request, "flash_error", "No se pudo dar de baja al cliente.");
                }
            }
        } catch (NumberFormatException e) {
            SeguridadUtil.guardarMensaje(request, "flash_error", "Identificador de cliente no valido.");
        } catch (SQLException e) {
            logger.error("Error al dar de baja al cliente: {}", e.getMessage());
            SeguridadUtil.guardarMensaje(request, "flash_error", "No se pudo dar de baja al cliente.");
        }
        response.sendRedirect(request.getContextPath() + "/ClienteServlet?accion=listar");
    }

    private void guardar(HttpServletRequest request, HttpServletResponse response, boolean modoEdicion)
            throws ServletException, IOException {
        Cliente cliente = leerFormulario(request);
        List<String> errores = new ArrayList<>();

        try {
            if (modoEdicion) {
                Cliente existente = clienteDAO.buscarPorId(cliente.getIdCliente());
                if (existente == null) {
                    SeguridadUtil.guardarMensaje(request, "flash_error",
                            "El cliente que intentas editar no existe.");
                    response.sendRedirect(request.getContextPath() + "/ClienteServlet?accion=listar");
                    return;
                }
                cliente.setCodigoCliente(existente.getCodigoCliente());
            }

            validar(cliente, modoEdicion, errores);

            if (!errores.isEmpty()) {
                SeguridadUtil.guardarMensaje(request, "flash_error", String.join(" ", errores));
                request.setAttribute("cliente", cliente);
                request.getRequestDispatcher(VISTA_FORM).forward(request, response);
                return;
            }

            boolean ok = modoEdicion ? clienteDAO.actualizar(cliente) : clienteDAO.insertar(cliente);
            if (ok) {
                logger.info("Usuario '{}' {} al cliente {}", SeguridadUtil.usuarioActual(request),
                        modoEdicion ? "actualizo" : "registro", cliente.getCodigoCliente());
                SeguridadUtil.guardarMensaje(request, "flash_success",
                        modoEdicion ? "Cliente actualizado correctamente." : "Cliente registrado correctamente.");
                response.sendRedirect(request.getContextPath() + "/ClienteServlet?accion=listar");
            } else {
                SeguridadUtil.guardarMensaje(request, "flash_error",
                        "No se pudo guardar el cliente. Intenta de nuevo.");
                request.setAttribute("cliente", cliente);
                request.getRequestDispatcher(VISTA_FORM).forward(request, response);
            }
        } catch (SQLException e) {
            logger.error("Error al guardar cliente '{}': {}", cliente.getCodigoCliente(), e.getMessage());
            SeguridadUtil.guardarMensaje(request, "flash_error",
                    "Ocurrio un error con la base de datos. Revisa los datos e intenta de nuevo.");
            request.setAttribute("cliente", cliente);
            request.getRequestDispatcher(VISTA_FORM).forward(request, response);
        }
    }

    private Cliente leerFormulario(HttpServletRequest request) {
        Cliente c = new Cliente();
        String id = request.getParameter("idCliente");
        if (id != null && !id.trim().isEmpty()) {
            try {
                c.setIdCliente(Integer.parseInt(id.trim()));
            } catch (NumberFormatException ignorada) {
                // se valida despues en modo edicion
            }
        }
        c.setCodigoCliente(texto(request, "codigoCliente").toUpperCase());
        c.setDui(texto(request, "dui"));
        c.setNombreCompleto(texto(request, "nombreCompleto"));
        c.setTelefono(texto(request, "telefono"));
        c.setCorreoElectronico(texto(request, "correoElectronico"));
        c.setDireccion(texto(request, "direccion"));
        String estado = texto(request, "estado");
        c.setEstado(estado.isEmpty() ? "Activo" : estado);
        return c;
    }

    private String texto(HttpServletRequest request, String nombre) {
        String valor = request.getParameter(nombre);
        return valor == null ? "" : valor.trim();
    }

    private void validar(Cliente c, boolean modoEdicion, List<String> errores) throws SQLException {
        // El formato del codigo solo se exige al registrar (al editar el codigo no cambia)
        if (!modoEdicion) {
            if (!c.getCodigoCliente().matches("^CL\\d{1,10}$")) {
                errores.add("El codigo debe tener el formato CL seguido de numeros (ej. CL007).");
            } else if (clienteDAO.buscarPorCodigo(c.getCodigoCliente()) != null) {
                errores.add("Ya existe un cliente con ese codigo.");
            }
        }

        if (!c.getDui().matches("^\\d{8}-\\d$")) {
            errores.add("El DUI debe tener el formato 00000000-0.");
        } else {
            Cliente otro = clienteDAO.buscarPorDui(c.getDui());
            if (otro != null && otro.getIdCliente() != c.getIdCliente()) {
                errores.add("Ya existe un cliente registrado con ese DUI.");
            }
        }

        if (c.getNombreCompleto().length() < 3) {
            errores.add("El nombre completo es obligatorio (minimo 3 caracteres).");
        }
        if (!c.getTelefono().isEmpty() && !c.getTelefono().matches("^[0-9+\\- ]{8,15}$")) {
            errores.add("El telefono no es valido (ej. 7777-7777).");
        }
        if (!c.getCorreoElectronico().isEmpty()
                && !c.getCorreoElectronico().matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            errores.add("El correo electronico no es valido.");
        }
        if (!ESTADOS.contains(c.getEstado())) {
            errores.add("El estado debe ser Activo, Inactivo o Bloqueado.");
        }
    }
}