<%@ page contentType="text/html;charset=UTF-8" %>
    <%@ include file="header.jsp" %>
        <div class="card">
            <h2 style="margin-top: 0;">Welcome to the Complaint Portal</h2>
            <p>Manage and track your complaints efficiently with our automated escalation system.</p>
            <div style="margin-top: 24px;">
                <% if (userId==null) { %>
                    <a href="login.jsp" class="btn btn--primary">Login to Your Account</a>
                    <a href="register.jsp" class="btn btn--secondary" style="margin-left: 8px;">User Registration</a>
                    <% } else { %>
                        <a href="myComplaints" class="btn btn--primary">View My Complaints</a>
                        <a href="registerComplaint.jsp" class="btn btn--secondary" style="margin-left: 8px;">Register
                            New Complaint</a>
                        <% } %>
            </div>
            <div style="margin-top: 32px; padding-top: 24px; border-top: 1px solid var(--color-border);">
                <h3>Quick Links</h3>
                <ul style="list-style: none; padding: 0;">
                    <li style="margin-bottom: 8px;"><a href="help.jsp">📖 How to Use This Portal</a></li>
                    <li style="margin-bottom: 8px;"><a href="contact.jsp">📞 Contact Support</a></li>
                </ul>
            </div>
        </div>
        <%@ include file="footer.jsp" %>