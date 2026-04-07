package com.complaintsystem.job;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.complaintsystem.config.DBConnection;
import com.complaintsystem.service.EscalationService;

/**
 * Background job for automated escalation processing.
 * Runs periodically to check and escalate complaints based on dynamic settings.
 */
public class EscalationJob {

    private EscalationService escalationService;

    public EscalationJob() {
        this.escalationService = new EscalationService();
    }

    /**
     * Main job method - checks all complaints that need escalation
     * and escalates them based on dynamic settings
     */
    public void run() {
        System.out.println("⏰ Escalation Job Running - " + System.currentTimeMillis());

        // Get all complaints that might need escalation
        String selectSql =
                "SELECT c.id, c.current_level, c.status, c.priority, " +
                "c.last_status_change, d.id AS dept_id " +
                "FROM complaints c " +
                "JOIN complaint_categories cc ON c.category_id = cc.id " +
                "JOIN departments d ON cc.department_id = d.id " +
                "WHERE c.status IN ('OPEN','IN_PROGRESS','ESCALATED')";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(selectSql);
             ResultSet rs = ps.executeQuery()) {

            int escalatedCount = 0;

            while (rs.next()) {
                int complaintId = rs.getInt("id");

                // Check if this complaint should be escalated
                if (escalationService.shouldEscalate(complaintId)) {
                    // Escalate the complaint
                    if (escalationService.escalateComplaint(complaintId)) {
                        escalatedCount++;
                        System.out.println("✓ Escalated complaint #" + complaintId);
                    }
                }
            }

            System.out.println("✓ Escalation Job Completed - " + escalatedCount + " complaints escalated");

        } catch (SQLException e) {
            System.err.println("✗ Error in Escalation Job: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

