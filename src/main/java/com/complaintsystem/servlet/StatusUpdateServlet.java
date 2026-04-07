package com.complaintsystem.servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.complaintsystem.dao.ComplaintDAO;
import com.complaintsystem.dao.NotificationDAO;
import com.complaintsystem.model.Complaint;

public class StatusUpdateServlet extends HttpServlet {

    private final ComplaintDAO complaintDAO = new ComplaintDAO();
    private final NotificationDAO notificationDAO = new NotificationDAO();

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
            // Get complaint details before updating
            Complaint complaint = complaintDAO.getById(complaintId);
            if (complaint == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Complaint not found.");
                return;
            }

            // ADMIN can update any complaint
            // Department staff can only update complaints assigned to them
            if ("ADMIN".equals(role)) {
                complaintDAO.updateStatus(complaintId, newStatus);

                // Create notification for the complaint filer
                if (complaint.getUserId() > 0) {
                    String message = "Your complaint #" + complaintId + " status updated to: " + newStatus;
                    notificationDAO.createNotification(complaintId, complaint.getUserId(), "SYSTEM", message);
                }

                // Create notification for assigned staff
                if (complaint.getAssignedTo() != null && complaint.getAssignedTo() > 0) {
                    String message = "Complaint #" + complaintId + " status changed to: " + newStatus;
                    notificationDAO.createNotification(complaintId, complaint.getAssignedTo(), "SYSTEM", message);
                }

            } else {
                if (complaint.getAssignedTo() != null && complaint.getAssignedTo().equals(userId)) {
                    complaintDAO.updateStatus(complaintId, newStatus);

                    // Notify the complaint creator
                    String message = "Your complaint #" + complaintId + " has been updated to: " + newStatus;
                    notificationDAO.createNotification(complaintId, complaint.getUserId(), "SYSTEM", message);

                    System.out.println("Notification created for complaint #" + complaintId +
                                     " - Status: " + newStatus);
                } else {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN,
                            "You can only update complaints assigned to you.");
                    return;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error updating complaint status: " + e.getMessage());
            e.printStackTrace();
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

