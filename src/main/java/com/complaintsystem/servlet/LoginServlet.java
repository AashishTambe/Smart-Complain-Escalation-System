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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String email = req.getParameter("email");
        String password = req.getParameter("password");

        try {
            User user = userDAO.findByEmailAndPassword(email, password);
            if (user == null) {
                req.setAttribute("error", "Invalid email or password");
                req.getRequestDispatcher("login.jsp").forward(req, resp);
                return;
            }

            System.out.println("LoginServlet: User logged in - ID=" + user.getId() + ", Name=" + user.getName() + ", Email=" + user.getEmail() + ", Role=" + user.getRole());

            HttpSession session = req.getSession(true);
            session.setAttribute("userId", user.getId());
            session.setAttribute("userName", user.getName());
            session.setAttribute("userRole", user.getRole());

            if ("USER".equals(user.getRole())) {
                System.out.println("LoginServlet: Redirecting to myComplaints (USER role detected)");
                resp.sendRedirect("myComplaints");
            } else {
                // ADMIN and department staff go to dashboard
                System.out.println("LoginServlet: Redirecting to staffDashboard (Staff/Admin role: " + user.getRole() + ")");
                resp.sendRedirect("staffDashboard");
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}

