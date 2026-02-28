<%@ page contentType="text/html;charset=UTF-8" %>
    <%@ include file="header.jsp" %>

        <div class="card">
            <h2 style="margin-top: 0;">Escalation Rules Management</h2>
            <p>This page is for managing escalation rules. Currently, rules are defined in the database.</p>

            <div
                style="margin-top: 24px; padding: 16px; background: var(--color-bg-muted); border-radius: var(--radius-md); border-left: 4px solid var(--color-primary);">
                <p style="margin: 0; font-weight: 500;">Note:</p>
                <p style="margin: 8px 0 0 0;">Automatic escalation logic is handled by the <code>EscalationJob</code>
                    which runs in the background. You can monitor escalated complaints in the Dashboard.</p>
            </div>
        </div>

        <%@ include file="footer.jsp" %>