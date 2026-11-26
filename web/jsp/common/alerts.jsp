<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    String errorMsg = (String) request.getAttribute("error");
    if (errorMsg == null) {
        errorMsg = (String) session.getAttribute("flash_error");
        session.removeAttribute("flash_error");
    }

    String successMsg = (String) request.getAttribute("success");
    if (successMsg == null) {
        successMsg = (String) session.getAttribute("flash_success");
        session.removeAttribute("flash_success");
    }
%>

<% if (errorMsg != null && !errorMsg.trim().isEmpty()) { %>
    <div class="alert alert-danger auto-dismiss">
        <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="10"/><line x1="15" y1="9" x2="9" y2="15"/><line x1="9" y1="9" x2="15" y2="15"/></svg>
        <span><%= errorMsg %></span>
    </div>
<% } %>

<% if (successMsg != null && !successMsg.trim().isEmpty()) { %>
    <div class="alert alert-success auto-dismiss">
        <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"/><polyline points="22 4 12 14.01 9 11.01"/></svg>
        <span><%= successMsg %></span>
    </div>
<% } %>
