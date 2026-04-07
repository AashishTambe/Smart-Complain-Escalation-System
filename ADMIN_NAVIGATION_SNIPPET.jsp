<!-- 
    Add this snippet to your admin dashboard (e.g., in staffDashboard.jsp or adminDashboard.jsp)
    This adds a link to the Escalation Settings panel in the admin navigation
-->

<!-- Inside your admin menu/navigation section, add: -->

<% 
    User user = (User) session.getAttribute("user");
    if (user != null && "ADMIN".equals(user.getRole())) {
%>
    <div class="admin-menu-section">
        <h3>System Configuration</h3>
        <ul>
            <li>
                <a href="adminEscalationSettings" class="menu-link">
                    <span class="icon">⚙️</span> 
                    <span class="label">Escalation Settings</span>
                </a>
            </li>
        </ul>
    </div>
<% 
    } 
%>

<!-- Optional: Add some CSS styling -->
<style>
    .admin-menu-section {
        background: #f8f9fa;
        padding: 15px;
        margin: 10px 0;
        border-left: 4px solid #667eea;
        border-radius: 5px;
    }

    .admin-menu-section h3 {
        color: #333;
        font-size: 14px;
        text-transform: uppercase;
        letter-spacing: 1px;
        margin-bottom: 10px;
    }

    .admin-menu-section ul {
        list-style: none;
        padding: 0;
    }

    .admin-menu-section li {
        margin: 8px 0;
    }

    .menu-link {
        display: flex;
        align-items: center;
        padding: 10px 12px;
        color: #333;
        text-decoration: none;
        border-radius: 5px;
        transition: all 0.3s;
    }

    .menu-link:hover {
        background: white;
        color: #667eea;
        box-shadow: 0 2px 8px rgba(0,0,0,0.1);
    }

    .menu-link .icon {
        margin-right: 10px;
        font-size: 18px;
    }

    .menu-link .label {
        font-weight: 500;
    }
</style>
