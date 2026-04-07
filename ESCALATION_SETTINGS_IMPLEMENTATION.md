# Smart Complaint Escalation System - Dynamic Settings Implementation

## Overview
This document provides a comprehensive guide to the newly implemented dynamic escalation settings system. Instead of hardcoded escalation logic, administrators can now configure escalation behaviors directly from the Admin Panel.

## Architecture & Components

### 1. **Database Schema**
The new `escalation_settings` table stores dynamic escalation configurations:

```sql
CREATE TABLE escalation_settings (
    id INT AUTO_INCREMENT PRIMARY KEY,
    setting_name VARCHAR(100) NOT NULL UNIQUE,
    max_escalation_level INT NOT NULL DEFAULT 3,
    low_priority_sla_hours INT NOT NULL DEFAULT 24,
    medium_priority_sla_hours INT NOT NULL DEFAULT 12,
    high_priority_sla_hours INT NOT NULL DEFAULT 6,
    auto_escalation_enabled TINYINT(1) DEFAULT 1,
    notify_on_escalation TINYINT(1) DEFAULT 1,
    escalation_levels TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

**Key Fields:**
- `setting_name`: Descriptive name (e.g., "Default", "High Priority")
- `max_escalation_level`: Maximum escalation levels (1-5)
- `low_priority_sla_hours`: SLA for LOW priority complaints
- `medium_priority_sla_hours`: SLA for MEDIUM priority complaints
- `high_priority_sla_hours`: SLA for HIGH priority complaints
- `auto_escalation_enabled`: Toggle automatic escalation on/off
- `notify_on_escalation`: Send notifications when escalating
- `escalation_levels`: Textual description of escalation hierarchy

### 2. **Model Layer**
**File:** `EscalationSettings.java`

```java
public class EscalationSettings {
    private int id;
    private String settingName;
    private int maxEscalationLevel;
    private int lowPrioritySlaHours;
    private int mediumPrioritySlaHours;
    private int highPrioritySlaHours;
    private boolean autoEscalationEnabled;
    private boolean notifyOnEscalation;
    private String escalationLevels;
    private String createdAt;
    private String updatedAt;
    
    // Getters, setters, and utility methods
    public int getSlaHoursForPriority(String priority) { ... }
}
```

### 3. **Data Access Layer (DAO)**
**File:** `EscalationSettingsDAO.java`

Provides database operations:
- `getAllSettings()`: Fetch all configurations
- `getSettingsById(int id)`: Get specific configuration
- `getActiveSettings()`: Get the active/default settings
- `createSettings(EscalationSettings)`: Create new configuration
- `updateSettings(EscalationSettings)`: Update existing configuration
- `deleteSettings(int id)`: Delete configuration (non-default only)

### 4. **Service Layer**
**File:** `EscalationService.java`

Core business logic for escalation:

```java
public class EscalationService {
    // Check if complaint should be escalated
    public boolean shouldEscalate(int complaintId) { ... }
    
    // Escalate complaint to next level
    public boolean escalateComplaint(int complaintId) { ... }
    
    // Apply dynamic settings to escalation
    public EscalationSettings getActiveSettings() { ... }
}
```

**Key Features:**
- Reads settings from database instead of hardcoded values
- Calculates SLA based on complaint priority and active settings
- Automatically finds assignee for escalation level
- Creates notifications if enabled in settings
- All logic is configurable and reusable

### 5. **Admin Configuration (DB-based)**
In this codebase, escalation configuration is applied dynamically from the MySQL table `escalation_settings` via:
- `EscalationSettingsDAO` (loads the active/default settings)
- `EscalationService` (uses the settings to decide SLA-based escalation)

Note: an Admin CRUD UI (`AdminEscalationSettingsServlet` and `adminEscalationSettings.jsp`) is not wired in the current repository. To modify escalation SLAs, update the relevant row(s) in MySQL (commonly the default row `id=1`).

### 6. **User Interface (JSP)**
Admin JSP/servlet UI for editing escalation settings is not included/wired in this repository.

## Usage Workflow

### For Administrators:
In this repository, administrators update SLA/escalation settings directly in MySQL (because the admin CRUD UI is not wired in code).

1. **Update escalation SLA values in `escalation_settings`**
```sql
USE complaint_system;

UPDATE escalation_settings
SET max_escalation_level = 3,
    low_priority_sla_hours = 24,
    medium_priority_sla_hours = 12,
    high_priority_sla_hours = 6,
    auto_escalation_enabled = 1,
    notify_on_escalation = 1
