<%@ page contentType="text/html;charset=UTF-8" %>
    <html>

    <head>
        <title>Register - Smart Complaint Escalation System</title>
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
                            <a href="login.jsp" class="sidebar__link">
                                <span class="sidebar__link-icon">🔐</span>
                                <span>Login</span>
                            </a>
                        </li>
                        <li class="sidebar__item">
                            <a href="register.jsp" class="sidebar__link sidebar__link--active">
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
                    </ul>
                </nav>
            </aside>

            <!-- Main Content -->
            <main class="app-main">
                <nav class="navbar">
                    <div class="navbar__left">
                        <button class="sidebar-toggle" aria-label="Toggle sidebar">☰</button>
                        <h1 class="navbar__title">Register</h1>
                    </div>
                    <div class="navbar__right">
                        <a href="index.jsp" class="btn btn--secondary btn--sm">Back to Home</a>
                    </div>
                </nav>

                <div class="app-content">
                    <div class="form-card" style="max-width: 400px; margin: 40px auto;">
                        <h2 style="margin-top: 0; margin-bottom: 24px;">Create an Account</h2>

                        <form action="register" method="post">
                            <div class="form-group">
                                <label class="form-label" for="name">Full Name</label>
                                <input type="text" id="name" name="name" class="form-control" required
                                    placeholder="Enter your full name" />
                            </div>

                            <div class="form-group">
                                <label class="form-label" for="email">Email Address</label>
                                <input type="email" id="email" name="email" class="form-control" required
                                    placeholder="your.email@example.com" />
                            </div>

                            <div class="form-group">
                                <label class="form-label" for="password">Password</label>
                                <input type="password" id="password" name="password" class="form-control" required
                                    placeholder="Create a password" />
                            </div>

                            <% if (request.getAttribute("error") !=null) { %>
                                <div class="alert alert--danger">
                                    <span>⚠️</span>
                                    <span>${error}</span>
                                </div>
                                <% } %>

                                    <button type="submit" class="btn btn--primary"
                                        style="width: 100%; margin-top: 8px;">Register</button>
                        </form>

                        <p style="margin-top: 20px; text-align: center; font-size: 13px;">
                            Already have an account? <a href="login.jsp">Login here</a>
                        </p>
                        <p style="margin-top: 10px; text-align: center; font-size: 13px;">
                            <a href="index.jsp">← Back to Home</a>
                        </p>
                    </div>
                </div>
            </main>
        </div>

        <script src="js/main.js"></script>
    </body>

    </html>