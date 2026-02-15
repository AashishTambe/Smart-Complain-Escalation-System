package com.complaintsystem.listener;

import com.complaintsystem.util.DBConnection;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@WebListener
public class EscalationJobListener implements ServletContextListener {

    private ScheduledExecutorService scheduler;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        // Run every 10 minutes
        scheduler.scheduleAtFixedRate(this::runEscalationJob, 0, 10, TimeUnit.MINUTES);
        System.out.println("Escalation Job Scheduler Initialized");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (scheduler != null) {
            scheduler.shutdownNow();
        }
    }

    private void runEscalationJob() {
        System.out.println("Running Escalation Job...");
        // Similar logic to EscalationService but using JDBC directly
        // Note: Ideally moving this logic to a Service/DAO class would be cleaner,
        // but putting here for simplicity as per refactor plan.
        
        String selectSql =
                "SELECT c.id, c.current_level, c.last_status_change, c.priority, c.category_id " +
                "FROM complaints c " +
                "JOIN escalation_rules er " +
                "  ON (er.category_id IS NULL OR er.category_id = c.category_id) " +
                " AND er.priority = c.priority " +
                " AND er.level = c.current_level " +
                "WHERE c.status IN ('OPEN','IN_PROGRESS','ESCALATED') " +
                "  AND TIMESTAMPDIFF(HOUR, c.last_status_change, NOW()) > er.sla_hours";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(selectSql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int complaintId = rs.getInt("id");
                int currentLevel = rs.getInt("current_level");
                escalateComplaint(conn, complaintId, currentLevel);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void escalateComplaint(Connection conn, int complaintId, int currentLevel) {
        int newLevel = currentLevel + 1;
        Integer newAssigneeId = findAssigneeForLevel(conn, newLevel);

        if (newAssigneeId == null) return;

        String updateSql = "UPDATE complaints SET current_level = ?, status = 'ESCALATED', assigned_to = ?, last_status_change = NOW() WHERE id = ?";
        String historySql = "INSERT INTO complaint_history (complaint_id, old_status, new_status, old_level, new_level, changed_by, comment) VALUES (?, 'IN_PROGRESS', 'ESCALATED', ?, ?, NULL, 'Auto escalated by system')";
        String notificationSql = "INSERT INTO notifications (complaint_id, recipient_id, type, message) VALUES (?, ?, 'SYSTEM', ?)";

        try (PreparedStatement updateStmt = conn.prepareStatement(updateSql);
             PreparedStatement historyStmt = conn.prepareStatement(historySql);
             PreparedStatement notifyStmt = conn.prepareStatement(notificationSql)) {

            conn.setAutoCommit(false);

            // Update Complaint
            updateStmt.setInt(1, newLevel);
            updateStmt.setInt(2, newAssigneeId);
            updateStmt.setInt(3, complaintId);
            updateStmt.executeUpdate();

            // History
            historyStmt.setInt(1, complaintId);
            historyStmt.setInt(2, currentLevel);
            historyStmt.setInt(3, newLevel);
            historyStmt.executeUpdate();

            // Notification
            notifyStmt.setInt(1, complaintId);
            notifyStmt.setInt(2, newAssigneeId);
            notifyStmt.setString(3, "Complaint #" + complaintId + " escalated to level " + newLevel);
            notifyStmt.executeUpdate();

            conn.commit();
            conn.setAutoCommit(true);
            System.out.println("Escalated complaint #" + complaintId + " to level " + newLevel);

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private Integer findAssigneeForLevel(Connection conn, int level) {
        String role;
        if (level == 1) role = "OFFICER_L1";
        else if (level == 2) role = "OFFICER_L2";
        else role = "ADMIN";

        String sql = "SELECT id FROM users WHERE role = ? LIMIT 1";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, role);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
