package com.complaintsystem.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class EscalationServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        String role = session != null ? (String) session.getAttribute("userRole") : null;
        if (role == null || !"ADMIN".equals(role)) {
            resp.sendRedirect("login.jsp");
            return;
        }

        // For now, just forward to escalationPanel.jsp
        // In future, can load escalation rules from DB
        req.getRequestDispatcher("escalationPanel.jsp").forward(req, resp);
    }
}