WHERE id = 1;
```

2. **Verify escalation behavior**
   - Create/adjust a test complaint
   - Set `last_status_change` older than the SLA for its priority
   - Wait for `EscalationJob` (scheduler) to run and trigger escalation accordingly

### For System:

1. **Automatic Escalation Processing**
   - EscalationJob runs periodically (configured in web.xml listener)
   - For each complaint, calls `shouldEscalate()`
   - Reads active settings from database
   - Compares complaint age with SLA for its priority
   - Escalates if SLA exceeded and max level not reached

2. **Active Settings**
   - System uses settings with id=1 (Default) by default
   - To use different settings, swap id=1 settings with another config
   - Or modify settings with id=1 directly

## Integration Guide

### Step 1: Database Setup
Run the updated `data.sql`:
```sql
-- Includes the new escalation_settings table and default data
```

### Step 2: Update escalation SLAs in MySQL
The escalation scheduler is already wired in `EscalationScheduler`/`EscalationJob`. To change SLA values, update the default/active row in `escalation_settings` (commonly `id=1`) using SQL.

### Step 3: Update EscalationJob
Already updated to use `EscalationService`. The job now:
- Retrieves dynamic settings
- Respects admin configurations
- Provides logging for monitoring

## Configuration Examples

### Example 1: Standard Configuration
- **Name:** Default
- **Max Level:** 3
- **Low SLA:** 24 hours
- **Medium SLA:** 12 hours
- **High SLA:** 6 hours
- **Auto Escalation:** Enabled
- **Notifications:** Enabled

### Example 2: High Priority Configuration
- **Name:** High Priority
- **Max Level:** 2
- **Low SLA:** 48 hours
- **Medium SLA:** 24 hours
- **High SLA:** 2 hours
- **Auto Escalation:** Enabled
- **Notifications:** Enabled

### Example 3: Manual Resolution
- **Name:** Manual Escalation Only
- **Max Level:** 3
- **Low SLA:** 72 hours
- **Medium SLA:** 48 hours
- **High SLA:** 24 hours
- **Auto Escalation:** Disabled
- **Notifications:** Disabled

## API Reference

### EscalationService Methods

```java
// Check if complaint needs escalation
boolean shouldEscalate(int complaintId)
// Returns: true if SLA exceeded and max level not reached

// Escalate complaint to next level
boolean escalateComplaint(int complaintId)
// Returns: true if escalation successful

// Get current active settings
EscalationSettings getActiveSettings()
// Returns: Active escalation settings or defaults
```

### EscalationSettingsDAO Methods

```java
// Get all settings
List<EscalationSettings> getAllSettings()

// Get specific setting
EscalationSettings getSettingsById(int id)

// Get active setting
EscalationSettings getActiveSettings()

// Create new setting
int createSettings(EscalationSettings settings)

// Update setting
boolean updateSettings(EscalationSettings settings)

// Delete setting
boolean deleteSettings(int id)
```

## Database Queries

### Get SLA for a Complaint
```sql
SELECT es.* FROM escalation_settings es
WHERE es.id = 1
```

### Find Complaints Due for Escalation
```sql
SELECT c.id, c.priority, 
       TIMESTAMPDIFF(HOUR, c.last_status_change, NOW()) as hours_elapsed
FROM complaints c
WHERE c.status IN ('OPEN','IN_PROGRESS','ESCALATED')
      AND c.current_level < (SELECT max_escalation_level FROM escalation_settings WHERE id = 1)
```

## Error Handling

The system handles:
- Invalid input validation (all numeric values must be > 0)
- SQL exceptions with user-friendly messages
- Database connection failures
- Missing or null settings (falls back to defaults)

## Best Practices

1. **SLA Configuration**
   - Set realistic time limits based on your organization's capacity
   - Generally: High > Medium > Low priority
   - Consider business hours and staffing

2. **Escalation Levels**
   - Map to your organizational hierarchy
   - Ensure each level has assigned users in database
   - Test escalation paths before going live

3. **Notifications**
   - Enable notifications for business-critical issues
   - Consider email load when enabling for all levels
   - Monitor notification delivery

4. **Auto Escalation**
   - Enable for mandatory SLAs
   - Disable for manual-only workflows
   - Monitor escalation job logs

5. **Backup**
   - Keep database backups before major setting changes
   - Test configuration changes in non-production first
   - Document your escalation policies

## Monitoring

Monitor escalation in logs:
```
⏰ Escalation Job Running - [timestamp]
✓ Escalated complaint #[id]
✓ Escalation Job Completed - [count] complaints escalated
```

## Troubleshooting

**Complaints Not Escalating:**
1. Check `auto_escalation_enabled` is true
2. Verify `last_status_change` timestamp is correct
3. Ensure `current_level < max_escalation_level`
4. Check escalation_settings table has data

**Escalation Job Not Running:**
1. Verify EscalationScheduler is registered in web.xml
2. Check server logs for listener initialization
3. Ensure database connection is working

**Settings Not Saving:**
1. Verify user has ADMIN role
2. Check input validation errors
3. Review database permissions

## Future Enhancements

Potential improvements:
- Category-specific escalation rules
- Time-based escalation (business hours aware)
- Multiple active configurations with routing logic
- Email template customization for escalation notifications
- Escalation analytics and reporting dashboard
- Historical configuration tracking and rollback

## File Summary

| File | Purpose | Type |
|------|---------|------|
| EscalationSettings.java | Model for escalation data | Model |
| EscalationSettingsDAO.java | Database operations | DAO |
| EscalationService.java | Business logic | Service |
| AdminEscalationSettingsServlet.java | Request handling | Servlet |
| adminEscalationSettings.jsp | Admin UI | View |
| EscalationJob.java | Scheduled processing | Job |
| data.sql | Database schema | SQL |
| web.xml | Servlet configuration | Config |

## Support & Maintenance

- Monitor escalation job logs regularly
- Test configuration changes before deployment
- Keep audit trail of setting modifications
- Regularly review SLA compliance metrics
- Update settings based on performance data
