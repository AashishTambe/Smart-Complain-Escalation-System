<%@ page contentType="text/html;charset=UTF-8" %>
    <html>

    <head>
        <title>Smart Complaint Escalation System</title>
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
                            <a href="index.jsp" class="sidebar__link sidebar__link--active">
                                <span class="sidebar__link-icon">🏠</span>
                                <span>Home</span>
                            </a>
                        </li>
                        <li class="sidebar__item">
                            <a href="login.jsp" class="sidebar__link">
                                <span class="sidebar__link-icon">🔐</span>
                                <span>Login</span>
                            </a>
                        </li>
                        <li class="sidebar__item">
                            <a href="register.jsp" class="sidebar__link">
                                <span class="sidebar__link-icon">👤</span>
                                <span>User Registration</span>
                            </a>
                        </li>
                        <li class="sidebar__item">
                            <a href="help.jsp" class="sidebar__link">
                                <span class="sidebar__link-icon">❓</span>
                                <span>Help</span>
                            </a>
                        </li>
                        <li class="sidebar__item">
                            <a href="contact.jsp" class="sidebar__link">
                                <span class="sidebar__link-icon">📞</span>
                                <span>Contact</span>
                            </a>
                        </li>
                    </ul>
                </nav>
                <div class="sidebar__footer">
                    <small>© 2026 Complaint System</small>
                </div>
            </aside>

            <!-- Main Content -->
            <main class="app-main">
                <nav class="navbar">
                    <div class="navbar__left">
                        <button class="sidebar-toggle" aria-label="Toggle sidebar">☰</button>
                        <h1 class="navbar__title">Smart Complaint Escalation System</h1>
                    </div>
                    <div class="navbar__right">
                        <a href="login.jsp" class="btn btn--primary btn--sm">Login</a>
                    </div>
                </nav>

                <div class="app-content">
                    <div class="card">
                        <h2 style="margin-top: 0;">Welcome to the Complaint Portal</h2>
                        <p>Manage and track your complaints efficiently with our automated escalation system.</p>

                        <div style="margin-top: 24px;">
                            <a href="login.jsp" class="btn btn--primary">Login to Your Account</a>
                            <a href="register.jsp" class="btn btn--secondary" style="margin-left: 8px;">User
                                Registration</a>
                        </div>

                        <div style="margin-top: 32px; padding-top: 24px; border-top: 1px solid var(--color-border);">
                            <h3>Quick Links</h3>
                            <ul style="list-style: none; padding: 0;">
                                <li style="margin-bottom: 8px;">
                                    <a href="help.jsp">📖 How to Use This Portal</a>
                                </li>
                                <li style="margin-bottom: 8px;">
                                    <a href="contact.jsp">📞 Contact Support</a>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
            </main>
        </div>

        <script src="js/main.js"></script>
    </body>

    </html>