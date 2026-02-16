package com.complaintsystem.dao;

import com.complaintsystem.config.DBConnection;
import com.complaintsystem.model.Complaint;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ComplaintDAO {

    public List<Complaint> getComplaintsByUser(int userId) throws SQLException {
        List<Complaint> list = new ArrayList<>();
        String sql = "SELECT c.id, c.user_id, c.category_id, c.title, c.description, c.priority, " +
                "c.status, c.current_level, c.assigned_to, c.created_at, c.last_status_change, " +
                "cc.name AS category_name, u.name AS assigned_to_name " +
                "FROM complaints c " +
                "JOIN complaint_categories cc ON c.category_id = cc.id " +
                "LEFT JOIN users u ON c.assigned_to = u.id " +
                "WHERE c.user_id = ? ORDER BY c.created_at DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    public List<Complaint> getComplaintsAssignedTo(int userId) throws SQLException {
        List<Complaint> list = new ArrayList<>();
        String sql = "SELECT c.id, c.user_id, c.category_id, c.title, c.description, c.priority, " +
                "c.status, c.current_level, c.assigned_to, c.created_at, c.last_status_change, " +
                "cc.name AS category_name, u.name AS assigned_to_name " +
                "FROM complaints c " +
                "JOIN complaint_categories cc ON c.category_id = cc.id " +
                "JOIN users u ON c.assigned_to = u.id " +
                "WHERE c.assigned_to = ? " +
                "ORDER BY c.created_at DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    public List<Complaint> getComplaintsForAdmin(String status, String priority, Integer departmentId)
            throws SQLException {

        StringBuilder sb = new StringBuilder(
                "SELECT c.id, c.user_id, c.category_id, c.title, c.description, c.priority, " +
                "c.status, c.current_level, c.assigned_to, c.created_at, c.last_status_change, " +
                "cc.name AS category_name, u.name AS assigned_to_name, d.id AS dept_id " +
                "FROM complaints c " +
                "JOIN complaint_categories cc ON c.category_id = cc.id " +
                "LEFT JOIN departments d ON cc.department_id = d.id " +
                "LEFT JOIN users u ON c.assigned_to = u.id WHERE 1=1"
        );
        List<Object> params = new ArrayList<>();

        if (status != null && !status.isEmpty()) {
            sb.append(" AND c.status = ?");
            params.add(status);
        }
        if (priority != null && !priority.isEmpty()) {
            sb.append(" AND c.priority = ?");
            params.add(priority);
        }
        if (departmentId != null) {
            sb.append(" AND d.id = ?");
            params.add(departmentId);
        }
        sb.append(" ORDER BY c.created_at DESC");

        List<Complaint> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sb.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    public Complaint getById(int id) throws SQLException {
        String sql = "SELECT c.*, cc.name AS category_name, u.name AS assigned_to_name " +
                "FROM complaints c " +
                "JOIN complaint_categories cc ON c.category_id = cc.id " +
                "LEFT JOIN users u ON c.assigned_to = u.id " +
                "WHERE c.id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    public void updateStatus(int complaintId, String newStatus) throws SQLException {
        String sql = "UPDATE complaints SET status = ?, last_status_change = NOW() WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newStatus);
            ps.setInt(2, complaintId);
            ps.executeUpdate();
        }
    }

    private Complaint mapRow(ResultSet rs) throws SQLException {
        Complaint c = new Complaint();
        c.setId(rs.getInt("id"));
        c.setUserId(rs.getInt("user_id"));
        c.setCategoryId(rs.getInt("category_id"));
        c.setTitle(rs.getString("title"));
        c.setDescription(rs.getString("description"));
        c.setPriority(rs.getString("priority"));
        c.setStatus(rs.getString("status"));
        c.setCurrentLevel(rs.getInt("current_level"));
        
        int assigned = rs.getInt("assigned_to");
        c.setAssignedTo(rs.wasNull() ? null : assigned);
        
        c.setCreatedAt(rs.getTimestamp("created_at"));
        c.setLastStatusChange(rs.getTimestamp("last_status_change"));
        
        // These columns may not exist in all queries, handle gracefully
        try {
            String catName = rs.getString("category_name");
            if (catName != null) c.setCategoryName(catName);
        } catch (SQLException ignored) {
        }
        
        try {
            String assignedName = rs.getString("assigned_to_name");
            if (assignedName != null) c.setAssignedToName(assignedName);
        } catch (SQLException ignored) {
        }
        
        return c;
    }
}

