package com.complaintsystem.controller;

import com.complaintsystem.dao.ComplaintDAO;
import com.complaintsystem.dao.DepartmentDAO;
import com.complaintsystem.model.Complaint;
import com.complaintsystem.model.Department;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

// Mapped to /adminDashboard, /updateStatus
public class AdminServlet extends HttpServlet {

    private ComplaintDAO complaintDAO = new ComplaintDAO();
    private DepartmentDAO departmentDAO = new DepartmentDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        HttpSession session = req.getSession(false);

        if (session == null || !"ADMIN".equals(session.getAttribute("userRole"))) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        if ("/adminDashboard".equals(path)) {
            List<Complaint> complaints = complaintDAO.findAllByOrderByCreatedAtDesc();
            req.setAttribute("complaints", complaints);

            List<Department> departments = departmentDAO.findAll();
            req.setAttribute("departments", departments);

            req.getRequestDispatcher("/adminDashboard.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        HttpSession session = req.getSession(false);

        if (session == null || !"ADMIN".equals(session.getAttribute("userRole"))) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        if ("/updateStatus".equals(path)) {
            String complaintIdStr = req.getParameter("complaintId");
            String status = req.getParameter("status");

            if (complaintIdStr != null && status != null) {
                int complaintId = Integer.parseInt(complaintIdStr);
                Optional<Complaint> complaintOpt = complaintDAO.findById(complaintId);
                
                if (complaintOpt.isPresent()) {
                    Complaint complaint = complaintOpt.get();
                    complaint.setStatus(status);
                    complaintDAO.save(complaint);
                }
            }
            resp.sendRedirect(req.getContextPath() + "/adminDashboard");
        }
    }
}
