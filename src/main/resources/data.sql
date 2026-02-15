CREATE DATABASE complaint_system;
USE complaint_system;
CREATE TABLE departments (id INT AUTO_INCREMENT PRIMARY KEY,name VARCHAR(100) NOT NULL UNIQUE);
drop table users;
create TABLE users (id INT AUTO_INCREMENT PRIMARY KEY,name VARCHAR(100) NOT NULL,email VARCHAR(150) NOT NULL UNIQUE,password VARCHAR(255) NOT NULL,role ENUM('USER','OFFICER_L1','OFFICER_L2','ADMIN') NOT NULL,department_id INT NULL,active TINYINT(1) DEFAULT 1,FOREIGN KEY (department_id) REFERENCES departments(id));
desc users;
CREATE TABLE complaint_categories (id INT AUTO_INCREMENT PRIMARY KEY,name VARCHAR(100) NOT NULL UNIQUE,department_id INT NOT NULL,FOREIGN KEY (department_id) REFERENCES departments(id));
CREATE TABLE complaints (id INT AUTO_INCREMENT PRIMARY KEY,user_id INT NOT NULL,category_id INT NOT NULL,title VARCHAR(200) NOT NULL,description TEXT NOT NULL,priority ENUM('LOW','MEDIUM','HIGH') NOT NULL,status ENUM('OPEN','IN_PROGRESS','ESCALATED','RESOLVED','CLOSED') DEFAULT 'OPEN',current_level INT NOT NULL DEFAULT 1,assigned_to INT NULL,created_at DATETIME DEFAULT CURRENT_TIMESTAMP,last_status_change DATETIME DEFAULT CURRENT_TIMESTAMP,FOREIGN KEY (user_id) REFERENCES users(id),FOREIGN KEY (category_id) REFERENCES complaint_categories(id),FOREIGN KEY (assigned_to) REFERENCES users(id));
CREATE TABLE escalation_rules (id INT AUTO_INCREMENT PRIMARY KEY,category_id INT NULL,priority ENUM('LOW','MEDIUM','HIGH') NOT NULL,level INT NOT NULL,sla_hours INT NOT NULL,UNIQUE (category_id, priority, level),FOREIGN KEY (category_id) REFERENCES complaint_categories(id));
CREATE TABLE complaint_history (id INT AUTO_INCREMENT PRIMARY KEY,complaint_id INT NOT NULL,old_status ENUM('OPEN','IN_PROGRESS','ESCALATED','RESOLVED','CLOSED'),new_status ENUM('OPEN','IN_PROGRESS','ESCALATED','RESOLVED','CLOSED'),old_level INT,new_level INT,changed_by INT NULL,comment VARCHAR(255),changed_at DATETIME DEFAULT CURRENT_TIMESTAMP,FOREIGN KEY (complaint_id) REFERENCES complaints(id),FOREIGN KEY (changed_by) REFERENCES users(id));
CREATE TABLE notifications (id INT AUTO_INCREMENT PRIMARY KEY,complaint_id INT NOT NULL,recipient_id INT NOT NULL,type ENUM('EMAIL','SYSTEM') NOT NULL,message VARCHAR(255) NOT NULL,created_at DATETIME DEFAULT CURRENT_TIMESTAMP,sent TINYINT(1) DEFAULT 0,sent_at DATETIME NULL,FOREIGN KEY (complaint_id) REFERENCES complaints(id),FOREIGN KEY (recipient_id) REFERENCES users(id));
SHOW TABLES;
INSERT INTO departments (name) VALUES ('IT'), ('HR'), ('Maintenance');

INSERT INTO complaint_categories (id, name, department_id) VALUES (1, 'IT', 1);
INSERT INTO complaint_categories (id, name, department_id) VALUES (2, 'Service', 2);
INSERT INTO complaint_categories (id, name, department_id) VALUES (3, 'Infrastructure', 3);

INSERT INTO users (name, email, password, role)VALUES ('Admin2', 'admin2@test.com', '1234', 'ADMIN');

select * from departments;
select * from complaint_categories;
select * from complaints;
select * from escalation_rules;