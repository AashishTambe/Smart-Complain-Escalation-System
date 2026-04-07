# Smart Complaint Escalation System
## Project Report (Chapter-wise)

Submitted in partial fulfilment of the requirement of the degree of  
`B.S.C. (Computer Science)` (or equivalent)  

Submitted by: `__________________________`  
Submitted to: `__________________________`  
Under the Guidance of: `__________________________`  

Academic Year: `2025-2026`

---

## Index
1. ACKNOWLEDGEMENT  
2. ABSTRACT  
3. Chapter 1: Introduction  
   1.1 Project Overview  
   1.2 Problem Statement  
   1.3 Objectives of the System  
   1.4 Scope of the Project  
   1.5 Features of the System  
   1.6 Technology Overview  
4. Chapter 2: System Requirements & Architecture  
   2.1 Hardware Requirements  
   2.2 Software Requirements  
   2.3 System Architecture  
   2.3.1 Overall Architecture Overview  
   2.3.2 Frontend–Backend Interaction Flow  
   2.3.3 Escalation Processing Flow  
   2.4 Data Flow Diagram (DFD)  
   2.5 Use Case Diagram  
   2.6 System Workflow  
5. Chapter 3: System Design  
   3.1 Frontend Design  
   3.2 Backend Design  
   3.3 Database Design  
   3.4 Security Design  
6. Chapter 4: Module Description  
   4.1 User Authentication Module  
   4.2 Complaint Management Module  
   4.3 Status Update & History Module  
   4.4 Escalation Engine Module  
   4.5 Notification Module  
   4.6 Dashboards (Admin/Staff/User)  
7. Chapter 5: Implementation  
   5.1 Frontend Implementation (Servlets + JSP)  
   5.2 Backend Implementation (JDBC + Servlets)  
   5.3 Escalation Scheduling & Dynamic SLA Logic  
   5.4 Notification Handling  
   5.5 Database Integration (MySQL + `data.sql`)  
8. Chapter 6: Testing  
   6.1 Unit Testing (Code-level verification)  
   6.2 Integration Testing  
   6.3 UI Testing (Manual)  
   6.4 Test Cases  
   6.5 Results & Observations  
9. Chapter 7: Results & Screenshots (Placeholders)  
10. Chapter 8: Conclusion  
11. References  
12. Appendix (Source structure & schema notes)

---

## ACKNOWLEDGEMENT
We would like to express our sincere gratitude to our project guide and the department for their continuous support and valuable guidance throughout the development of this project.  
We also thank our college/institution for providing the necessary infrastructure to complete this work. Finally, we appreciate the contribution and encouragement of our friends and colleagues during the testing and improvement phases of the system.

---

## ABSTRACT
The Smart Complaint Escalation System is a Java-based web application built using Servlets and JSP to help organizations manage complaints in a structured manner and automatically escalate them when resolution does not happen within a defined SLA (Service Level Agreement).  

The system allows users to register complaints with priority and category. Depending on priority and departmental assignment, an automated escalation engine periodically checks complaints and escalates them to the next level when the SLA time is exceeded. During escalation, the system updates the complaint status, increments the escalation level, assigns the complaint to the next eligible staff member (based on role mapping per department and level), records the escalation in complaint history, and generates notifications for all relevant users.  

Notifications are displayed to users through a dedicated notifications page and supported by an unread notification badge in the header. Administrators and staff can track and update complaint status through role-based dashboards. The overall design follows a layered architecture (presentation → application → data) and uses JDBC for MySQL connectivity.

---

## Chapter 1: Introduction

### 1.1 Project Overview
In many organizations, complaint handling is time-sensitive and requires coordination across multiple departments and staff levels. When complaints remain unresolved beyond acceptable time limits, the burden increases and the response quality can deteriorate.  

To address this, the Smart Complaint Escalation System provides a centralized platform where complaints are submitted, tracked, and automatically escalated according to configurable SLA rules stored in a database. This reduces manual monitoring and helps ensure that higher-priority complaints receive faster attention.

### 1.2 Problem Statement
The key challenges in complaint handling are:
- Complaints may not be escalated promptly when they exceed resolution deadlines.
- Users may not receive timely updates about escalation and status changes.
- Manual tracking across multiple staff and departments is error-prone and time-consuming.
- Organizations require an approach that is consistent, auditable (history of changes), and secure for different user roles.

