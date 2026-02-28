<%@ page contentType="text/html;charset=UTF-8" %>
    <%@ include file="header.jsp" %>

        <div class="card">
            <h2 style="margin-top: 0;">Contact & Support</h2>

            <p>If you face any issue with the portal or escalation system, please reach out:</p>

            <div
                style="display: grid; grid-template-columns: repeat(auto-fit, minmax(250px, 1fr)); gap: 24px; margin: 24px 0;">
                <div style="padding: 16px; background: var(--color-bg-muted); border-radius: var(--radius-md);">
                    <div style="font-weight: 600; margin-bottom: 8px;">📧 Email</div>
                    <div>support@example.com</div>
                </div>
                <div style="padding: 16px; background: var(--color-bg-muted); border-radius: var(--radius-md);">
                    <div style="font-weight: 600; margin-bottom: 8px;">📞 Phone</div>
                    <div>+91-00000-00000</div>
                </div>
                <div style="padding: 16px; background: var(--color-bg-muted); border-radius: var(--radius-md);">
                    <div style="font-weight: 600; margin-bottom: 8px;">⏰ Office Hours</div>
                    <div>Mon–Fri, 9:00 AM – 6:00 PM</div>
                </div>
            </div>

            <p class="text-muted" style="font-style: italic;">
                You can also raise a support complaint using the "New Complaint" page and choose the appropriate
                category.
            </p>
        </div>

        <%@ include file="footer.jsp" %>