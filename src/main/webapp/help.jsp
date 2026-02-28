<%@ page contentType="text/html;charset=UTF-8" %>
    <%@ include file="header.jsp" %>

        <div class="card">
            <h2 style="margin-top: 0;">How to Use This Portal</h2>

            <section style="margin-bottom: 24px;">
                <h3>Registering a Complaint</h3>
                <ol style="line-height: 1.6;">
                    <li>Login using your registered email and password.</li>
                    <li>Go to <strong>New Complaint</strong> in the sidebar.</li>
                    <li>Select the correct category and priority (Low / Medium / High).</li>
                    <li>Describe your issue clearly and submit the complaint.</li>
                </ol>
            </section>

            <section style="margin-bottom: 24px;">
                <h3>Tracking Status</h3>
                <p>Open <strong>My Complaints</strong> after logging in to see:</p>
                <ul style="line-height: 1.6;">
                    <li><strong>Current status</strong> (Open, In Progress, Escalated, Resolved, Closed)</li>
                    <li><strong>Escalation level</strong> (L1, L2, Admin)</li>
                    <li><strong>Assigned officer</strong> (if any)</li>
                </ul>
            </section>

            <section>
                <h3>Escalation Logic</h3>
                <p style="line-height: 1.6;">
                    If a complaint is not resolved within its SLA time and is not marked as Resolved/Closed,
                    it is automatically escalated to the next level (e.g. Level 2, then Admin).
                </p>
            </section>
        </div>

        <%@ include file="footer.jsp" %>