### 1.3 Objectives of the System
The main objectives are:
- Provide a web-based interface for complaint registration and tracking.
- Support priority-based escalation using SLA limits.
- Automate escalation with a background scheduler (periodic checks).
- Notify the complaint creator and relevant assigned staff during escalation and status updates.
- Maintain a history/audit trail of complaint status changes and escalation levels.

### 1.4 Scope of the Project
The system focuses on:
- User login/logout and complaint registration.
- Role-based dashboards for users, staff, and administrators.
- Status updates with notifications.
- Automated SLA-based escalation using dynamic configuration from MySQL.

Out of scope for the current build:
- Fully automated email delivery (notifications are stored and viewed in-app).
- Advanced admin UI for editing escalation settings (settings are currently read from database; updates can be done via SQL or future UI).

### 1.5 Features of the System
- Authentication using Servlet session (`userId`, `userRole`, `userName`).
- Complaint creation with category, priority, and description.
- Department-based assignee selection during escalation.
- SLA-based escalation engine (checks and escalates periodically).
- Complaint history logging (`complaint_history` table).
- Notifications system (create, list, mark read).
- Role-based UI navigation and dashboards.

### 1.6 Technology Overview
The project uses:
- Java (Servlets + JSP)
- Apache Tomcat 9
- Maven (WAR build)
- MySQL (JDBC connectivity)
- JSTL (JSP standard tag library)

---

## Chapter 2: System Requirements & Architecture

### 2.1 Hardware Requirements
For development and deployment in a typical lab environment:
- Processor: Intel i3 or higher
- RAM: 4 GB minimum (8 GB recommended)
- Storage: At least 10–20 GB free space
- Internet connection (only required if pulling dependencies or using remote tools)

### 2.2 Software Requirements
- Operating System: Windows 10/11 or equivalent
- Java Development Kit: Java 11
- Application Server: Apache Tomcat 9
- Database: MySQL
- Build Tool: Maven
- IDE: Eclipse / IntelliJ IDEA / VS Code / Cursor IDE (any works)

### 2.3 System Architecture

#### 2.3.1 Overall Architecture Overview
The architecture is a three-layer design:
1. Presentation Layer
   - JSP pages for UI rendering
   - Servlets for request routing and session handling
2. Application Layer
   - Business logic services, especially `EscalationService`
   - Background escalation runner: `EscalationJob`
   - Scheduler: `EscalationScheduler` (runs periodically)
3. Data Layer
   - JDBC operations via DAOs
   - MySQL schema defined in `src/main/resources/data.sql`

In addition, an in-app notification flow connects the service layer to user-facing JSP pages.

#### 2.3.2 Frontend–Backend Interaction Flow
1. User opens JSP pages (login/register/dashboard/complaint pages).
2. User submits forms (login credentials or complaint registration).
3. Servlets validate input and use DAOs to read/update MySQL.
4. Response JSP renders updated information.
5. Session attributes are used for role-based navigation and access control.

#### 2.3.3 Escalation Processing Flow
1. `EscalationScheduler` starts when the web application is initialized.
2. The scheduler runs `EscalationJob` periodically (initial delay then fixed-rate execution).
3. `EscalationJob` queries complaints that may be escalated.
4. For each complaint, `EscalationService.shouldEscalate(complaintId)`:
   - Loads active `escalation_settings`
   - Computes hours since `last_status_change`
   - Checks SLA exceeded and `current_level` below max level
5. If escalation is needed, `EscalationService.escalateComplaint(complaintId)`:
   - Updates complaint status to `ESCALATED`
   - Sets `assigned_to` for the next-level role (department + level based)
   - Updates `last_status_change`
   - Inserts a record into `complaint_history`
   - Creates notifications (if enabled in settings)

#### 2.3.4 Escalation Decision Logic (Summary)
Escalation occurs when:
- `auto_escalation_enabled` is enabled in `escalation_settings`
- `hoursSince(last_status_change) > slaHours(priority)`
- `current_level < max_escalation_level`

### 2.4 Data Flow Diagram (DFD)
Text DFD (Level 1):
```
User/Admin/Staff
  |
  | (HTTP requests: login, register, registerComplaint, updateStatus, view notifications)
  v
JSP + Servlets (Controllers)
  |
  | (JDBC via DAOs)
  v
MySQL Database
  - complaints
  - escalation_settings
  - complaint_history
  - notifications

Background Scheduler Thread
  |
  v
EscalationScheduler -> EscalationJob -> EscalationService -> DAOs -> Database updates
```

