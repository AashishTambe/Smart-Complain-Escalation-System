package com.complaintsystem.model;

/**
 * Model class representing escalation configuration settings.
 * Allows admins to dynamically configure escalation behavior.
 */
public class EscalationSettings {
    private int id;
    private String settingName;           // e.g., "Default", "High Priority"
    private int maxEscalationLevel;       // Maximum level of escalation (default 3)
    private int lowPrioritySlaHours;      // SLA for LOW priority complaints
    private int mediumPrioritySlaHours;   // SLA for MEDIUM priority complaints
    private int highPrioritySlaHours;     // SLA for HIGH priority complaints
    private boolean autoEscalationEnabled; // Enable/disable automatic escalation
    private boolean notifyOnEscalation;   // Send notifications on escalation
    private String escalationLevels;      // JSON or comma-separated for display
    private String createdAt;
    private String updatedAt;

    public EscalationSettings() {
    }

    public EscalationSettings(String settingName, int maxEscalationLevel,
                            int lowPrioritySlaHours, int mediumPrioritySlaHours,
                            int highPrioritySlaHours, boolean autoEscalationEnabled,
                            boolean notifyOnEscalation) {
        this.settingName = settingName;
        this.maxEscalationLevel = maxEscalationLevel;
        this.lowPrioritySlaHours = lowPrioritySlaHours;
        this.mediumPrioritySlaHours = mediumPrioritySlaHours;
        this.highPrioritySlaHours = highPrioritySlaHours;
        this.autoEscalationEnabled = autoEscalationEnabled;
        this.notifyOnEscalation = notifyOnEscalation;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSettingName() {
        return settingName;
    }

    public void setSettingName(String settingName) {
        this.settingName = settingName;
    }

    public int getMaxEscalationLevel() {
        return maxEscalationLevel;
    }

    public void setMaxEscalationLevel(int maxEscalationLevel) {
        this.maxEscalationLevel = maxEscalationLevel;
    }

    public int getLowPrioritySlaHours() {
        return lowPrioritySlaHours;
    }

    public void setLowPrioritySlaHours(int lowPrioritySlaHours) {
        this.lowPrioritySlaHours = lowPrioritySlaHours;
    }

    public int getMediumPrioritySlaHours() {
        return mediumPrioritySlaHours;
    }

    public void setMediumPrioritySlaHours(int mediumPrioritySlaHours) {
        this.mediumPrioritySlaHours = mediumPrioritySlaHours;
    }

    public int getHighPrioritySlaHours() {
        return highPrioritySlaHours;
    }

    public void setHighPrioritySlaHours(int highPrioritySlaHours) {
        this.highPrioritySlaHours = highPrioritySlaHours;
    }

    public boolean isAutoEscalationEnabled() {
        return autoEscalationEnabled;
    }

    public void setAutoEscalationEnabled(boolean autoEscalationEnabled) {
        this.autoEscalationEnabled = autoEscalationEnabled;
    }

    public boolean isNotifyOnEscalation() {
        return notifyOnEscalation;
    }

    public void setNotifyOnEscalation(boolean notifyOnEscalation) {
        this.notifyOnEscalation = notifyOnEscalation;
    }

    public String getEscalationLevels() {
        return escalationLevels;
    }

    public void setEscalationLevels(String escalationLevels) {
        this.escalationLevels = escalationLevels;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * Get SLA hours for a specific priority level
     */
    public int getSlaHoursForPriority(String priority) {
        if (priority == null) {
            return mediumPrioritySlaHours;
        }
        switch (priority.toUpperCase()) {
            case "LOW":
                return lowPrioritySlaHours;
            case "HIGH":
                return highPrioritySlaHours;
            case "MEDIUM":
            default:
                return mediumPrioritySlaHours;
        }
    }

    @Override
    public String toString() {
        return "EscalationSettings{" +
                "id=" + id +
                ", settingName='" + settingName + '\'' +
                ", maxEscalationLevel=" + maxEscalationLevel +
                ", lowPrioritySlaHours=" + lowPrioritySlaHours +
                ", mediumPrioritySlaHours=" + mediumPrioritySlaHours +
                ", highPrioritySlaHours=" + highPrioritySlaHours +
                ", autoEscalationEnabled=" + autoEscalationEnabled +
                ", notifyOnEscalation=" + notifyOnEscalation +
                '}';
    }
}
