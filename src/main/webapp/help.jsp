<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Help - Smart Complaint Escalation System</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">

</head>
<body>
<header>
    <h1>Smart Complaint Escalation System</h1>
</header>
<div class="container">
    <h2>How to Use This Portal</h2>

    <h3>Registering a Complaint</h3>
    <ol>
        <li>Login using your registered email and password.</li>
        <li>Go to “Register a Complaint”.</li>
        <li>Select the correct category and priority (Low / Medium / High).</li>
        <li>Describe your issue clearly and submit the complaint.</li>
    </ol>

    <h3>Tracking Status</h3>
    <p>
        Open <b>My Complaints</b> after logging in to see:
    </p>
    <ul>
        <li>Current status (Open, In Progress, Escalated, Resolved, Closed)</li>
        <li>Escalation level (L1, L2, Admin)</li>
        <li>Assigned officer (if any)</li>
    </ul>

    <h3>Escalation Logic</h3>
    <p>
        If a complaint is not resolved within its SLA time and is not marked as Resolved/Closed,
        it is automatically escalated to the next level (e.g. Level 2, then Admin).
    </p>

    <p><a href="index.jsp">Back to Home</a></p>
</div>

</body>
</html>

