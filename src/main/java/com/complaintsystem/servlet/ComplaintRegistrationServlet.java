package com.complaintsystem.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.complaintsystem.config.DBConnection;

public class ComplaintRegistrationServlet extends HttpServlet {

    private Integer findAssigneeForLevel(Connection conn, int level, int deptId) throws SQLException {
        String role = getRoleForLevelAndDept(level, deptId);

        if (role == null) {
            return null;
        }

        String sql = "SELECT id FROM users WHERE role = ? AND department_id = ? AND active = 1 LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, role);
            ps.setInt(2, deptId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        }
        return null;
    }

    private String getRoleForLevelAndDept(int level, int deptId) {
        // Assuming deptId: 1=IT, 2=HR, 3=Maintenance
        if (deptId == 1) { // IT
            if (level == 1) {
				return "IT_JR_DEV";
			}
            if (level == 2) {
				return "IT_SR_DEV";
			}
            if (level == 3) {
				return "IT_HEAD";
			}
        } else if (deptId == 2) { // HR
            if (level == 1) {
				return "HR_EMPLOYEE";
			}
            if (level == 2) {
				return "HR_MANAGER";
			}
            if (level == 3) {
				return "HR_HEAD";
			}
        } else if (deptId == 3) { // Maintenance
            if (level == 1) {
				return "MAINT_WORKER";
			}
            if (level == 2) {
				return "MAINT_ENGINEER";
			}
            if (level == 3) {
				return "MAINT_HEAD";
			}
        }
        return null;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        Integer userId = session != null ? (Integer) session.getAttribute("userId") : null;
        if (userId == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        try {
            int categoryId = Integer.parseInt(req.getParameter("categoryId"));
            String priority = req.getParameter("priority");
            String title = req.getParameter("title");
            String description = req.getParameter("description");

            // Get department_id from category and assign to appropriate level 1 staff
            int departmentId = 0;
            Integer assignedTo = null;
            try (Connection conn = DBConnection.getConnection()) {
                // Get department_id
                String deptSql = "SELECT department_id FROM complaint_categories WHERE id = ?";
                try (PreparedStatement ps = conn.prepareStatement(deptSql)) {
                    ps.setInt(1, categoryId);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            departmentId = rs.getInt("department_id");
                            // Find assignee for level 1
                            assignedTo = findAssigneeForLevel(conn, 1, departmentId);
                        }
                    }
                }
            }

            // Insert complaint and get generated ID
            String sql = "INSERT INTO complaints " +
                    "(user_id, category_id, title, description, priority, status, current_level, assigned_to) " +
                    "VALUES (?,?,?,?,?,'OPEN',1,?)";

            int complaintId = -1;
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                ps.setInt(1, userId);
                ps.setInt(2, categoryId);
                ps.setString(3, title);
                ps.setString(4, description);
                ps.setString(5, priority);
                if (assignedTo != null) {
                    ps.setInt(6, assignedTo);
                } else {
                    ps.setNull(6, Types.INTEGER);
                }

                ps.executeUpdate();

                // Get generated complaint ID
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        complaintId = generatedKeys.getInt(1);
                    }
                }
            }

            // Create notification for assigned staff
            if (complaintId > 0 && assignedTo != null) {
                String notificationSql = "INSERT INTO notifications " +
                        "(complaint_id, recipient_id, type, message, sent) " +
                        "VALUES (?, ?, 'SYSTEM', ?, 0)";
                try (Connection conn = DBConnection.getConnection();
                     PreparedStatement ps = conn.prepareStatement(notificationSql)) {
                    ps.setInt(1, complaintId);
                    ps.setInt(2, assignedTo);
                    ps.setString(3, "New complaint #" + complaintId + " assigned to you - Priority: " + priority);
                    ps.executeUpdate();
                    System.out.println("✓ Notification created for new complaint #" + complaintId);
                } catch (SQLException e) {
                    System.err.println("✗ Error creating notification: " + e.getMessage());
                    e.printStackTrace();
                }
            }

            // Redirect to My Complaints page with success message
            resp.sendRedirect("myComplaints?success=true&complaintId=" + complaintId);
        } catch (NumberFormatException | SQLException e) {
            System.err.println("✗ Error registering complaint: " + e.getMessage());
            e.printStackTrace();
            // Redirect back to registration form with error message
            resp.sendRedirect("registerComplaint?error=" + e.getMessage());
        }
    }
}
