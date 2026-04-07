package com.complaintsystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.complaintsystem.config.DBConnection;
import com.complaintsystem.model.Notification;

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

    public int createNotification(int complaintId, int recipientId, String type, String message) throws SQLException {
        String sql = "INSERT INTO notifications (complaint_id, recipient_id, type, message, sent) " +
                "VALUES (?, ?, ?, ?, 0)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, complaintId);
            ps.setInt(2, recipientId);
            ps.setString(3, type);
            ps.setString(4, message);
            ps.executeUpdate();

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        }
        return -1;
    }

    public void clearNotifications(int complaintId) throws SQLException {
        String sql = "DELETE FROM notifications WHERE complaint_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, complaintId);
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
