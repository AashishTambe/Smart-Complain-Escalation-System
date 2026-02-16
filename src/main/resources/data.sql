CREATE DATABASE complaint_system;
USE complaint_system;
CREATE TABLE departments (id INT AUTO_INCREMENT PRIMARY KEY,name VARCHAR(100) NOT NULL UNIQUE);
CREATE TABLE users (id INT AUTO_INCREMENT PRIMARY KEY,name VARCHAR(100) NOT NULL,email VARCHAR(150) NOT NULL UNIQUE,password VARCHAR(255) NOT NULL,role VARCHAR(50) NOT NULL,department_id INT NULL,active TINYINT(1) DEFAULT 1,FOREIGN KEY (department_id) REFERENCES departments(id));
CREATE TABLE complaint_categories (id INT AUTO_INCREMENT PRIMARY KEY,name VARCHAR(100) NOT NULL UNIQUE,department_id INT NOT NULL,FOREIGN KEY (department_id) REFERENCES departments(id));
CREATE TABLE complaints (id INT AUTO_INCREMENT PRIMARY KEY,user_id INT NOT NULL,category_id INT NOT NULL,title VARCHAR(200) NOT NULL,description TEXT NOT NULL,priority ENUM('LOW','MEDIUM','HIGH') NOT NULL,status ENUM('OPEN','IN_PROGRESS','ESCALATED','RESOLVED','CLOSED') DEFAULT 'OPEN',current_level INT NOT NULL DEFAULT 1,assigned_to INT NULL,created_at DATETIME DEFAULT CURRENT_TIMESTAMP,last_status_change DATETIME DEFAULT CURRENT_TIMESTAMP,FOREIGN KEY (user_id) REFERENCES users(id),FOREIGN KEY (category_id) REFERENCES complaint_categories(id),FOREIGN KEY (assigned_to) REFERENCES users(id));
CREATE TABLE escalation_rules (id INT AUTO_INCREMENT PRIMARY KEY,category_id INT NULL,priority ENUM('LOW','MEDIUM','HIGH') NOT NULL,level INT NOT NULL,sla_hours INT NOT NULL,UNIQUE (category_id, priority, level),FOREIGN KEY (category_id) REFERENCES complaint_categories(id));
CREATE TABLE complaint_history (id INT AUTO_INCREMENT PRIMARY KEY,complaint_id INT NOT NULL,old_status ENUM('OPEN','IN_PROGRESS','ESCALATED','RESOLVED','CLOSED'),new_status ENUM('OPEN','IN_PROGRESS','ESCALATED','RESOLVED','CLOSED'),old_level INT,new_level INT,changed_by INT NULL,comment VARCHAR(255),changed_at DATETIME DEFAULT CURRENT_TIMESTAMP,FOREIGN KEY (complaint_id) REFERENCES complaints(id),FOREIGN KEY (changed_by) REFERENCES users(id));
CREATE TABLE notifications (id INT AUTO_INCREMENT PRIMARY KEY,complaint_id INT NOT NULL,recipient_id INT NOT NULL,type ENUM('EMAIL','SYSTEM') NOT NULL,message VARCHAR(255) NOT NULL,created_at DATETIME DEFAULT CURRENT_TIMESTAMP,sent TINYINT(1) DEFAULT 0,sent_at DATETIME NULL,FOREIGN KEY (complaint_id) REFERENCES complaints(id),FOREIGN KEY (recipient_id) REFERENCES users(id));

INSERT INTO departments (name) VALUES ('IT'), ('HR'), ('Maintenance');

INSERT INTO complaint_categories (name, department_id) VALUES ('IT Support', 1), ('HR Services', 2), ('Infrastructure', 3);

-- Admin user
INSERT INTO users (name, email, password, role, department_id) VALUES ('Admin', 'admin@test.com', '1234', 'ADMIN', NULL);

-- IT Department users
INSERT INTO users (name, email, password, role, department_id) VALUES ('Rohan Jr Dev', 'jrdev@it.com', '1234', 'IT_JR_DEV', 1);
INSERT INTO users (name, email, password, role, department_id) VALUES ('Sahil Sr Dev', 'srdev@it.com', '1234', 'IT_SR_DEV', 1);
INSERT INTO users (name, email, password, role, department_id) VALUES ('Vikas IT Head', 'ithead@it.com', '1234', 'IT_HEAD', 1);

