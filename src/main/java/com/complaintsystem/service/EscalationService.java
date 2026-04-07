package com.complaintsystem.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;

import com.complaintsystem.config.DBConnection;
import com.complaintsystem.dao.EscalationSettingsDAO;
import com.complaintsystem.dao.NotificationDAO;
import com.complaintsystem.model.EscalationSettings;

/**
 * Service class for handling escalation logic.
 * Encapsulates escalation business rules and coordinates with DAOs.
 * Reads escalation settings dynamically from the database instead of using hardcoded values.
 */
public class EscalationService {

    private EscalationSettingsDAO settingsDAO;
    private NotificationDAO notificationDAO;

    public EscalationService() {
        this.settingsDAO = new EscalationSettingsDAO();
        this.notificationDAO = new NotificationDAO();
    }

    /**
     * Check if a complaint should be escalated based on current settings
     *
     * @param complaintId ID of the complaint
     * @param currentLevel Current escalation level
     * @return true if complaint should be escalated
     */
    public boolean shouldEscalate(int complaintId) {
        try {
            EscalationSettings settings = settingsDAO.getActiveSettings();

            // If auto-escalation is disabled, don't escalate
            if (!settings.isAutoEscalationEnabled()) {
                return false;
            }

            String sql = "SELECT c.id, c.current_level, c.priority, c.status, " +
                        "c.last_status_change, d.id AS dept_id " +
                        "FROM complaints c " +
                        "JOIN complaint_categories cc ON c.category_id = cc.id " +
                        "JOIN departments d ON cc.department_id = d.id " +
                        "WHERE c.id = ? AND c.status IN ('OPEN','IN_PROGRESS','ESCALATED')";

            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setInt(1, complaintId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        int level = rs.getInt("current_level");
                        String priority = rs.getString("priority");
                        long hoursSinceChange = getHoursSinceLastchange(rs.getTimestamp("last_status_change"));

                        int slaHours = settings.getSlaHoursForPriority(priority);

                        // Check if SLA has been exceeded
                        return (hoursSinceChange > slaHours) &&
                               (level < settings.getMaxEscalationLevel());
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Escalate a complaint to the next level
     *
     * @param complaintId ID of the complaint to escalate
     * @return true if escalation was successful
     */
    public boolean escalateComplaint(int complaintId) {
        try {
            EscalationSettings settings = settingsDAO.getActiveSettings();

            // First, get the complaint details
            String selectSql = "SELECT c.id, c.current_level, c.status, c.priority, " +
                              "d.id AS dept_id FROM complaints c " +
                              "JOIN complaint_categories cc ON c.category_id = cc.id " +
                              "JOIN departments d ON cc.department_id = d.id " +
                              "WHERE c.id = ?";

            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement(selectSql)) {

                ps.setInt(1, complaintId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        int currentLevel = rs.getInt("current_level");
                        String oldStatus = rs.getString("status");
                        int deptId = rs.getInt("dept_id");

                        // Check if we can escalate further
                        if (currentLevel >= settings.getMaxEscalationLevel()) {
                            return false;
                        }

                        int newLevel = currentLevel + 1;
                        Integer newAssigneeId = findAssigneeForLevel(conn, newLevel, deptId);

                        return updateComplaintLevel(conn, complaintId, newLevel, oldStatus,
                                                  currentLevel, newAssigneeId, settings);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Update complaint to new escalation level
     */
    private boolean updateComplaintLevel(Connection conn, int complaintId, int newLevel,
                                       String oldStatus, int oldLevel, Integer newAssigneeId,
                                       EscalationSettings settings) throws SQLException {

        String updateSql = "UPDATE complaints SET current_level = ?, status = 'ESCALATED', " +
                         "assigned_to = ?, last_status_change = NOW() WHERE id = ?";

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

        // Record history
        String historySql = "INSERT INTO complaint_history " +
                          "(complaint_id, old_status, new_status, old_level, new_level, comment) " +
                          "VALUES (?, ?, 'ESCALATED', ?, ?, 'Auto escalated by system')";
        try (PreparedStatement ps = conn.prepareStatement(historySql)) {
            ps.setInt(1, complaintId);
            ps.setString(2, oldStatus);
            ps.setInt(3, oldLevel);
            ps.setInt(4, newLevel);
            ps.executeUpdate();
        }

        // Create notification if enabled (using DAO)
        if (settings.isNotifyOnEscalation()) {
            createNotification(complaintId, newAssigneeId, newLevel);
        }

        return true;
    }

    /**
     * Find assignee for a specific escalation level in a department
     */
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

    /**
     * Get role for a specific level in a department
     * Maps escalation levels to departmental roles
     */
    private String getRoleForLevelAndDept(int level, int deptId) {
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

    /**
     * Create notification for escalation (using DAO)
     * Notifies: complaint creator, previous assignee, and new assignee
     */
    private void createNotification(int complaintId, Integer newAssigneeId, int level) {
        try {
            // Get complaint details to notify all relevant users
            String sql = "SELECT user_id, assigned_to FROM complaints WHERE id = ?";
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, complaintId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        int creatorId = rs.getInt("user_id");
                        int previousAssigneeId = rs.getInt("assigned_to");
                        
                        // 1. Notify the complaint creator
                        String creatorMsg = "Your complaint #" + complaintId + 
                            " has been escalated to level " + level;
                        notificationDAO.createNotification(complaintId, creatorId, "SYSTEM", creatorMsg);
                        System.out.println("✓ Escalation notification sent to creator (ID: " + creatorId + ")");
                        
                        // 2. Notify the previous assignee (if exists and different from new assignee)
                        if (previousAssigneeId != 0 && 
                            (newAssigneeId == null || previousAssigneeId != newAssigneeId)) {
                            String prevMsg = "Complaint #" + complaintId + 
                                " has been escalated from your care to level " + level;
                            notificationDAO.createNotification(complaintId, previousAssigneeId, "SYSTEM", prevMsg);
                            System.out.println("✓ Escalation notification sent to previous assignee (ID: " + previousAssigneeId + ")");
                        }
                        
                        // 3. Notify the new assignee (if exists)
                        if (newAssigneeId != null && newAssigneeId > 0) {
                            String newMsg = "Complaint #" + complaintId + 
                                " escalated to level " + level + " - Now assigned to you";
                            notificationDAO.createNotification(complaintId, newAssigneeId, "SYSTEM", newMsg);
                            System.out.println("✓ Escalation notification sent to new assignee (ID: " + newAssigneeId + ")");
                        }
                        
                        System.out.println("✓ All escalation notifications created for complaint #" + complaintId);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("✗ Error creating escalation notification: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Calculate hours since last status change
     */
    private long getHoursSinceLastchange(Timestamp lastChange) {
        if (lastChange == null) {
            return 0;
        }
        long diff = System.currentTimeMillis() - lastChange.getTime();
        return diff / (1000 * 60 * 60);
    }

    /**
     * Get current active escalation settings
     */
    public EscalationSettings getActiveSettings() {
        try {
            return settingsDAO.getActiveSettings();
        } catch (SQLException e) {
            e.printStackTrace();
            return settingsDAO.getDefaultSettings();
        }
    }
}
