package com.complaintsystem.servlet;

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
import java.sql.SQLException;
import java.util.List;

public class StaffDashboardServlet extends HttpServlet {

    private final ComplaintDAO complaintDAO = new ComplaintDAO();
    private final DepartmentDAO departmentDAO = new DepartmentDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        Integer userId = session != null ? (Integer) session.getAttribute("userId") : null;
        String role = session != null ? (String) session.getAttribute("userRole") : null;
        String userName = session != null ? (String) session.getAttribute("userName") : null;
        
        if (userId == null || role == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        try {
            List<Complaint> complaints;
            List<Department> departments = null;
            String status = req.getParameter("status");
            String priority = req.getParameter("priority");
            String deptParam = req.getParameter("departmentId");
            Integer departmentId = (deptParam != null && !deptParam.isEmpty())
                    ? Integer.parseInt(deptParam) : null;

            // ADMIN sees all complaints with filters
            if ("ADMIN".equals(role)) {
                departments = departmentDAO.findAll();
                complaints = complaintDAO.getComplaintsForAdmin(status, priority, departmentId);
                req.setAttribute("isAdmin", true);
                req.setAttribute("statusFilter", status);
                req.setAttribute("priorityFilter", priority);
                req.setAttribute("deptFilter", deptParam);
                req.setAttribute("departments", departments);
            } else {
                // Department staff sees only their assigned complaints
                complaints = complaintDAO.getComplaintsAssignedTo(userId);
                req.setAttribute("isAdmin", false);
            }

            req.setAttribute("complaints", complaints);
            req.setAttribute("userName", userName);
            req.setAttribute("userRole", role);
            
            req.getRequestDispatcher("staffDashboard.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}
