package servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Servlet de Autenticacion temporal para permitir pruebas de interfaz
 * y navegacion de roles (Admin, Cajero, Cliente).
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/LoginServlet", "/LogoutServlet"})
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String path = request.getServletPath();
        if ("/LogoutServlet".equalsIgnoreCase(path)) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        response.sendRedirect(request.getContextPath() + "/login.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String usuario = request.getParameter("username");
        String rol = request.getParameter("rol");

        if (usuario == null || usuario.trim().isEmpty()) {
            usuario = "Usuario Demo";
        }

        if (rol == null || rol.trim().isEmpty()) {
            rol = "ADMIN";
        }

        // Crear sesion y registrar rol y usuario
        HttpSession session = request.getSession(true);
        session.setAttribute("usuario", usuario.trim());
        session.setAttribute("rol", rol.trim().toUpperCase());
        session.setAttribute("flash_success", "¡Bienvenido al sistema bancario! Sesión iniciada como " + rol.toUpperCase());

        // Redireccionar al dashboard correspondiente
        if ("ADMIN".equalsIgnoreCase(rol)) {
            response.sendRedirect(request.getContextPath() + "/jsp/admin/dashboard.jsp");
        } else if ("CAJERO".equalsIgnoreCase(rol)) {
            response.sendRedirect(request.getContextPath() + "/jsp/cajero/dashboard.jsp");
        } else if ("CLIENTE".equalsIgnoreCase(rol)) {
            response.sendRedirect(request.getContextPath() + "/jsp/cliente/dashboard.jsp");
        } else {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
        }
    }
}
