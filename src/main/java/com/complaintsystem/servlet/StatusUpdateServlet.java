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

public class StatusUpdateServlet extends HttpServlet {

    private final ComplaintDAO complaintDAO = new ComplaintDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        Integer userId = session != null ? (Integer) session.getAttribute("userId") : null;
        String role = session != null ? (String) session.getAttribute("userRole") : null;
        if (userId == null || role == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        int complaintId = Integer.parseInt(req.getParameter("complaintId"));
        String newStatus = req.getParameter("status");

        try {
            // ADMIN can update any complaint
            // Department staff can only update complaints assigned to them
            if ("ADMIN".equals(role)) {
                complaintDAO.updateStatus(complaintId, newStatus);
            } else {
                Complaint complaint = complaintDAO.getById(complaintId);
                if (complaint != null && complaint.getAssignedTo() != null && complaint.getAssignedTo().equals(userId)) {
                    complaintDAO.updateStatus(complaintId, newStatus);
                } else {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "You can only update complaints assigned to you.");
                    return;
                }
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }

        // Redirect based on role
        if ("ADMIN".equals(role)) {
            resp.sendRedirect("staffDashboard");
        } else {
            resp.sendRedirect("staffDashboard");
        }
    }
}

