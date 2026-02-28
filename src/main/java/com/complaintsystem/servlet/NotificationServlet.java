package com.complaintsystem.servlet;

import com.complaintsystem.dao.NotificationDAO;
import com.complaintsystem.model.Notification;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class NotificationServlet extends HttpServlet {

    private final NotificationDAO notificationDAO = new NotificationDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        int userId = (int) session.getAttribute("userId");
        String action = req.getParameter("action");

        try {
            if ("markRead".equals(action)) {
                String idStr = req.getParameter("id");
                if (idStr != null) {
                    notificationDAO.markAsRead(Integer.parseInt(idStr));
                }
                resp.sendRedirect("notifications");
                return;
            }

            List<Notification> notifications = notificationDAO.getNotificationsByUserId(userId);
            req.setAttribute("notifications", notifications);
            req.getRequestDispatcher("notifications.jsp").forward(req, resp);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
