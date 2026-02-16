package com.complaintsystem.servlet;

import com.complaintsystem.dao.UserDAO;
import com.complaintsystem.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        // Basic validation
        if (name == null || name.trim().isEmpty() || 
            email == null || email.trim().isEmpty() || 
            password == null || password.trim().isEmpty()) {
            
            req.setAttribute("error", "All fields are required");
            req.getRequestDispatcher("register.jsp").forward(req, resp);
            return;
        }

        try {
            // Check if email already exists
            User existingUser = userDAO.findByEmail(email);
            if (existingUser != null) {
                req.setAttribute("error", "Email already exists");
                req.getRequestDispatcher("register.jsp").forward(req, resp);
                return;
            }
             User newUser = new User();
            newUser.setName(name);
            newUser.setEmail(email);
            newUser.setPassword(password); // In production, hash this!
            newUser.setRole("USER");
            newUser.setActive(true);

            userDAO.createUser(newUser);
            
            // Redirect to login with success message
            resp.sendRedirect("login.jsp?registered=true");

        } catch (SQLException e) {
            e.printStackTrace();
            if (e.getMessage().contains("Duplicate entry")) {
                 req.setAttribute("error", "Email already exists");
            } else {
                 req.setAttribute("error", "Registration failed: " + e.getMessage());
            }
            req.getRequestDispatcher("register.jsp").forward(req, resp);
        }
    }
}
