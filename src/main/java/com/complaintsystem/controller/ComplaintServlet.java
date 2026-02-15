package com.complaintsystem.controller;

import com.complaintsystem.dao.ComplaintCategoryDAO;
import com.complaintsystem.dao.ComplaintDAO;
import com.complaintsystem.dao.UserDAO;
import com.complaintsystem.model.Complaint;
import com.complaintsystem.model.ComplaintCategory;
import com.complaintsystem.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

// Mapped to /registerComplaint, /myComplaints, /viewComplaint
public class ComplaintServlet extends HttpServlet {

    private ComplaintDAO complaintDAO = new ComplaintDAO();
    private ComplaintCategoryDAO categoryDAO = new ComplaintCategoryDAO();
    private UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        if ("/registerComplaint".equals(path)) {
            List<ComplaintCategory> categories = categoryDAO.findAllByOrderByNameAsc();
            req.setAttribute("categories", categories);
            req.getRequestDispatcher("/registerComplaint.jsp").forward(req, resp);
        } else if ("/myComplaints".equals(path)) {
            int userId = (Integer) session.getAttribute("userId");
            Optional<User> userOpt = userDAO.findById(userId);
            if (userOpt.isPresent()) {
                List<Complaint> complaints = complaintDAO.findByUserOrderByCreatedAtDesc(userOpt.get());
                req.setAttribute("complaints", complaints);
            }
            req.getRequestDispatcher("/myComplaints.jsp").forward(req, resp);
        } else if ("/viewComplaint".equals(path)) {
            String idStr = req.getParameter("id");
            if (idStr != null) {
                int id = Integer.parseInt(idStr);
                Optional<Complaint> complaintOpt = complaintDAO.findById(id);
                if (complaintOpt.isPresent()) {
                    Complaint complaint = complaintOpt.get();
                    int userId = (Integer) session.getAttribute("userId");
                    String userRole = (String) session.getAttribute("userRole");

                    if (complaint.getUser().getId() == userId || "ADMIN".equals(userRole)) {
                        req.setAttribute("complaint", complaint);
                        req.getRequestDispatcher("/viewComplaint.jsp").forward(req, resp);
                        return;
                    } else {
                        req.setAttribute("error", "Access Denied");
                    }
                }
            }
            resp.sendRedirect(req.getContextPath() + "/myComplaints");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        if ("/registerComplaint".equals(path)) {
            String title = req.getParameter("title");
            String description = req.getParameter("description");
            String priority = req.getParameter("priority");
            String categoryIdStr = req.getParameter("categoryId");

            int userId = (Integer) session.getAttribute("userId");
            int categoryId = Integer.parseInt(categoryIdStr);

            Optional<User> userOpt = userDAO.findById(userId);
            Optional<ComplaintCategory> categoryOpt = categoryDAO.findById(categoryId);

            if (userOpt.isPresent() && categoryOpt.isPresent()) {
                Complaint complaint = new Complaint();
                complaint.setUser(userOpt.get());
                complaint.setCategory(categoryOpt.get());
                complaint.setTitle(title);
                complaint.setDescription(description);
                complaint.setPriority(priority);
                complaint.setStatus("OPEN");
                complaint.setCurrentLevel(1);

                complaintDAO.save(complaint);
                resp.sendRedirect(req.getContextPath() + "/myComplaints");
            } else {
                req.setAttribute("error", "Invalid user or category");
                List<ComplaintCategory> categories = categoryDAO.findAllByOrderByNameAsc();
                req.setAttribute("categories", categories);
                req.getRequestDispatcher("/registerComplaint.jsp").forward(req, resp);
            }
        }
    }
}
