<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Escalation Panel</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
</head>
<body>
    <jsp:include page="header.jsp" />

    <div class="container mt-5">
        <h2>Escalation Rules Management</h2>

        <p>This page is for managing escalation rules. Currently, rules are defined in the database.</p>

        <!-- Add form or table to view/edit rules if needed -->
    </div>

    <jsp:include page="footer.jsp" />
</body>
</html>