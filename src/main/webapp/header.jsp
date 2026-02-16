<%@ page contentType="text/html;charset=UTF-8" %>
<%
    String userRole = (String) session.getAttribute("userRole");
    String currentPage = request.getRequestURI();
    boolean isStaffOrAdmin = userRole != null && !userRole.equals("USER");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Complaint System</title>
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
                <% if (isStaffOrAdmin) { %>
                <li class="sidebar__item">
                    <a href="staffDashboard" class="sidebar__link <%= currentPage.contains("staffDashboard") ? "sidebar__link--active" : "" %>">
                        <span class="sidebar__link-icon"><%= "ADMIN".equals(userRole) ? "⚙️" : "📋" %></span>
                        <span><%= "ADMIN".equals(userRole) ? "All Complaints" : "My Work" %></span>
                    </a>
                </li>
                <% if ("ADMIN".equals(userRole)) { %>
                <li class="sidebar__item">
                    <a href="escalationPanel" class="sidebar__link <%= currentPage.contains("escalationPanel") ? "sidebar__link--active" : "" %>">
                        <span class="sidebar__link-icon">📈</span>
                        <span>Escalation Panel</span>
                    </a>
                </li>
                <% } %>
                <% } else { %>
                <li class="sidebar__item">
                    <a href="myComplaints" class="sidebar__link <%= currentPage.contains("myComplaints") ? "sidebar__link--active" : "" %>">
                        <span class="sidebar__link-icon">📋</span>
                        <span>My Complaints</span>
                    </a>
                </li>
                <li class="sidebar__item">
                    <a href="registerComplaint.jsp" class="sidebar__link <%= currentPage.contains("registerComplaint") ? "sidebar__link--active" : "" %>">
                        <span class="sidebar__link-icon">➕</span>
                        <span>New Complaint</span>
                    </a>
                </li>
                <% } %>
    </aside>

    <!-- Main Content -->
    <main class="app-main">
        <nav class="navbar">
            <div class="navbar__left">
                <button class="sidebar-toggle" aria-label="Toggle sidebar">☰</button>
                <h1 class="navbar__title">Complaint System</h1>
            </div>
            <div class="navbar__right">
                <div class="navbar__profile">
                    <div class="navbar__avatar"><%= session.getAttribute("userName") != null ? ((String)session.getAttribute("userName")).substring(0, 1).toUpperCase() : "U" %></div>
                    <span><%= session.getAttribute("userName") != null ? session.getAttribute("userName") : "User" %></span>
                </div>
                <a href="logout" class="btn btn--secondary btn--sm">Logout</a>
            </div>
        </nav>

        <div class="app-content">