-- HR Department users
INSERT INTO users (name, email, password, role, department_id) VALUES ('Ashok HR Employee', 'hremp@hr.com', '1234', 'HR_EMPLOYEE', 2);
INSERT INTO users (name, email, password, role, department_id) VALUES ('Shivam HR Manager', 'hrmgr@hr.com', '1234', 'HR_MANAGER', 2);
INSERT INTO users (name, email, password, role, department_id) VALUES ('Dhruv HR Head', 'hrhead@hr.com', '1234', 'HR_HEAD', 2);

-- Maintenance Department users
INSERT INTO users (name, email, password, role, department_id) VALUES ('Om Worker', 'worker@maint.com', '1234', 'MAINT_WORKER', 3);
INSERT INTO users (name, email, password, role, department_id) VALUES ('Ram Engineer', 'engineer@maint.com', '1234', 'MAINT_ENGINEER', 3);
INSERT INTO users (name, email, password, role, department_id) VALUES ('Soham Maint Head', 'mainthead@maint.com', '1234', 'MAINT_HEAD', 3);

-- Escalation rules (global, category_id NULL)
INSERT INTO escalation_rules (priority, level, sla_hours) VALUES 
('LOW', 1, 24), ('LOW', 2, 48), ('LOW', 3, 72),
('MEDIUM', 1, 12), ('MEDIUM', 2, 24), ('MEDIUM', 3, 36),
('HIGH', 1, 6), ('HIGH', 2, 12), ('HIGH', 3, 18);

-- Test complaints assigned to staff members
-- Create test user (regular user who filed complaints)
INSERT INTO users (name, email, password, role, department_id) VALUES ('Test User', 'testuser@test.com', '1234', 'USER', NULL);

-- Test complaints for IT department (assigned to John Jr Dev - id 2)
INSERT INTO complaints (user_id, category_id, title, description, priority, status, current_level, assigned_to, created_at, last_status_change) 
VALUES (11, 1, 'Network Connection Issue', 'Unable to connect to company network', 'HIGH', 'OPEN', 1, 2, NOW(), NOW());

INSERT INTO complaints (user_id, category_id, title, description, priority, status, current_level, assigned_to, created_at, last_status_change) 
VALUES (11, 1, 'Printer Not Working', 'Network printer offline in office', 'MEDIUM', 'IN_PROGRESS', 1, 2, NOW(), NOW());

INSERT INTO complaints (user_id, category_id, title, description, priority, status, current_level, assigned_to, created_at, last_status_change) 
VALUES (11, 1, 'Software License Issue', 'Software license expired', 'LOW', 'OPEN', 1, 2, NOW(), NOW());

-- Test complaints for HR department (assigned to Alice HR Employee - id 5)
INSERT INTO complaints (user_id, category_id, title, description, priority, status, current_level, assigned_to, created_at, last_status_change) 
VALUES (11, 2, 'Salary Advance Request', 'Requesting advance on salary', 'MEDIUM', 'OPEN', 1, 5, NOW(), NOW());

INSERT INTO complaints (user_id, category_id, title, description, priority, status, current_level, assigned_to, created_at, last_status_change) 
VALUES (11, 2, 'Leave Approval Pending', 'Annual leave request pending approval', 'LOW', 'IN_PROGRESS', 1, 5, NOW(), NOW());

-- Test complaints for Maintenance department (assigned to Eve Worker - id 8)
INSERT INTO complaints (user_id, category_id, title, description, priority, status, current_level, assigned_to, created_at, last_status_change) 
VALUES (11, 3, 'Broken Door Handle', 'Door handle in office 201 broken', 'HIGH', 'OPEN', 1, 8, NOW(), NOW());

INSERT INTO complaints (user_id, category_id, title, description, priority, status, current_level, assigned_to, created_at, last_status_change) 
VALUES (11, 3, 'Electrical Outlet Issue', 'Power outlet not working in conference room', 'MEDIUM', 'OPEN', 1, 8, NOW(), NOW());