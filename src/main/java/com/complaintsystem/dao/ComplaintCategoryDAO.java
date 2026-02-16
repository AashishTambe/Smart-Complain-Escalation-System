package com.complaintsystem.dao;

import com.complaintsystem.config.DBConnection;
import com.complaintsystem.model.ComplaintCategory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ComplaintCategoryDAO {

    public List<ComplaintCategory> findAll() throws SQLException {
        List<ComplaintCategory> list = new ArrayList<>();
        String sql = "SELECT * FROM complaint_categories ORDER BY name";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ComplaintCategory c = new ComplaintCategory();
                c.setId(rs.getInt("id"));
                c.setName(rs.getString("name"));
                c.setDepartmentId(rs.getInt("department_id"));
                list.add(c);
            }
        }
        return list;
    }
}

