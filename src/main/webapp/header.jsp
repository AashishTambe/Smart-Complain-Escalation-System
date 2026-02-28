<%@ page contentType="text/html;charset=UTF-8" %>
    <%@ page import="com.complaintsystem.dao.NotificationDAO" %>
        <% String userRole=(String) session.getAttribute("userRole"); %>
            <% if (userRole==null) { userRole="GUEST" ; } %>
                <% userRole=userRole.trim().toUpperCase(); %>
                    <% Integer userId=(Integer) session.getAttribute("userId"); %>
                        <% String userName=(String) session.getAttribute("userName"); %>
                            <% if (userName==null) { userName="User" ; } %>
                                <% String currentPage=request.getRequestURI(); %>
                                    <% boolean isStaffOrAdmin=!"USER".equals(userRole) && !"GUEST".equals(userRole); %>
                                        <% String activeHome=(currentPage.endsWith("index.jsp") ||
                                            currentPage.endsWith("/ComplaintSystem/")) ? "sidebar__link--active" : "" ;
                                            %>
                                            <% String activeDashboard=currentPage.contains("staffDashboard")
                                                ? "sidebar__link--active" : "" ; %>
                                                <% String activeEscalation=currentPage.contains("escalationPanel")
                                                    ? "sidebar__link--active" : "" ; %>
                                                    <% String activeLogin=currentPage.contains("login.jsp")
                                                        ? "sidebar__link--active" : "" ; %>
                                                        <% String activeRegister=currentPage.contains("register.jsp")
                                                            ? "sidebar__link--active" : "" ; %>
                                                            <% String
                                                                activeMyComplaints=currentPage.contains("myComplaints")
                                                                ? "sidebar__link--active" : "" ; %>
                                                                <% String
                                                                    activeNewComplaint=currentPage.contains("registerComplaint")
                                                                    ? "sidebar__link--active" : "" ; %>
                                                                    <% int unreadCount=0; %>
                                                                        <% if (userId !=null) { try { unreadCount=new
                                                                            NotificationDAO().getUnreadCount(userId); }
                                                                            catch (Exception e) {} } %>
                                                                            <!DOCTYPE html>
                                                                            <html>

                                                                            <head>
                                                                                <title>Complaint System</title>
                                                                                <link rel="stylesheet"
                                                                                    href="${pageContext.request.contextPath}/css/styles.css">
                                                                                <meta name="viewport"
                                                                                    content="width=device-width, initial-scale=1.0" />
                                                                            </head>

                                                                            <body>
                                                                                <div class="app-shell">
                                                                                    <aside class="sidebar">
                                                                                        <div class="sidebar__logo">
                                                                                            <div
                                                                                                class="sidebar__logo-icon">
                                                                                                CS</div>
                                                                                            <span>Complaint
                                                                                                System</span>
                                                                                        </div>
                                                                                        <nav class="sidebar__nav">
                                                                                            <ul class="sidebar__menu">
                                                                                                <li
                                                                                                    class="sidebar__item">
                                                                                                    <a href="index.jsp"
                                                                                                        class="sidebar__link <%= activeHome %>">
                                                                                                        <span
                                                                                                            class="sidebar__link-icon">🏠</span>
                                                                                                        <span>Home</span>
                                                                                                    </a>
                                                                                                </li>
                                                                                                <% if (isStaffOrAdmin) {
                                                                                                    %>
                                                                                                    <li
                                                                                                        class="sidebar__item">
                                                                                                        <a href="staffDashboard"
                                                                                                            class="sidebar__link <%= activeDashboard %>">
                                                                                                            <span
                                                                                                                class="sidebar__link-icon">
                                                                                                                <%= "ADMIN"
                                                                                                                    .equals(userRole)
                                                                                                                    ? "⚙️"
                                                                                                                    : "📋"
                                                                                                                    %>
                                                                                                            </span>
                                                                                                            <span>
                                                                                                                <%= "ADMIN"
                                                                                                                    .equals(userRole)
                                                                                                                    ? "All Complaints"
                                                                                                                    : "Assigned Complaints"
                                                                                                                    %>
                                                                                                            </span>
                                                                                                        </a>
                                                                                                    </li>
                                                                                                    <% if
                                                                                                        ("ADMIN".equals(userRole))
                                                                                                        { %>
                                                                                                        <li
                                                                                                            class="sidebar__item">
                                                                                                            <a href="escalationPanel"
                                                                                                                class="sidebar__link <%= activeEscalation %>">
                                                                                                                <span
                                                                                                                    class="sidebar__link-icon">📈</span>
                                                                                                                <span>Escalation
                                                                                                                    Panel</span>
                                                                                                            </a>
                                                                                                        </li>
                                                                                                        <% } %>
                                                                                                            <% } %>
                                                                                                                <% if
                                                                                                                    (userId==null)
                                                                                                                    { %>
                                                                                                                    <li
                                                                                                                        class="sidebar__item">
                                                                                                                        <a href="login.jsp"
                                                                                                                            class="sidebar__link <%= activeLogin %>">
                                                                                                                            <span
                                                                                                                                class="sidebar__link-icon">🔐</span>
                                                                                                                            <span>Login</span>
                                                                                                                        </a>
                                                                                                                    </li>
                                                                                                                    <li
                                                                                                                        class="sidebar__item">
                                                                                                                        <a href="register.jsp"
                                                                                                                            class="sidebar__link <%= activeRegister %>">
                                                                                                                            <span
                                                                                                                                class="sidebar__link-icon">👤</span>
                                                                                                                            <span>Register</span>
                                                                                                                        </a>
                                                                                                                    </li>
                                                                                                                    <% } else
                                                                                                                        {
                                                                                                                        %>
                                                                                                                        <% if
                                                                                                                            (!isStaffOrAdmin)
                                                                                                                            {
                                                                                                                            %>
                                                                                                                            <li
                                                                                                                                class="sidebar__item">
                                                                                                                                <a href="myComplaints"
                                                                                                                                    class="sidebar__link <%= activeMyComplaints %>">
                                                                                                                                    <span
                                                                                                                                        class="sidebar__link-icon">📋</span>
                                                                                                                                    <span>My
                                                                                                                                        Complaints</span>
                                                                                                                                </a>
                                                                                                                            </li>
                                                                                                                            <li
                                                                                                                                class="sidebar__item">
                                                                                                                                <a href="registerComplaint.jsp"
                                                                                                                                    class="sidebar__link <%= activeNewComplaint %>">
                                                                                                                                    <span
                                                                                                                                        class="sidebar__link-icon">➕</span>
                                                                                                                                    <span>New
                                                                                                                                        Complaint</span>
                                                                                                                                </a>
                                                                                                                            </li>
                                                                                                                            <% }
                                                                                                                                %>
                                                                                                                                <% }
                                                                                                                                    %>
                                                                                            </ul>
                                                                                        </nav>
                                                                                    </aside>
                                                                                    <main class="app-main">
                                                                                        <nav class="navbar">
                                                                                            <div class="navbar__left">
                                                                                                <button
                                                                                                    class="sidebar-toggle"
                                                                                                    aria-label="Toggle sidebar">☰</button>
                                                                                                <h1
                                                                                                    class="navbar__title">
                                                                                                    Complaint System
                                                                                                </h1>
                                                                                            </div>
                                                                                            <div class="navbar__right">
                                                                                                <% if (userId !=null) {
                                                                                                    %>
                                                                                                    <a href="notifications"
                                                                                                        class="navbar__action"
                                                                                                        title="Notifications">
                                                                                                        <span
                                                                                                            class="navbar__icon">🔔</span>
                                                                                                        <% if
                                                                                                            (unreadCount>
                                                                                                            0) { %>
                                                                                                            <span
                                                                                                                class="navbar__badge">
                                                                                                                <%= unreadCount
                                                                                                                    %>
                                                                                                            </span>
                                                                                                            <% } %>
                                                                                                    </a>
                                                                                                    <% } %>
                                                                                                        <div
                                                                                                            class="navbar__profile">
                                                                                                            <div
                                                                                                                class="navbar__avatar">
                                                                                                                <%= (userName
                                                                                                                    !=null
                                                                                                                    &&
                                                                                                                    userName.length()>
                                                                                                                    0) ?
                                                                                                                    userName.substring(0,
                                                                                                                    1).toUpperCase()
                                                                                                                    :
                                                                                                                    "U"
                                                                                                                    %>
                                                                                                            </div>
                                                                                                            <span>
                                                                                                                <%= userName
                                                                                                                    %>
                                                                                                            </span>
                                                                                                        </div>
                                                                                                        <% if (userId
                                                                                                            !=null) { %>
                                                                                                            <a href="logout"
                                                                                                                class="btn btn--secondary btn--sm">Logout</a>
                                                                                                            <% } else {
                                                                                                                %>
                                                                                                                <a href="login.jsp"
                                                                                                                    class="btn btn--primary btn--sm">Login</a>
                                                                                                                <% } %>
                                                                                            </div>
                                                                                        </nav>
                                                                                        <div class="app-content">