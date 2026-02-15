<%@ page import="com.complaintsystem.model.Complaint" %>
    <%@ page import="com.complaintsystem.model.User" %>
        <%@ page import="com.complaintsystem.model.ComplaintCategory" %>
            <%@ page import="com.complaintsystem.util.DBConnection" %>
                <%@ page import="java.sql.*" %>
                    <%@ page import="java.util.Date" %>
                        <% String idStr=request.getParameter("id"); Complaint complaint=null; if (idStr !=null &&
                            !idStr.isEmpty()) { try (Connection conn=DBConnection.getConnection(); PreparedStatement
                            pstmt=conn.prepareStatement( "SELECT c.*, u.username as assigned_to_name, cat.name as category_name "
                            + "FROM complaints c " + "LEFT JOIN users u ON c.assigned_to_id = u.id "
                            + "LEFT JOIN complaint_categories cat ON c.category_id = cat.id " + "WHERE c.id = ?" )) {
                            pstmt.setInt(1, Integer.parseInt(idStr)); try (ResultSet rs=pstmt.executeQuery()) { if
                            (rs.next()) { complaint=new Complaint(); complaint.setId(rs.getInt("id"));
                            complaint.setTitle(rs.getString("title"));
                            complaint.setDescription(rs.getString("description"));
                            complaint.setStatus(rs.getString("status"));
                            complaint.setPriority(rs.getString("priority"));
                            complaint.setCurrentLevel(rs.getInt("current_level"));
                            complaint.setCreatedAt(rs.getTimestamp("created_at")); // Populate ComplaintCategory for
                            getCategoryName() String catName=rs.getString("category_name"); if (catName !=null) {
                            ComplaintCategory category=new ComplaintCategory(); category.setName(catName);
                            complaint.setCategory(category); } // Populate User for getAssignedToName() String
                            assignedName=rs.getString("assigned_to_name"); if (assignedName !=null) { User user=new
                            User(); user.setName(assignedName); // AssignedToName uses user.getName() which might be
                            username or full name. // Let's check User model if needed, but usually getName() is either
                            a method or it uses username. // If User.java has getName(), I should use that. // If
                            User.java is an Entity, it might have specific fields. // For now, I'll set username. If
                            getName() returns something else, I might need to check. // But let's assume username is
                            what we want or the User model has a getName() that returns username or similar.
                            complaint.setAssignedTo(user); } } } } catch (Exception e) { e.printStackTrace(); } } String
                            userName=(String) session.getAttribute("userName"); String userRole=(String)
                            session.getAttribute("userRole"); String statusClass="" ; if (complaint !=null) { if
                            ("OPEN".equals(complaint.getStatus())) statusClass="badge--open" ; else if
                            ("IN_PROGRESS".equals(complaint.getStatus())) statusClass="badge--in-progress" ; else if
                            ("ESCALATED".equals(complaint.getStatus())) statusClass="badge--escalated" ; else if
                            ("RESOLVED".equals(complaint.getStatus())) statusClass="badge--resolved" ; else if
                            ("CLOSED".equals(complaint.getStatus())) statusClass="badge--closed" ; } %>
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
                                                        <a href="adminDashboard" class="sidebar__link">
                                                            <span class="sidebar__link-icon">⚙️</span>
                                                            <span>Admin Dashboard</span>
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
                                                <% if (userName !=null) { %>
                                                    <div class="navbar__profile">
                                                        <div class="navbar__avatar">
                                                            <%= userName.substring(0, 1).toUpperCase() %>
                                                        </div>
                                                        <span>
                                                            <%= userName %>
                                                        </span>
                                                    </div>
                                                    <a href="logout" class="btn btn--secondary btn--sm">Logout</a>
                                                    <% } else { %>
                                                        <a href="login.jsp" class="btn btn--primary btn--sm">Login</a>
                                                        <% } %>
                                            </div>
                                        </nav>

                                        <div class="app-content">
                                            <% if (complaint==null) { %>
                                                <div class="card">
                                                    <div class="alert alert--danger">
                                                        <span>⚠️</span>
                                                        <span>Complaint not found.</span>
                                                    </div>
                                                    <p><a href="myComplaints" class="btn btn--secondary">← Back to My
                                                            Complaints</a>
                                                    </p>
                                                </div>
                                                <% } else { %>
                                                    <div class="card">
                                                        <div
                                                            style="display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 24px;">
                                                            <div>
                                                                <h2 style="margin-top: 0;">
                                                                    <%= complaint.getTitle() %>
                                                                </h2>
                                                                <p class="text-muted">Complaint ID: #<%=
                                                                        complaint.getId() %>
                                                                </p>
                                                            </div>
                                                            <div>
                                                                <span class="badge <%= statusClass %>"
                                                                    style="font-size: 13px; padding: 6px 12px;">
                                                                    <%= complaint.getStatus() %>
                                                                </span>
                                                            </div>
                                                        </div>

                                                        <div
                                                            style="display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: 20px; margin-bottom: 24px;">
                                                            <div>
                                                                <div class="card-subtitle">Category</div>
                                                                <div style="font-weight: 500; margin-top: 4px;">
                                                                    <%= complaint.getCategoryName() !=null ?
                                                                        complaint.getCategoryName() : "-" %>
                                                                </div>
                                                            </div>
                                                            <div>
                                                                <div class="card-subtitle">Priority</div>
                                                                <div style="margin-top: 4px;">
                                                                    <span
                                                                        class="badge badge--<%= complaint.getPriority().toLowerCase() %>">
                                                                        <%= complaint.getPriority() %>
                                                                    </span>
                                                                </div>
                                                            </div>
                                                            <div>
                                                                <div class="card-subtitle">Escalation Level</div>
                                                                <div style="font-weight: 500; margin-top: 4px;">Level
                                                                    <%= complaint.getCurrentLevel() %>
                                                                </div>
                                                            </div>
                                                            <div>
                                                                <div class="card-subtitle">Assigned To</div>
                                                                <div style="font-weight: 500; margin-top: 4px;">
                                                                    <%= complaint.getAssignedToName() !=null ?
                                                                        complaint.getAssignedToName() : "Not assigned"
                                                                        %>
                                                                </div>
                                                            </div>
                                                        </div>

                                                        <div style="margin-bottom: 24px;">
                                                            <div class="card-subtitle">Created At</div>
                                                            <div style="margin-top: 4px;">
                                                                <%= complaint.getCreatedAt() !=null ?
                                                                    complaint.getCreatedAt() : "-" %>
                                                            </div>
                                                        </div>

                                                        <div>
                                                            <div class="card-subtitle" style="margin-bottom: 8px;">
                                                                Description</div>
                                                            <div
                                                                style="background: var(--color-bg-muted); padding: 16px; border-radius: var(--radius-md); white-space: pre-wrap; line-height: 1.6;">
                                                                <%= complaint.getDescription() %>
                                                            </div>
                                                        </div>

                                                        <div
                                                            style="margin-top: 24px; padding-top: 20px; border-top: 1px solid var(--color-border);">
                                                            <a href="myComplaints" class="btn btn--secondary">← Back to
                                                                My
                                                                Complaints</a>
                                                        </div>
                                                    </div>
                                                    <% } %>
                                        </div>
                                    </main>
                                </div>

                                <script src="js/main.js"></script>
                            </body>

                            </html>