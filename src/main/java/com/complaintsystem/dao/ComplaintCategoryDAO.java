package com.complaintsystem.dao;

import com.complaintsystem.model.ComplaintCategory;
import com.complaintsystem.model.Department;
import com.complaintsystem.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ComplaintCategoryDAO {

    public List<ComplaintCategory> findAll() {
        return findAllByOrderByNameAsc();
    }

    public List<ComplaintCategory> findAllByOrderByNameAsc() {
        List<ComplaintCategory> categories = new ArrayList<>();
        String sql = "SELECT c.id, c.name, d.id as dept_id, d.name as dept_name " +
                     "FROM complaint_categories c " +
                     "JOIN departments d ON c.department_id = d.id " +
                     "ORDER BY c.name ASC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                ComplaintCategory category = new ComplaintCategory();
                category.setId(rs.getInt("id"));
                category.setName(rs.getString("name"));

                Department dept = new Department();
                dept.setId(rs.getInt("dept_id"));
                dept.setName(rs.getString("dept_name"));
                category.setDepartment(dept);

                categories.add(category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }

    public Optional<ComplaintCategory> findById(int id) {
        String sql = "SELECT c.id, c.name, d.id as dept_id, d.name as dept_name " +
                     "FROM complaint_categories c " +
                     "JOIN departments d ON c.department_id = d.id " +
                     "WHERE c.id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    ComplaintCategory category = new ComplaintCategory();
                    category.setId(rs.getInt("id"));
                    category.setName(rs.getString("name"));

                    Department dept = new Department();
                    dept.setId(rs.getInt("dept_id"));
                    dept.setName(rs.getString("dept_name"));
                    category.setDepartment(dept);
                    
                    return Optional.of(category);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
