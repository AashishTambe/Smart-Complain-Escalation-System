package com.complaintsystem.controller;

import com.complaintsystem.dao.UserDAO;
import com.complaintsystem.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;

// Mapped via web.xml to /login, /register, /logout
public class AuthServlet extends HttpServlet {

    private UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();

        if ("/login".equals(path)) {
            // Forward to login.jsp
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        } else if ("/register".equals(path)) {
            // Forward to register.jsp
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
        } else if ("/logout".equals(path)) {
            HttpSession session = req.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();

        if ("/login".equals(path)) {
            handleLogin(req, resp);
        } else if ("/register".equals(path)) {
            handleRegister(req, resp);
        }
    }

    private void handleLogin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        Optional<User> userOpt = userDAO.findByEmail(email);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getPassword().equals(password)) {
                HttpSession session = req.getSession();
                session.setAttribute("userId", user.getId());
                session.setAttribute("userName", user.getName());
                session.setAttribute("userRole", user.getRole());

                if ("ADMIN".equals(user.getRole())) {
                    resp.sendRedirect(req.getContextPath() + "/adminDashboard");
                } else if ("OFFICER_L1".equals(user.getRole()) || "OFFICER_L2".equals(user.getRole())) {
                    // For officers, where do they go? Usually admin dashboard or a specific officer page?
                    // The AdminController had /adminDashboard for ADMIN only.
                    // ComplaintController likely handles officer views.
                    // Let's assume for now they might have a different landing or standard works.
                    // Looking at AuthController, it redirected /myComplaints for non-ADMIN.
                    resp.sendRedirect(req.getContextPath() + "/myComplaints");
                } else {
                    resp.sendRedirect(req.getContextPath() + "/myComplaints");
                }
                return;
            }
        }

        req.setAttribute("error", "Invalid email or password");
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }

    private void handleRegister(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        if (userDAO.findByEmail(email).isPresent()) {
            req.setAttribute("error", "Email already exists");
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
            return;
        }

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole("USER");
        user.setActive(true);

        userDAO.save(user);

        resp.sendRedirect(req.getContextPath() + "/login?registered=true");
    }
}
