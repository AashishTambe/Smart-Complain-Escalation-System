# Dynamic Escalation Settings - Quick Start Guide

## 🎯 What's New?

Your complaint system now has **dynamic escalation configuration**. Admins can change escalation rules without touching code!

## 📁 Files Created/Modified

### New Files Added:
1. **Model**: `EscalationSettings.java` - Data model for escalation configuration
2. **DAO**: `EscalationSettingsDAO.java` - Database operations
3. **Service**: `EscalationService.java` - Escalation business logic
4. *(Admin UI not included in current codebase)*: Escalation settings are configured via the MySQL table `escalation_settings`

### Files Modified:
1. **EscalationJob.java** - Now uses EscalationService for dynamic settings
2. **data.sql** - Added/updated `escalation_settings` table with default data
3. *(Scheduler already wired)*: `EscalationScheduler` runs `EscalationJob` periodically

## 🚀 Quick Start (5 Minutes)

### 1. Database Setup
```bash
# Run the updated data.sql to create the escalation_settings table
mysql -u root -p < data.sql
```

### 2. Verify Table Creation
```sql
mysql> USE complaint_system;
mysql> DESC escalation_settings;
mysql> SELECT * FROM escalation_settings;
```

You should see default settings with id=1.

### 3. Build & Deploy
```bash
# In your project directory
mvn clean install
# Deploy the WAR file to Tomcat
```

### 4. Access Admin Panel
In this current build, escalation settings are updated directly in the database (there is no admin JSP/servlet UI wired).

1. Login to MySQL
2. Select the DB and view current settings:
```sql
USE complaint_system;
SELECT * FROM escalation_settings;
```

To edit the default configuration (usually `id=1`), run update queries like:
```sql
UPDATE escalation_settings
SET high_priority_sla_hours = 4,
    auto_escalation_enabled = 1,
    notify_on_escalation = 1
WHERE id = 1;
```

## 💡 How It Works

### Before (Old Way - Hardcoded):
```
EscalationJob → Hard-coded values → Escalate complaint
                   (24hr, 12hr, 6hr)
```

### After (New Way - Dynamic):
```
EscalationJob → EscalationService → Read from Database → Escalate complaint
                                    (configurable SLA)
```

## 🎛️ Dynamic Settings (How to Update)

### View Settings
```sql
SELECT * FROM escalation_settings;
```

### Edit Settings (example)
```sql
UPDATE escalation_settings
SET max_escalation_level = 3,
    low_priority_sla_hours = 24,
    medium_priority_sla_hours = 12,
    high_priority_sla_hours = 6,
    auto_escalation_enabled = 1,
    notify_on_escalation = 1
WHERE id = 1;
```

## ⚙️ Configuration Options

When creating/editing settings, you configure:

| Setting | Purpose | Example |
|---------|---------|---------|
| Setting Name | Descriptive label | "Default", "High Priority" |
| Max Escalation Level | Highest escalation tier (1-5) | 3 |
| Low Priority SLA | Hours before escalating LOW complaints | 24 |
| Medium Priority SLA | Hours before escalating MEDIUM complaints | 12 |
| High Priority SLA | Hours before escalating HIGH complaints | 6 |
| Auto Escalation | Enable/disable automatic escalation | ✓ Checkbox |
| Notifications | Send alerts on escalation | ✓ Checkbox |

## 🔄 Escalation Flow

1. **Job Runs** (scheduled in background)
2. **Check Each Complaint**
   - Is auto escalation enabled? → Continue
   - Age > SLA for this priority? → Continue
   - Level < max level? → Escalate
3. **Escalate Complaint**
   - Increase level
   - Find new assignee
   - Update status to ESCALATED
   - Create notification (if enabled)
   - Record history

## 📊 Example Configurations

### Default Config (Standard)
```
Max Level: 3
LOW SLA: 24 hours
MEDIUM SLA: 12 hours
HIGH SLA: 6 hours
Auto Escalation: ON
Notifications: ON
```

### Strict Config (Fast Escalation)
```
Max Level: 2
LOW SLA: 12 hours
MEDIUM SLA: 4 hours
HIGH SLA: 2 hours
Auto Escalation: ON
Notifications: ON
```

### Manual Config (No Auto Escalation)
```
Max Level: 3
LOW SLA: 48 hours
MEDIUM SLA: 36 hours
HIGH SLA: 24 hours
Auto Escalation: OFF
Notifications: ON
```

## 🔒 Security

