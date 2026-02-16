package com.complaintsystem.servlet;

import com.complaintsystem.dao.ComplaintDAO;
import com.complaintsystem.model.Complaint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class MyComplaintsServlet extends HttpServlet {

    private final ComplaintDAO complaintDAO = new ComplaintDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        Integer userId = session != null ? (Integer) session.getAttribute("userId") : null;
        String userRole = session != null ? (String) session.getAttribute("userRole") : null;
        if (userId == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        try {
            List<Complaint> complaints;
            if ("USER".equals(userRole)) {
                // Regular users see complaints they filed
                complaints = complaintDAO.getComplaintsByUser(userId);
            } else if ("ADMIN".equals(userRole)) {
                // Admins see complaints assigned to them
                complaints = complaintDAO.getComplaintsAssignedTo(userId);
            } else {
                // Other department staff see assigned complaints
                complaints = complaintDAO.getComplaintsAssignedTo(userId);
            }
            req.setAttribute("complaints", complaints);
            req.getRequestDispatcher("myComplaints.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}

