package servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public final class SeguridadUtil {

    private SeguridadUtil() {
    }

    public static String rolActual(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        Object rol = session.getAttribute("rol");
        return rol == null ? null : rol.toString();
    }

    public static String usuarioActual(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        Object usuario = session.getAttribute("usuario");
        return usuario == null ? null : usuario.toString();
    }

    public static boolean exigirRol(HttpServletRequest request, HttpServletResponse response,
                                    String... permitidos) throws IOException {
        String rol = rolActual(request);
        if (rol == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return false;
        }
        for (String permitido : permitidos) {
            if (permitido.equalsIgnoreCase(rol)) {
                return true;
            }
        }
        guardarMensaje(request, "flash_error", "Acceso denegado: no tienes permisos para esta operacion.");
        response.sendRedirect(dashboardDe(rol, request.getContextPath()));
        return false;
    }

    public static boolean esAdmin(HttpServletRequest request) {
        return "ADMIN".equalsIgnoreCase(rolActual(request));
    }

    public static void guardarMensaje(HttpServletRequest request, String clave, String mensaje) {
        request.getSession(true).setAttribute(clave, mensaje);
    }

    public static String tomarMensaje(HttpServletRequest request, String clave) {
        Object enRequest = request.getAttribute(clave);
        if (enRequest != null) {
            return enRequest.toString();
        }
        HttpSession session = request.getSession(false);
        if (session != null) {
            Object enSesion = session.getAttribute(clave);
            if (enSesion != null) {
                session.removeAttribute(clave);
                return enSesion.toString();
            }
        }
        return null;
    }

    public static String dashboardDe(String rol, String contexto) {
        if ("ADMIN".equalsIgnoreCase(rol)) {
            return contexto + "/jsp/admin/dashboard.jsp";
        } else if ("CAJERO".equalsIgnoreCase(rol)) {
            return contexto + "/jsp/cajero/dashboard.jsp";
        } else if ("CLIENTE".equalsIgnoreCase(rol)) {
            return contexto + "/jsp/cliente/dashboard.jsp";
        }
        return contexto + "/login.jsp";
    }

    public static String esc(Object valor) {
        if (valor == null) {
            return "";
        }
        String texto = valor.toString();
        StringBuilder sb = new StringBuilder(texto.length() + 16);
        for (int i = 0; i < texto.length(); i++) {
            char c = texto.charAt(i);
            switch (c) {
                case '&': sb.append("&amp;"); break;
                case '<': sb.append("&lt;"); break;
                case '>': sb.append("&gt;"); break;
                case '"': sb.append("&quot;"); break;
                case '\'': sb.append("&#39;"); break;
                default: sb.append(c);
            }
        }
        return sb.toString();
    }
}