### 2.5 Use Case Diagram
Key actors and use cases:
- User: Register/Login, Register Complaint, View My Complaints, View Notifications
- Staff: View Assigned Complaints, Update Status
- Admin: View All Complaints, Update Status
- System (Automated): Periodic Escalation and Notification Generation

Example Mermaid use-case (optional):
```mermaid
usecaseDiagram
  actor User
  actor Staff
  actor Admin
  actor System

  User --> (Register / Login)
  User --> (Register Complaint)
  User --> (View My Complaints)
  User --> (View Notifications)

  Staff --> (View Assigned Complaints)
  Staff --> (Update Complaint Status)

  Admin --> (View All Complaints)
  Admin --> (Update Complaint Status)

  System --> (Auto Escalate by SLA)
  System --> (Create Notifications)
```

### 2.6 System Workflow
1. Login (user/staff/admin)
2. For users: submit complaint
3. System assigns complaint to level 1 assignee (department-based role mapping)
4. Staff/admin updates complaint status (notifications created)
5. Scheduler runs periodically and escalates if SLA exceeded
6. Notifications appear in user “Notifications” page with unread badge support

---

## Chapter 3: System Design

### 3.1 Frontend Design
The UI uses JSP pages styled by static CSS and a shared `header.jsp`.
Key UI responsibilities:
- Render role-based navigation (based on `userRole`)
- Show unread notification count (via `NotificationDAO.getUnreadCount(userId)`)
- Display dashboards and lists for complaints by role
- Provide forms:
  - login (handled by `LoginServlet`)
  - registration (handled by `RegisterServlet`)
  - complaint submission (handled by `ComplaintRegistrationServlet`)

Navigation uses servlet mappings defined in `web.xml` and direct JSP routes.

### 3.2 Backend Design
Main backend components:
- Servlets (Controllers)
  - `LoginServlet`: validates login and creates session attributes
  - `LogoutServlet`: invalidates session
  - `ComplaintRegistrationServlet`: creates complaint and initial notification for assignee
  - `StatusUpdateServlet`: role-based status updates and notifications
  - `NotificationServlet`: lists notifications and marks as read
  - `StaffDashboardServlet` / `MyComplaintsServlet`: list complaints (by assignment or by creator)
  - `AdminDashboardServlet`: currently redirects to staff dashboard (legacy support)
- Service Layer
  - `EscalationService`: SLA logic and escalation updates
- Job + Scheduler
  - `EscalationScheduler`: timer that runs periodically
  - `EscalationJob`: queries candidate complaints and triggers service escalation
- DAO Layer
  - DAOs handle JDBC reads/writes for `complaints`, `users`, `notifications`, and `escalation_settings`

### 3.3 Database Design
The database schema (MySQL) is created by `data.sql` and includes:
- `departments` (IT, HR, Maintenance)
- `users` (stores staff roles and department mapping)
- `complaint_categories` (belongs to department)
- `complaints`
  - `priority` (LOW/MEDIUM/HIGH)
  - `status` (OPEN/IN_PROGRESS/ESCALATED/RESOLVED/CLOSED)
  - `current_level` (escalation level)
  - `assigned_to` (next responsible user)
  - `last_status_change`
- `escalation_settings` (dynamic SLA settings used by escalation engine)
  - `low_priority_sla_hours`, `medium_priority_sla_hours`, `high_priority_sla_hours`
  - `max_escalation_level`
  - `auto_escalation_enabled`, `notify_on_escalation`
- `complaint_history` (audit trail for status transitions and escalation events)
- `notifications` (in-app notifications for user recipients)

Role mapping for assignees:
- Department (`deptId`) + escalation `level` maps to specific user roles (e.g., IT_JR_DEV, IT_SR_DEV, IT_HEAD).

### 3.4 Security Design
Current security approach:
- Authentication using session-based access control:
  - `LoginServlet` stores `userId`, `userRole`, `userName`
  - Servlets validate presence of session attributes before processing
- Authorization:
  - `StatusUpdateServlet` allows status updates based on role:
    - `ADMIN` can update any complaint
    - staff can update only complaints assigned to them
- Input handling:
  - DAO layer uses prepared statements for SQL parameter binding
- Session timeout:
  - session max inactive interval is set in `LoginServlet`

Note (design consideration):
- Password hashing is not implemented in this version (login checks compare stored password directly).
- For production-grade security, passwords should be hashed (e.g., bcrypt) and never stored in plaintext.

---

## Chapter 4: Module Description

### 4.1 User Authentication Module
- `RegisterServlet`:
  - collects name/email/password/phone
  - creates a `USER` role by default
