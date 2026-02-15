<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>CSS Test Page</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/styles.css" />
</head>
<body>
<div class="app-shell">
    <main class="app-main">
        <nav class="navbar">
            <div class="navbar__left">
                <h1 class="navbar__title">CSS Test</h1>
            </div>
        </nav>
        <div class="app-content">
            <div class="card">
                <h2>CSS Loading Test</h2>
                <p>If you see styled content below, CSS is working:</p>
                
                <div style="margin: 20px 0;">
                    <button class="btn btn--primary">Primary Button</button>
                    <button class="btn btn--secondary">Secondary Button</button>
                    <button class="btn btn--danger">Danger Button</button>
                </div>
                
                <div style="margin: 20px 0;">
                    <span class="badge badge--open">OPEN</span>
                    <span class="badge badge--resolved">RESOLVED</span>
                    <span class="badge badge--pending">PENDING</span>
                </div>
                
                <div class="alert alert--success">Success alert - CSS is working!</div>
                
                <p><strong>CSS Path:</strong> ${pageContext.request.contextPath}/css/styles.css</p>
                <p><strong>Expected URL:</strong> http://localhost:8080/ComplaintSystem/css/styles.css</p>
            </div>
        </div>
    </main>
</div>
</body>
</html>
