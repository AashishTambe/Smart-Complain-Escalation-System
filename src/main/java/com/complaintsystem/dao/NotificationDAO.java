package com.complaintsystem.dao;

import com.complaintsystem.config.DBConnection;
import com.complaintsystem.model.Notification;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAO {

    public List<Notification> getNotificationsByUserId(int userId) throws SQLException {
        List<Notification> list = new ArrayList<>();
        String sql = "SELECT * FROM notifications WHERE recipient_id = ? ORDER BY created_at DESC";
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

    public int getUnreadCount(int userId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM notifications WHERE recipient_id = ? AND sent = 0";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    public void markAsRead(int notificationId) throws SQLException {
        String sql = "UPDATE notifications SET sent = 1, sent_at = NOW() WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, notificationId);
            ps.executeUpdate();
        }
    }

    private Notification mapRow(ResultSet rs) throws SQLException {
        Notification n = new Notification();
        n.setId(rs.getInt("id"));
        n.setComplaintId(rs.getInt("complaint_id"));
        n.setRecipientId(rs.getInt("recipient_id"));
        n.setType(rs.getString("type"));
        n.setMessage(rs.getString("message"));
        n.setCreatedAt(rs.getTimestamp("created_at"));
        n.setSent(rs.getBoolean("sent"));
        n.setSentAt(rs.getTimestamp("sent_at"));
        return n;
    }
}
