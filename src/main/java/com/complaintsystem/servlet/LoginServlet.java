package com.complaintsystem.servlet;

import com.complaintsystem.dao.UserDAO;
import com.complaintsystem.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

public class LoginServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String identifier = req.getParameter("identifier");
        String password = req.getParameter("password");

        // Validate input
        if (identifier == null || identifier.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            req.setAttribute("error", "Email/Phone and password are required.");
            req.getRequestDispatcher("login.jsp").forward(req, resp);
            return;
        }

        try {
            User user = userDAO.findByIdentifierAndPassword(identifier, password);
            if (user == null) {
                req.setAttribute("error", "Invalid Email/Phone or password");
                req.getRequestDispatcher("login.jsp").forward(req, resp);
                return;
            }

            System.out.println("LoginServlet: User logged in - ID=" + user.getId() + ", Name=" + user.getName() + ", Email=" + user.getEmail() + ", Role=" + user.getRole());

            HttpSession session = req.getSession(true);
            session.setMaxInactiveInterval(30 * 60); // 30 minutes
            session.setAttribute("userId", user.getId());
            session.setAttribute("userName", user.getName());
            session.setAttribute("userRole", user.getRole());
            
            // Verify session was created
            if (session.getAttribute("userId") == null) {
                System.err.println("LoginServlet ERROR: Session not properly created!");
                req.setAttribute("error", "Session creation failed. Please try again.");
                req.getRequestDispatcher("login.jsp").forward(req, resp);
                return;
            }

            if ("USER".equals(user.getRole())) {
                System.out.println("LoginServlet: Redirecting to myComplaints (USER role detected). Session ID: " + session.getId());
                resp.sendRedirect(req.getContextPath() + "/myComplaints");
            } else {
                // ADMIN and department staff go to dashboard
                System.out.println("LoginServlet: Redirecting to staffDashboard (Staff/Admin role: " + user.getRole() + "). Session ID: " + session.getId());
                resp.sendRedirect(req.getContextPath() + "/staffDashboard");
            }
        } catch (SQLException e) {
            System.err.println("LoginServlet: Database error - " + e.getMessage());
            e.printStackTrace();
            req.setAttribute("error", "Database error: " + e.getMessage());
            try {
                req.getRequestDispatcher("login.jsp").forward(req, resp);
            } catch (ServletException ex) {
                throw new ServletException(ex);
            }
        }
    }
}

