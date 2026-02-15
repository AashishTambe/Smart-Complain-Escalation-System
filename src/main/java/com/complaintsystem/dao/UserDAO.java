package com.complaintsystem.dao;

import com.complaintsystem.model.Department;
import com.complaintsystem.model.User;
import com.complaintsystem.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDAO {

    public User save(User user) {
        String sql = "INSERT INTO users (name, email, password, role, department_id, active) VALUES (?, ?, ?, ?, ?, ?)";
        // If id exists, it's an update (not fully implemented here as save usually implies create or update)
        // For now handling create only for registration
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getRole());
            if (user.getDepartment() != null) {
                stmt.setInt(5, user.getDepartment().getId());
            } else {
                stmt.setNull(5, Types.INTEGER);
            }
            stmt.setBoolean(6, user.isActive());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return user;
    }

    public Optional<User> findByEmail(String email) {
        String sql = "SELECT u.*, d.name as dept_name FROM users u " +
                     "LEFT JOIN departments d ON u.department_id = d.id " +
                     "WHERE u.email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToUser(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
    
    public Optional<User> findById(int id) {
        String sql = "SELECT u.*, d.name as dept_name FROM users u " +
                     "LEFT JOIN departments d ON u.department_id = d.id " +
                     "WHERE u.id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToUser(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private User mapRowToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setRole(rs.getString("role"));
        user.setActive(rs.getBoolean("active"));

        int deptId = rs.getInt("department_id");
        if (!rs.wasNull()) {
            Department dept = new Department();
            dept.setId(deptId);
            dept.setName(rs.getString("dept_name"));
            user.setDepartment(dept);
        }
        return user;
    }
}