- **Admin Only**: Only users with role = "ADMIN" can access settings
- **Authentication**: Must be logged in to access
- **Validation**: Input values are validated (numbers must be > 0)
- **Protection**: Default settings (id=1) cannot be deleted

## 🧪 Testing

### Create a Test Complaint:
1. Login as regular user
2. File a complaint with HIGH priority
3. Get the complaint ID

### Force Escalation:
```sql
-- Update to simulate old complaint
UPDATE complaints 
SET last_status_change = DATE_SUB(NOW(), INTERVAL 7 HOUR)
WHERE id = [YOUR_COMPLAINT_ID];

-- Wait for job or manually trigger escalation
-- Check if status changed to ESCALATED
SELECT id, status, current_level FROM complaints WHERE id = [YOUR_COMPLAINT_ID];
```

## 📱 Adding Admin Link

In your admin dashboard JSP, add this button/link:

```jsp
<% if("ADMIN".equals(((User)session.getAttribute("user")).getRole())) { %>
    <a href="adminEscalationSettings" class="admin-btn">
        ⚙️ Escalation Settings
    </a>
<% } %>
```

See `ADMIN_NAVIGATION_SNIPPET.jsp` for complete code.

## 🐛 Troubleshooting

### Problem: "Cannot find servlet"
```
→ Check escalation_settings table exists
→ Verify web.xml has servlet mapping
→ Rebuild project with mvn clean install
```

### Problem: "Access Denied"
```
→ Make sure you're logged in as ADMIN user
→ Check User object in session
```

### Problem: "Complaints not escalating"
```
→ Verify auto_escalation_enabled = 1
→ Check last_status_change timestamp
→ Check escalation_settings table has data
→ Review tomcat logs for errors
```

### Problem: "Table doesn't exist"
```
→ Run data.sql again
→ Check that CREATE TABLE for escalation_settings ran
→ Verify with: DESCRIBE escalation_settings;
```

## 📋 Default Data

When you run data.sql, you get one default configuration:

```sql
INSERT INTO escalation_settings 
(setting_name, max_escalation_level, low_priority_sla_hours, 
 medium_priority_sla_hours, high_priority_sla_hours, 
 auto_escalation_enabled, notify_on_escalation)
VALUES 
('Default', 3, 24, 12, 6, 1, 1);
```

This means by default:
- Complaints can escalate up to 3 levels
- LOW: 24 hours before escalation
- MEDIUM: 12 hours before escalation
- HIGH: 6 hours before escalation
- Auto escalation is ON
- Notifications are ON

## ✅ Deployment Checklist

- [ ] Run updated data.sql
- [ ] Build project: `mvn clean install`
- [ ] Deploy WAR file to Tomcat
- [ ] Restart Tomcat
- [ ] Login and test admin panel
- [ ] Create test configuration
- [ ] Edit and save settings
- [ ] Verify escalation job logs
- [ ] Test with sample complaint

## 📚 Documentation

For more details, see:
- `ESCALATION_SETTINGS_IMPLEMENTATION.md` - Full technical docs
- `INTEGRATION_CHECKLIST.md` - Step-by-step integration guide
- This file - Quick reference

## 🎓 Key Concepts

**EscalationSettings**: Configuration model storing SLA and behavior rules

**EscalationSettingsDAO**: Handles database CRUD for settings

**EscalationService**: Core logic that uses settings to decide when to escalate

**AdminEscalationSettingsServlet**: Web controller for managing settings

**adminEscalationSettings.jsp**: User interface for admins

## 💡 Tips

1. **Test in Development First** - Try different SLA values before production
2. **Monitor Escalations** - Check logs to see escalation activity
3. **Adjust Based on Data** - Fine-tune SLAs based on complaint patterns
4. **Document Policies** - Record why you chose specific SLA values
5. **Backup Settings** - Export/backup your configuration before changes

## 🚀 What's Next?

After setting this up:
1. Train admins on using the panel
2. Set SLA times for your organization
3. Configure escalation per-department if needed
4. Monitor escalation effectiveness
5. Adjust SLA times based on metrics
6. Consider future enhancements (analytics, custom rules, etc.)

---

## Quick Reference

| Task | URL |
|------|-----|
| View Settings | `/adminEscalationSettings` |
| Create Setting | Click "+ New Configuration" |
| Edit Setting | Click "Edit" on a card |
| Delete Setting | Click "Delete" on a card |

**Remember**: Only admins can access these features!

---
**Version**: 1.0  
**Last Updated**: 2026-03-07  
**Status**: Ready to Deploy
