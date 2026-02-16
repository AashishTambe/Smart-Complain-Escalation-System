<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.complaintsystem.model.Complaint" %>
<%@ page import="java.util.List" %>
<%
    List<Complaint> complaints = (List<Complaint>) request.getAttribute("complaints");
    String userName = (String) session.getAttribute("userName");
    String userRole = (String) session.getAttribute("userRole");
%>
<html>
<head>
    <title>My Complaints</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
</head>
<body>
<div class="app-shell">
    <!-- Sidebar -->
    <aside class="sidebar">
        <div class="sidebar__logo">
            <div class="sidebar__logo-icon">CS</div>
            <span>Complaint System</span>
        </div>
        <nav class="sidebar__nav">
            <ul class="sidebar__menu">
                <li class="sidebar__item">
                    <a href="index.jsp" class="sidebar__link">
                        <span class="sidebar__link-icon">🏠</span>
                        <span>Home</span>
                    </a>
                </li>
                <li class="sidebar__item">
                    <a href="myComplaints" class="sidebar__link sidebar__link--active">
                        <span class="sidebar__link-icon">📋</span>
                        <span>My Complaints</span>
                    </a>
                </li>
                <li class="sidebar__item">
                    <a href="registerComplaint.jsp" class="sidebar__link">
                        <span class="sidebar__link-icon">➕</span>
                        <span>New Complaint</span>
                    </a>
                </li>
                <% if ("ADMIN".equals(userRole)) { %>
                <li class="sidebar__item">
                    <a href="staffDashboard" class="sidebar__link">
                        <span class="sidebar__link-icon">⚙️</span>
                        <span>All Complaints</span>
                    </a>
                </li>
                <% } %>
            </ul>
        </nav>
    </aside>

    <!-- Main Content -->
    <main class="app-main">
        <nav class="navbar">
            <div class="navbar__left">
                <button class="sidebar-toggle" aria-label="Toggle sidebar">☰</button>
                <h1 class="navbar__title">My Complaints</h1>
            </div>
            <div class="navbar__right">
                <div class="navbar__profile">
                    <div class="navbar__avatar"><%= userName != null ? userName.substring(0, 1).toUpperCase() : "U" %></div>
                    <span><%= userName != null ? userName : "User" %></span>
                </div>
                <a href="logout" class="btn btn--secondary btn--sm">Logout</a>
            </div>
        </nav>

        <div class="app-content">
            <div class="flex-between" style="margin-bottom: 20px;">
                <h2 style="margin: 0;"><%= "USER".equals(userRole) ? "My Complaints" : "Assigned Complaints" %></h2>
                <% if ("USER".equals(userRole)) { %>
                <a href="registerComplaint.jsp" class="btn btn--primary">➕ Register New Complaint</a>
                <% } %>
            </div>

            <% if (complaints == null || complaints.isEmpty()) { %>
            <div class="card">
                <p style="text-align: center; color: var(--color-text-muted);">
                    <%= "USER".equals(userRole) ? "You haven't registered any complaints yet." : "No complaints assigned to you." %>
                    <br/>
                    <% if ("USER".equals(userRole)) { %>
                    <a href="registerComplaint.jsp" class="btn btn--primary" style="margin-top: 12px;">Register Your First Complaint</a>
                    <% } %>
                </p>
            </div>
            <% } else { %>
            <div class="table-wrapper">
                <table class="table complaints-table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Title</th>
                            <th>Category</th>
                            <th>Priority</th>
                            <th>Status</th>
                            <th>Level</th>
                            <th>Assigned To</th>
                            <th>Created At</th>
                            <% if (!"USER".equals(userRole)) { %>
                            <th>Actions</th>
                            <% } %>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (Complaint c : complaints) {
                            String statusClass = "";
                            if ("OPEN".equals(c.getStatus())) statusClass = "badge--open";
                            else if ("IN_PROGRESS".equals(c.getStatus())) statusClass = "badge--in-progress";
                            else if ("ESCALATED".equals(c.getStatus())) statusClass = "badge--escalated";
                            else if ("RESOLVED".equals(c.getStatus())) statusClass = "badge--resolved";
                            else if ("CLOSED".equals(c.getStatus())) statusClass = "badge--closed";
                        %>
                        <tr>
                            <td><a href="viewComplaint.jsp?id=<%= c.getId() %>"><%= c.getId() %></a></td>
                            <td><strong><%= c.getTitle() %></strong></td>
                            <td><%= c.getCategoryName() != null ? c.getCategoryName() : "-" %></td>
                            <td><span class="badge badge--<%= c.getPriority().toLowerCase() %>"><%= c.getPriority() %></span></td>
                            <td><span class="badge <%= statusClass %>"><%= c.getStatus() %></span></td>
                            <td>L<%= c.getCurrentLevel() %></td>
                            <td><%= c.getAssignedToName() != null ? c.getAssignedToName() : "-" %></td>
                            <td><%= c.getCreatedAt() != null ? c.getCreatedAt() : "-" %></td>
                            <% if (!"USER".equals(userRole)) { %>
                            <td>
                                <form action="updateStatus" method="post" class="complaint-actions">
                                    <input type="hidden" name="complaintId" value="<%= c.getId() %>" />
                                    <select name="status" class="form-select" style="width: auto; padding: 4px 8px; font-size: 12px;">
                                        <option value="OPEN" <%= "OPEN".equals(c.getStatus()) ? "selected" : "" %>>OPEN</option>
                                        <option value="IN_PROGRESS" <%= "IN_PROGRESS".equals(c.getStatus()) ? "selected" : "" %>>IN_PROGRESS</option>
                                        <option value="RESOLVED" <%= "RESOLVED".equals(c.getStatus()) ? "selected" : "" %>>RESOLVED</option>
                                        <option value="CLOSED" <%= "CLOSED".equals(c.getStatus()) ? "selected" : "" %>>CLOSED</option>
                                    </select>
                                    <button type="submit" class="btn btn--primary btn--sm">Update</button>
                                </form>
                            </td>
                            <% } %>
                        </tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
            <% } %>
        </div>
    </main>
</div>

<script src="js/main.js"></script>
</body>
</html>
