package com.complaintsystem.job;

import com.complaintsystem.config.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class EscalationJob {

    public void run() {
        String selectSql =
                "SELECT c.id, c.current_level, c.status, d.id AS dept_id " +
                "FROM complaints c " +
                "JOIN complaint_categories cc ON c.category_id = cc.id " +
                "JOIN departments d ON cc.department_id = d.id " +
                "JOIN escalation_rules er " +
                "  ON (er.category_id IS NULL OR er.category_id = c.category_id) " +
                " AND er.priority = c.priority " +
                " AND er.level = c.current_level " +
                "WHERE c.status IN ('OPEN','IN_PROGRESS','ESCALATED') " +
                "  AND TIMESTAMPDIFF(HOUR, c.last_status_change, NOW()) > er.sla_hours";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(selectSql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int complaintId = rs.getInt("id");
                int currentLevel = rs.getInt("current_level");
                String oldStatus = rs.getString("status");
                int deptId = rs.getInt("dept_id");
                escalateComplaint(conn, complaintId, currentLevel, oldStatus, deptId);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void escalateComplaint(Connection conn, int complaintId, int currentLevel, String oldStatus, int deptId) throws SQLException {
        int newLevel = currentLevel + 1;

        Integer newAssigneeId = findAssigneeForLevel(conn, newLevel, deptId);

        String updateSql =
                "UPDATE complaints " +
                "SET current_level = ?, status = 'ESCALATED', assigned_to = ?, last_status_change = NOW() " +
                "WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(updateSql)) {
            ps.setInt(1, newLevel);
            if (newAssigneeId != null) {
                ps.setInt(2, newAssigneeId);
            } else {
                ps.setNull(2, Types.INTEGER);
            }
            ps.setInt(3, complaintId);
            ps.executeUpdate();
        }

        String historySql =
                "INSERT INTO complaint_history " +
                "(complaint_id, old_status, new_status, old_level, new_level, changed_by, comment) " +
                "VALUES (?, ?, 'ESCALATED', ?, ?, NULL, 'Auto escalated by system')";
        try (PreparedStatement ps = conn.prepareStatement(historySql)) {
            ps.setInt(1, complaintId);
            ps.setString(2, oldStatus);
            ps.setInt(3, currentLevel);
            ps.setInt(4, newLevel);
            ps.executeUpdate();
        }

        createNotification(conn, complaintId, newAssigneeId, newLevel);
    }

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
            if (level == 1) return "IT_JR_DEV";
            if (level == 2) return "IT_SR_DEV";
            if (level == 3) return "IT_HEAD";
        } else if (deptId == 2) { // HR
            if (level == 1) return "HR_EMPLOYEE";
            if (level == 2) return "HR_MANAGER";
            if (level == 3) return "HR_HEAD";
        } else if (deptId == 3) { // Maintenance
            if (level == 1) return "MAINT_WORKER";
            if (level == 2) return "MAINT_ENGINEER";
            if (level == 3) return "MAINT_HEAD";
        }
        return null;
    }

    private void createNotification(Connection conn, int complaintId, Integer assigneeId, int level)
            throws SQLException {
        if (assigneeId == null) {
            return;
        }

        String sql = "INSERT INTO notifications (complaint_id, recipient_id, type, message) " +
                     "VALUES (?, ?, 'SYSTEM', ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, complaintId);
            ps.setInt(2, assigneeId);
            ps.setString(3, "Complaint #" + complaintId + " escalated to level " + level);
            ps.executeUpdate();
        }
    }
}

