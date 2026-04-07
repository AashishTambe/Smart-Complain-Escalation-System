# Dynamic Escalation Settings - Integration Checklist

## ✅ Database Setup
- [x] Create `escalation_settings` table
- [x] Add default escalation settings record
- [x] Run `data.sql` to update database schema
- [x] Verify table structure with: `DESC escalation_settings;`

## ✅ Java Classes Created
- [x] `EscalationSettings.java` (Model)
- [x] `EscalationSettingsDAO.java` (Data Access Layer)
- [x] `EscalationService.java` (Business Logic)
 
*(AdminEscalationSettingsServlet is not present/wired in this repository. Settings are updated via `escalation_settings` in MySQL.)*

## ✅ JSP Pages Created
 
*(adminEscalationSettings.jsp is not present/wired in this repository. Settings are updated via `escalation_settings` in MySQL.)*

## ✅ Configuration Updates
- [x] Updated `EscalationJob.java` to use EscalationService
- [x] Updated `data.sql` with new table and default data

## ✅ Project Structure
```
src/main/java/com/complaintsystem/
├── model/
│   └── EscalationSettings.java ✓
├── dao/
│   └── EscalationSettingsDAO.java ✓
├── service/
│   └── EscalationService.java ✓
├── job/
│   └── EscalationJob.java (Updated) ✓
└── ...

src/main/webapp/
└── ...

src/main/resources/
└── data.sql (Updated) ✓

Documentation/
├── ESCALATION_SETTINGS_IMPLEMENTATION.md ✓
├── INTEGRATION_CHECKLIST.md (This file) ✓
└── ADMIN_NAVIGATION_SNIPPET.jsp ✓
```

## 📋 Testing Steps

### Step 1: Build & Deploy
```bash
# Build the project
mvn clean install

# Deploy to Tomcat server
```

### Step 2: Database Verification
```sql
-- Connect to MySQL
mysql -u root -p

-- Check escalation_settings table
USE complaint_system;
SELECT * FROM escalation_settings;

-- Expected output: Default settings with id=1
```

### Step 3: Update Escalation Settings (DB)
1. Connect to MySQL
2. View current active settings:
```sql
USE complaint_system;
SELECT * FROM escalation_settings;
```
3. Update the default row (commonly `id=1`) to test fast escalation:
```sql
UPDATE escalation_settings
SET high_priority_sla_hours = 1,
    auto_escalation_enabled = 1,
    notify_on_escalation = 1
WHERE id = 1;
```
4. Confirm the change:
```sql
SELECT * FROM escalation_settings WHERE id = 1;
```

### Step 5: Test Automatic Escalation
1. Login as regular user
2. Create a new complaint with HIGH priority
3. Set `last_status_change` to more than 6 hours ago (via SQL):
   ```sql
   UPDATE complaints 
   SET last_status_change = DATE_SUB(NOW(), INTERVAL 7 HOUR) 
   WHERE id = [complaint_id];
   ```
4. Wait for escalation job to run (or manually trigger if possible)
5. Check if complaint status changed to 'ESCALATED'
6. Verify notification was created

## 🔍 Verification Queries

### Check Settings Created
```sql
SELECT * FROM escalation_settings;
```

### Check Escalation History
```sql
SELECT c.id, c.status, c.current_level, c.priority, 
       c.last_status_change, es.*
FROM complaints c
CROSS JOIN escalation_settings es
WHERE es.id = 1
ORDER BY c.id DESC;
```

### Check Notifications Created
```sql
SELECT n.*, c.id as complaint_id
FROM notifications n
JOIN complaints c ON n.complaint_id = c.id
WHERE n.message LIKE '%escalated%'
ORDER BY n.created_at DESC;
```

## 🐛 Common Issues & Solutions

### Issue: "Settings not found" error
**Solution:** 
- Verify data.sql was run completely
- Check escalation_settings table exists: `SHOW TABLES;`
- Insert default record manually:
  ```sql
  INSERT INTO escalation_settings 
  (setting_name, max_escalation_level, low_priority_sla_hours, 
   medium_priority_sla_hours, high_priority_sla_hours)
  VALUES ('Default', 3, 24, 12, 6);
  ```

### Issue: Escalation settings not found / not applied
**Solution:**
- Verify `data.sql` was run completely
- Check escalation_settings table exists: `SHOW TABLES;`
- Ensure there is a default/active row (commonly `id=1`)

### Issue: Complaints not escalating automatically
**Solution:**
- Verify `auto_escalation_enabled = 1` in escalation_settings
- Check `last_status_change` timestamp is old enough
- Check `current_level < max_escalation_level`
- Verify escalation_settings table has records
- Check server logs for escalation job errors

### Issue: "Access Denied" when editing settings
**Solution:**
*(Not applicable in the current codebase because settings are edited via SQL/MySQL, not via an admin UI.)*

## 📝 Configuration Notes

### Default Settings
The system comes with default escalation settings (id=1):
- Max Level: 3
- LOW SLA: 24 hours
- MEDIUM SLA: 12 hours
- HIGH SLA: 6 hours
- Auto Escalation: Enabled
- Notifications: Enabled

### Modifying Default Settings
To use different default behavior:
1. Update directly in database using SQL:
   ```sql
   UPDATE escalation_settings 
   SET max_escalation_level = 4,
       high_priority_sla_hours = 4
   WHERE id = 1;
   ```

### Multiple Configurations
You can create multiple configurations but only one (id=1) is active.
To switch configurations:
1. Back up current settings
2. Update Default record with new values
3. Or restructure database to support multiple active configs

## 📚 Documentation Files

1. **ESCALATION_SETTINGS_IMPLEMENTATION.md** - Comprehensive technical documentation
2. **INTEGRATION_CHECKLIST.md** - This file, step-by-step integration guide
3. **ADMIN_NAVIGATION_SNIPPET.jsp** - Code sample for adding admin menu link

## 🚀 Next Steps

- [ ] Test all functionality in development environment
- [ ] Update staffDashboard.jsp with link to escalation settings
- [ ] Configure escalation job scheduler interval
- [ ] Set up monitoring/alerts for escalation failures
- [ ] Document SLA policies for your organization
- [ ] Train admins on using the settings panel
- [ ] Deploy to production
- [ ] Monitor and adjust SLA times based on metrics

## 📞 Support

For issues or questions:
1. Check logs: `catalina.out` in Tomcat
2. Review error messages in admin panel
3. Verify database connectivity
4. Check that all files are properly deployed
5. Ensure no compilation errors during build

---
**Last Updated:** 2026-03-07
**System Version:** 1.0
**Status:** Ready for Production