- `LoginServlet`:
  - validates identifier (email/phone) and password
  - creates session attributes
  - redirects:
    - `USER` → `myComplaints`
    - staff/admin → `staffDashboard`
- `LogoutServlet`:
  - invalidates session

### 4.2 Complaint Management Module
Complaint submission is handled by:
- `ComplaintRegistrationServlet`
  - reads category and priority
  - derives `department_id` from category
  - assigns complaint to a level 1 staff based on department role mapping
  - creates complaint record
  - creates an initial notification for the assigned staff

Complaint viewing is role-based:
- users see their own complaints
- staff sees complaints assigned to them
- admin views all complaints (via staff dashboard logic in this build)

### 4.3 Status Update & History Module
Handled by:
- `StatusUpdateServlet`
  - `ADMIN`: updates status for any complaint
  - staff: updates status only if complaint is assigned to the logged-in staff
  - creates notifications to:
    - complaint creator (`user_id`)
    - previously assigned staff (`assigned_to`) during admin updates

Escalation history is recorded by:
- `EscalationService` inserts into `complaint_history` whenever a complaint is escalated.

### 4.4 Escalation Engine Module
Components:
- `EscalationScheduler`
  - initializes a `Timer`
  - first run after a short delay
  - repeats every fixed period
- `EscalationJob`
  - queries all complaints in states likely to be escalated
  - calls `EscalationService.shouldEscalate()`
  - if true, calls `EscalationService.escalateComplaint()`
- `EscalationService`
  - loads SLA settings using `EscalationSettingsDAO.getActiveSettings()`
  - calculates time elapsed since last status change
  - decides escalation based on SLA and max escalation level
  - escalates complaint:
    - updates `current_level`, `status`, `assigned_to`, and `last_status_change`
    - records history
    - creates notifications for creator, previous assignee, and new assignee (when enabled)

Dynamic SLA configuration:
- SLA values are read from `escalation_settings` table.
- This version currently reads settings; editing settings is not exposed via an admin UI in the codebase.

### 4.5 Notification Module
Components:
- `EscalationService` creates escalation notifications (if enabled).
- `ComplaintRegistrationServlet` creates initial notification for assigned staff.
- `StatusUpdateServlet` creates notifications on status updates.
- `NotificationDAO` provides:
  - list notifications for recipient
  - mark notification as read (`sent=1`, `sent_at=NOW()`)
  - unread count for header badge
- `NotificationServlet` forwards notifications to `notifications.jsp`.

### 4.6 Dashboards (Admin/Staff/User)
- `staffDashboard`:
  - shows complaints filtered for staff assignment or admin views
- `myComplaints`:
  - shows complaints created by the logged-in user
- `notifications`:
  - shows in-app notification feed and supports mark-as-read actions

---

## Chapter 5: Implementation

### 5.1 Frontend Implementation (Servlets + JSP)
Frontend is implemented with:
- JSP pages under `src/main/webapp`
- shared `header.jsp` with:
  - role-based navigation
  - unread notification badge
- CSS assets served from `/css/styles.css`

Request-response flow:
- JSP pages render server-side values from session and DAO queries.

### 5.2 Backend Implementation (JDBC + Servlets)
Backend is implemented using:
- Servlet classes extending `HttpServlet`
- DAO classes using `DBConnection.getConnection()` for MySQL JDBC connections
- Prepared statements to avoid SQL injection for parameters

Session management:
- Login creates `userId`, `userName`, `userRole`
- Servlets read session to enforce access control

### 5.3 Escalation Scheduling & Dynamic SLA Logic
Scheduling:
- implemented in `EscalationScheduler` as a Java `Timer` task
Escalation logic:
- implemented in `EscalationJob` and `EscalationService`

Dynamic SLA:
- `EscalationSettingsDAO` reads `escalation_settings` (active setting with `id=1`)
- `EscalationService` uses priority-specific SLA hours

### 5.4 Notification Handling
Notifications are stored in MySQL and displayed in JSP:
- `notifications` table stores:
  - complaint_id, recipient_id, type, message, created_at, sent status
- unread badge:
  - `header.jsp` calls `NotificationDAO.getUnreadCount(userId)`

### 5.5 Database Integration (MySQL + `data.sql`)
All schema and default records are created using:
- `src/main/resources/data.sql`

The file initializes:
- departments, users, complaint categories
- default escalation settings record
- escalation rules (legacy)

---

## Chapter 6: Testing

