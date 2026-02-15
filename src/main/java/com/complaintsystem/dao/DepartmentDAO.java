package com.complaintsystem.dao;

import com.complaintsystem.model.Department;
import com.complaintsystem.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DepartmentDAO {

    public List<Department> findAll() {
        List<Department> departments = new ArrayList<>();
        String sql = "SELECT * FROM departments";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Department dept = new Department();
                dept.setId(rs.getInt("id"));
                dept.setName(rs.getString("name"));
                departments.add(dept);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return departments;
    }

    public Optional<Department> findById(int id) {
        String sql = "SELECT * FROM departments WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Department dept = new Department();
                    dept.setId(rs.getInt("id"));
                    dept.setName(rs.getString("name"));
                    return Optional.of(dept);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
