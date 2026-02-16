<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.complaintsystem.dao.ComplaintDAO" %>
<%@ page import="com.complaintsystem.model.Complaint" %>
<%
    String idParam = request.getParameter("id");
    Complaint complaint = null;
    if (idParam != null) {
        int id = Integer.parseInt(idParam);
        ComplaintDAO dao = new ComplaintDAO();
        try {
            complaint = dao.getById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    String userName = (String) session.getAttribute("userName");
    String userRole = (String) session.getAttribute("userRole");
    
    String statusClass = "";
    if (complaint != null) {
        if ("OPEN".equals(complaint.getStatus())) statusClass = "badge--open";
        else if ("IN_PROGRESS".equals(complaint.getStatus())) statusClass = "badge--in-progress";
        else if ("ESCALATED".equals(complaint.getStatus())) statusClass = "badge--escalated";
        else if ("RESOLVED".equals(complaint.getStatus())) statusClass = "badge--resolved";
        else if ("CLOSED".equals(complaint.getStatus())) statusClass = "badge--closed";
    }
%>
<html>
<head>
    <title>Complaint Details</title>
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
                <h1 class="navbar__title">Complaint Details</h1>
            </div>
            <div class="navbar__right">
                <% if (userName != null) { %>
                <div class="navbar__profile">
                    <div class="navbar__avatar"><%= userName.substring(0, 1).toUpperCase() %></div>
                    <span><%= userName %></span>
                </div>
                <a href="logout" class="btn btn--secondary btn--sm">Logout</a>
                <% } else { %>
                <a href="login.jsp" class="btn btn--primary btn--sm">Login</a>
                <% } %>
            </div>
        </nav>

        <div class="app-content">
            <% if (complaint == null) { %>
            <div class="card">
                <div class="alert alert--danger">
                    <span>⚠️</span>
                    <span>Complaint not found.</span>
                </div>
                <p><a href="myComplaints" class="btn btn--secondary">← Back to My Complaints</a></p>
            </div>
            <% } else { %>
            <div class="card">
                <div style="display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 24px;">
                    <div>
                        <h2 style="margin-top: 0;"><%= complaint.getTitle() %></h2>
                        <p class="text-muted">Complaint ID: #<%= complaint.getId() %></p>
                    </div>
                    <div>
                        <span class="badge <%= statusClass %>" style="font-size: 13px; padding: 6px 12px;"><%= complaint.getStatus() %></span>
                    </div>
                </div>

                <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: 20px; margin-bottom: 24px;">
                    <div>
                        <div class="card-subtitle">Category</div>
                        <div style="font-weight: 500; margin-top: 4px;"><%= complaint.getCategoryName() != null ? complaint.getCategoryName() : "-" %></div>
                    </div>
                    <div>
                        <div class="card-subtitle">Priority</div>
                        <div style="margin-top: 4px;">
                            <span class="badge badge--<%= complaint.getPriority().toLowerCase() %>"><%= complaint.getPriority() %></span>
                        </div>
                    </div>
                    <div>
                        <div class="card-subtitle">Escalation Level</div>
                        <div style="font-weight: 500; margin-top: 4px;">Level <%= complaint.getCurrentLevel() %></div>
                    </div>
                    <div>
                        <div class="card-subtitle">Assigned To</div>
                        <div style="font-weight: 500; margin-top: 4px;"><%= complaint.getAssignedToName() != null ? complaint.getAssignedToName() : "Not assigned" %></div>
                    </div>
                </div>

                <div style="margin-bottom: 24px;">
                    <div class="card-subtitle">Created At</div>
                    <div style="margin-top: 4px;"><%= complaint.getCreatedAt() != null ? complaint.getCreatedAt() : "-" %></div>
                </div>

                <div>
                    <div class="card-subtitle" style="margin-bottom: 8px;">Description</div>
                    <div style="background: var(--color-bg-muted); padding: 16px; border-radius: var(--radius-md); white-space: pre-wrap; line-height: 1.6;"><%= complaint.getDescription() %></div>
                </div>

                <div style="margin-top: 24px; padding-top: 20px; border-top: 1px solid var(--color-border);">
                    <a href="myComplaints" class="btn btn--secondary">← Back to My Complaints</a>
                </div>
            </div>
            <% } %>
        </div>
    </main>
</div>

<script src="js/main.js"></script>
</body>
</html>
