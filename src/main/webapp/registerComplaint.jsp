<%@ page contentType="text/html;charset=UTF-8" %>
    <% String userName=(String) session.getAttribute("userName"); String userRole=(String)
        session.getAttribute("userRole"); if (userName==null) { response.sendRedirect("login.jsp"); return; } %>
        <html>

        <head>
            <title>Register Complaint</title>
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
                            <% if (userName !=null) { %>
                                <li class="sidebar__item">
                                    <a href="myComplaints" class="sidebar__link">
                                        <span class="sidebar__link-icon">📋</span>
                                        <span>My Complaints</span>
                                    </a>
                                </li>
                                <% } %>
                                    <li class="sidebar__item">
                                        <a href="registerComplaint.jsp" class="sidebar__link sidebar__link--active">
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
                            <h1 class="navbar__title">Register New Complaint</h1>
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
                        <div class="form-card" style="max-width: 600px; margin: 20px auto;">
                            <h2 style="margin-top: 0;">Register a New Complaint</h2>
                            <p class="text-muted">Fill in the details below to submit your complaint. Our system will
                                automatically assign it and track its progress.</p>

                            <% if (request.getAttribute("error") !=null) { %>
                                <div class="alert alert--danger">
                                    <span>⚠️</span>
                                    <span>${error}</span>
                                </div>
                                <% } %>

                                    <form action="registerComplaint" method="post">
                                        <div class="form-group">
                                            <label class="form-label" for="categoryId">Category</label>
                                            <select id="categoryId" name="categoryId" class="form-select" required>
                                                <option value="">Select a category</option>
                                                <option value="1">IT</option>
                                                <option value="2">Service</option>
                                                <option value="3">Infrastructure</option>
                                            </select>
                                        </div>

                                        <div class="form-group">
                                            <label class="form-label" for="priority">Priority</label>
                                            <select id="priority" name="priority" class="form-select" required>
                                                <option value="">Select priority</option>
                                                <option value="LOW">Low</option>
                                                <option value="MEDIUM">Medium</option>
                                                <option value="HIGH">High</option>
                                            </select>
                                        </div>

                                        <div class="form-group">
                                            <label class="form-label" for="title">Title</label>
                                            <input type="text" id="title" name="title" class="form-control" required
                                                placeholder="Brief description of your complaint" />
                                        </div>

                                        <div class="form-group">
                                            <label class="form-label" for="description">Description</label>
                                            <textarea id="description" name="description" class="form-textarea" required
                                                placeholder="Provide detailed information about your complaint..."></textarea>
                                        </div>

                                        <div style="display: flex; gap: 8px; margin-top: 24px;">
                                            <button type="submit" class="btn btn--primary">Submit Complaint</button>
                                            <a href="<%= userName != null ? " myComplaints" : "index.jsp" %>" class="btn
                                                btn--secondary">Cancel</a>
                                        </div>
                                    </form>
                        </div>
                    </div>
                </main>
            </div>

            <script src="js/main.js"></script>
        </body>

        </html>