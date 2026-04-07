package com.complaintsystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.complaintsystem.config.DBConnection;
import com.complaintsystem.model.EscalationSettings;

/**
 * Data Access Object for EscalationSettings.
 * Handles all database operations for escalation configuration.
 */
public class EscalationSettingsDAO {

    /**
     * Get all escalation settings
     */
    public List<EscalationSettings> getAllSettings() throws SQLException {
        List<EscalationSettings> settings = new ArrayList<>();
        String sql = "SELECT * FROM escalation_settings ORDER BY id ASC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                settings.add(mapResultSetToSettings(rs));
            }
        }
        return settings;
    }

    /**
     * Get escalation settings by ID
     */
    public EscalationSettings getSettingsById(int id) throws SQLException {
        String sql = "SELECT * FROM escalation_settings WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToSettings(rs);
                }
            }
        }
        return null;
    }

    /**
     * Get active/default escalation settings (usually id=1 or marked as active)
     */
    public EscalationSettings getActiveSettings() throws SQLException {
        String sql = "SELECT * FROM escalation_settings WHERE id = 1 LIMIT 1";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return mapResultSetToSettings(rs);
            }
        }
        return getDefaultSettings();
    }

    /**
     * Get default settings if none exist
     */
    public EscalationSettings getDefaultSettings() {
        EscalationSettings defaults = new EscalationSettings();
        defaults.setId(0);
        defaults.setSettingName("System Default");
        defaults.setMaxEscalationLevel(3);
        defaults.setLowPrioritySlaHours(24);
        defaults.setMediumPrioritySlaHours(12);
        defaults.setHighPrioritySlaHours(6);
        defaults.setAutoEscalationEnabled(true);
        defaults.setNotifyOnEscalation(true);
        return defaults;
    }


    /**
     * Map ResultSet to EscalationSettings object
     */
    private EscalationSettings mapResultSetToSettings(ResultSet rs) throws SQLException {
        EscalationSettings settings = new EscalationSettings();
        settings.setId(rs.getInt("id"));
        settings.setSettingName(rs.getString("setting_name"));
        settings.setMaxEscalationLevel(rs.getInt("max_escalation_level"));
        settings.setLowPrioritySlaHours(rs.getInt("low_priority_sla_hours"));
        settings.setMediumPrioritySlaHours(rs.getInt("medium_priority_sla_hours"));
        settings.setHighPrioritySlaHours(rs.getInt("high_priority_sla_hours"));
        settings.setAutoEscalationEnabled(rs.getBoolean("auto_escalation_enabled"));
        settings.setNotifyOnEscalation(rs.getBoolean("notify_on_escalation"));
        settings.setEscalationLevels(rs.getString("escalation_levels"));
        settings.setCreatedAt(rs.getString("created_at"));
        settings.setUpdatedAt(rs.getString("updated_at"));
        return settings;
    }
}
