package com.complaintsystem.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.complaintsystem.dao.ComplaintDAO;
import com.complaintsystem.model.Complaint;

public class MyComplaintsServlet extends HttpServlet {

    private final ComplaintDAO complaintDAO = new ComplaintDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        Integer userId = session != null ? (Integer) session.getAttribute("userId") : null;
        String userRole = session != null ? (String) session.getAttribute("userRole") : null;

        System.out.println("MyComplaintsServlet: Session check - Session exists: " + (session != null) + ", UserId: " + userId + ", Role: " + userRole);

        if (userId == null) {
            System.out.println("MyComplaintsServlet: No valid session found, redirecting to login");
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        try {
            List<Complaint> complaints;
            if ("USER".equals(userRole)) {
                // Regular users see complaints they filed
                System.out.println("MyComplaintsServlet: Fetching complaints for USER - userId: " + userId);
                complaints = complaintDAO.getComplaintsByUser(userId);
            } else if ("ADMIN".equals(userRole)) {
                // Admins see complaints assigned to them
                System.out.println("MyComplaintsServlet: Fetching complaints for ADMIN - userId: " + userId);
                complaints = complaintDAO.getComplaintsAssignedTo(userId);
            } else {
                // Other department staff see assigned complaints
                System.out.println("MyComplaintsServlet: Fetching complaints for STAFF - userId: " + userId + ", Role: " + userRole);
                complaints = complaintDAO.getComplaintsAssignedTo(userId);
            }
            System.out.println("MyComplaintsServlet: Found " + (complaints != null ? complaints.size() : 0) + " complaints");
            req.setAttribute("complaints", complaints);
            req.getRequestDispatcher("myComplaints.jsp").forward(req, resp);
        } catch (SQLException e) {
            System.err.println("MyComplaintsServlet: Database error - " + e.getMessage());
            e.printStackTrace();
            throw new ServletException(e);
        }
    }
}

