<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.complaintsystem.model.Notification" %>
<%@ page import="java.util.List" %>

<%@ include file="header.jsp" %>

<%
@SuppressWarnings("unchecked")
List<Notification> notifications = (List<Notification>) request.getAttribute("notifications");
%>

<div class="flex-between mb-6">
    <h2 style="margin: 0;">My Notifications</h2>
</div>

<% if (notifications == null || notifications.isEmpty()) { %>

<div class="card text-center py-8">
    <div style="font-size: 48px; margin-bottom: 16px; opacity: 0.3;">📭</div>
    <p class="text-muted">You have no notifications at the moment.</p>
    <a href="index.jsp" class="btn btn--primary mt-4">Go to Home</a>
</div>

<% } else { %>

<div class="notification-list">
<% for (Notification n : notifications) {
    String unreadClass = (!n.isSent()) ? "notification-item--unread" : "";
%>

    <div class="notification-item <%= unreadClass %>">
        <div class="notification-content">
            <div class="notification-message">
                <%= n.getMessage() %>
            </div>
            <div class="notification-time">
                <%= n.getCreatedAt() %>
            </div>
        </div>
    </div>

<% } %>
</div>

<% } %>

<%@ include file="footer.jsp" %>