Testing approach:
- The project does not include automated unit-test suites in the repository (`src/test` is not present).
- Testing is performed as manual integration testing using Tomcat + MySQL.

### 6.1 Unit Testing (Code-level verification)
Manual verification methods:
- Validate DAO SQL queries:
  - run SELECT queries to confirm row mapping
- Validate escalation logic:
  - adjust `last_status_change` values in DB to simulate SLA expiration

### 6.2 Integration Testing
Integration verifies that:
- `EscalationScheduler` triggers `EscalationJob`
- `EscalationJob` escalates complaints by SLA
- Escalation updates propagate to:
  - `complaints`
  - `complaint_history`
  - `notifications`

### 6.3 UI Testing (Manual)
UI checks:
- role-based navigation rendering
- complaint submission form validation
- notifications list and mark-as-read behavior
- status update flows for admin/staff

### 6.4 Test Cases
Example functional test cases:
- TC01 Login with valid user:
  - Input: correct email/phone and password
  - Expected: session created and redirected to correct dashboard
- TC02 Register complaint:
  - Input: category, title, description, priority
  - Expected: complaint created with `status=OPEN` and assigned staff notification generated
- TC03 Escalation triggers:
  - Input: set `last_status_change` older than SLA and ensure `auto_escalation_enabled=1`
  - Expected: status becomes `ESCALATED`, `current_level` increments, history and notifications created
- TC04 Max level reached:
  - Input: `current_level == max_escalation_level`
  - Expected: no further escalation
- TC05 Status update notifications:
  - Input: admin updates status
  - Expected: notifications created for complaint creator and assigned staff
- TC06 Mark notification as read:
  - Input: click mark-read action
  - Expected: unread badge count decreases

### 6.5 Results & Observations
Based on the implemented logic, the system produces:
- Periodic escalation of complaints when SLA is exceeded
- Notifications for all stakeholders when escalation occurs (creator, previous assignee, new assignee)
- Notifications for admin/staff status updates
- Auditability via `complaint_history`

---

## Chapter 7: Results & Screenshots (Placeholders)
The following screenshots should be inserted from the running application:
- Login Page
- Registration Page
- Complaint Registration Page
- User “My Complaints” Page
- Staff/Administration Dashboard Page
- Notifications Page (with unread badge)
- Escalation Flow Demonstration (before/after status change)

---

## Chapter 8: Conclusion
The Smart Complaint Escalation System successfully addresses the need for structured complaint management and time-bound escalation. By combining role-based dashboards, complaint lifecycle tracking, and a periodic escalation engine driven by SLA settings stored in MySQL, the system reduces manual monitoring and improves responsiveness.  

The escalation mechanism is implemented in a modular manner using `EscalationScheduler`, `EscalationJob`, and `EscalationService`, while the notification workflow provides visibility to users through in-app messages and unread indicators. The use of complaint history ensures traceability of status changes and escalation events.  

Future enhancements can include:
- Admin UI for editing escalation settings (CRUD on `escalation_settings`)
- Password hashing with bcrypt and stronger authentication
- Email/SMS notification channels
- Additional analytics dashboards for SLA compliance
- Category/department-specific SLA routing

---

## References
- Java Servlet Specification (Servlet API)
- Apache Tomcat 9 Documentation
- JSTL (JSP Standard Tag Library)
- MySQL JDBC Connector Documentation
- Java Timer / Scheduling concepts (Servlet context listeners)

---

## Appendix

### Appendix A: Key Source Structure
- `src/main/java/com/complaintsystem/config/DBConnection.java`  
  Database connection helper (JDBC).
- `src/main/java/com/complaintsystem/servlet/*`  
  Controllers for login, complaint registration, dashboards, status updates, and notifications.
- `src/main/java/com/complaintsystem/service/EscalationService.java`  
  SLA logic + escalation updates + notification creation.
- `src/main/java/com/complaintsystem/job/EscalationJob.java`  
  Escalation runner invoked periodically.
- `src/main/java/com/complaintsystem/listener/EscalationScheduler.java`  
  Starts the timer-based scheduler when context initializes.
- `src/main/java/com/complaintsystem/dao/*`  
  JDBC data access layer.

### Appendix B: Database Configuration Notes
- `DBConnection` currently uses:
  - JDBC URL: `jdbc:mysql://localhost:3306/complaint_system`
  - User: `root`
  - Password: `root` (edit for your environment)
- Use `src/main/resources/data.sql` to initialize schema and default data.

