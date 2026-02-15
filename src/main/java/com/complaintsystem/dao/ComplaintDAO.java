package com.complaintsystem.dao;

import com.complaintsystem.model.Complaint;
import com.complaintsystem.model.ComplaintCategory;
import com.complaintsystem.model.Department;
import com.complaintsystem.model.User;
import com.complaintsystem.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ComplaintDAO {

    public Complaint save(Complaint complaint) {
        if (complaint.getId() == 0) {
            return create(complaint);
        } else {
            return update(complaint);
        }
    }

    private Complaint create(Complaint complaint) {
        String sql = "INSERT INTO complaints (user_id, category_id, title, description, priority, status, current_level, assigned_to, created_at, last_status_change) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, complaint.getUser().getId());
            stmt.setInt(2, complaint.getCategory().getId());
            stmt.setString(3, complaint.getTitle());
            stmt.setString(4, complaint.getDescription());
            stmt.setString(5, complaint.getPriority());
            stmt.setString(6, complaint.getStatus());
            stmt.setInt(7, complaint.getCurrentLevel());
            
            if (complaint.getAssignedTo() != null) {
                stmt.setInt(8, complaint.getAssignedTo().getId());
            } else {
                stmt.setNull(8, Types.INTEGER);
            }

            Timestamp now = new Timestamp(System.currentTimeMillis());
            complaint.setCreatedAt(now);
            complaint.setLastStatusChange(now);

            stmt.setTimestamp(9, now);
            stmt.setTimestamp(10, now);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating complaint failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    complaint.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return complaint;
    }

    private Complaint update(Complaint complaint) {
        String sql = "UPDATE complaints SET category_id = ?, title = ?, description = ?, priority = ?, status = ?, current_level = ?, assigned_to = ?, last_status_change = ? WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, complaint.getCategory().getId());
            stmt.setString(2, complaint.getTitle());
            stmt.setString(3, complaint.getDescription());
            stmt.setString(4, complaint.getPriority());
            stmt.setString(5, complaint.getStatus());
            stmt.setInt(6, complaint.getCurrentLevel());
            
            if (complaint.getAssignedTo() != null) {
                stmt.setInt(7, complaint.getAssignedTo().getId());
            } else {
                stmt.setNull(7, Types.INTEGER);
            }
            
            Timestamp now = new Timestamp(System.currentTimeMillis());
            // Only update lastStatusChange if we want to track every update, or check logic.
            // For now, updating on every save.
            complaint.setLastStatusChange(now);
            stmt.setTimestamp(8, now);
            
            stmt.setInt(9, complaint.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return complaint;
    }

    public List<Complaint> findByUserOrderByCreatedAtDesc(User user) {
        return findByCondition("c.user_id = ?", user.getId());
    }

    public List<Complaint> findByAssignedToOrderByCreatedAtDesc(User user) {
        return findByCondition("c.assigned_to = ?", user.getId());
    }

    public List<Complaint> findAllByOrderByCreatedAtDesc() {
        return findByCondition(null, 0); // Hacky way to findAll
    }

    public Optional<Complaint> findById(int id) {
        List<Complaint> complaints = findByCondition("c.id = ?", id);
        return complaints.isEmpty() ? Optional.empty() : Optional.of(complaints.get(0));
    }

    private List<Complaint> findByCondition(String whereClause, int param) {
        List<Complaint> complaints = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT c.*, " +
            "u.id as u_id, u.name as u_name, u.email as u_email, u.role as u_role, " +
            "cat.id as cat_id, cat.name as cat_name, " +
            "d.id as d_id, d.name as d_name, " +
            "a.id as a_id, a.name as a_name, a.email as a_email, a.role as a_role " +
            "FROM complaints c " +
            "JOIN users u ON c.user_id = u.id " +
            "JOIN complaint_categories cat ON c.category_id = cat.id " +
            "JOIN departments d ON cat.department_id = d.id " +
            "LEFT JOIN users a ON c.assigned_to = a.id ");

        if (whereClause != null) {
            sql.append("WHERE ").append(whereClause).append(" ");
        }
        sql.append("ORDER BY c.created_at DESC");

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            if (whereClause != null) {
                stmt.setInt(1, param);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    complaints.add(mapRowToComplaint(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return complaints;
    }

    private Complaint mapRowToComplaint(ResultSet rs) throws SQLException {
        Complaint c = new Complaint();
        c.setId(rs.getInt("id"));
        c.setTitle(rs.getString("title"));
        c.setDescription(rs.getString("description"));
        c.setPriority(rs.getString("priority"));
        c.setStatus(rs.getString("status"));
        c.setCurrentLevel(rs.getInt("current_level"));
        c.setCreatedAt(rs.getTimestamp("created_at"));
        c.setLastStatusChange(rs.getTimestamp("last_status_change"));

        User user = new User();
        user.setId(rs.getInt("u_id"));
        user.setName(rs.getString("u_name"));
        user.setEmail(rs.getString("u_email"));
        user.setRole(rs.getString("u_role"));
        c.setUser(user);

        ComplaintCategory cat = new ComplaintCategory();
        cat.setId(rs.getInt("cat_id"));
        cat.setName(rs.getString("cat_name"));
        Department dept = new Department();
        dept.setId(rs.getInt("d_id"));
        dept.setName(rs.getString("d_name"));
        cat.setDepartment(dept);
        c.setCategory(cat);

        int assignedId = rs.getInt("a_id");
        if (!rs.wasNull()) {
            User assigned = new User();
            assigned.setId(assignedId);
            assigned.setName(rs.getString("a_name"));
            assigned.setEmail(rs.getString("a_email"));
            assigned.setRole(rs.getString("a_role"));
            c.setAssignedTo(assigned);
        }

        return c;
    }
}
