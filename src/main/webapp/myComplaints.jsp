<%@ page contentType="text/html;charset=UTF-8" %>
    <%@ page import="com.complaintsystem.model.Complaint" %>
        <%@ page import="java.util.List" %>
            <%@ include file="header.jsp" %>
                <% @SuppressWarnings("unchecked") List<Complaint> complaints = (List<Complaint>)
                        request.getAttribute("complaints"); %>

                        <div class="flex-between mb-6">
                            <h2 style="margin: 0;">
                                <%= "USER" .equals(userRole) ? "My Complaints" : "Assigned Complaints" %>
                            </h2>
                            <% if ("USER".equals(userRole)) { %>
                                <a href="registerComplaint.jsp" class="btn btn--primary">➕ Register New Complaint</a>
                                <% } %>
                        </div>

                        <% if (complaints==null || complaints.isEmpty()) { %>
                            <div class="card text-center py-8">
                                <div style="font-size: 48px; margin-bottom: 16px; opacity: 0.3;">📭</div>
                                <p class="text-muted">
                                    <%= "USER" .equals(userRole) ? "You haven't registered any complaints yet."
                                        : "No complaints assigned to you." %>
                                </p>
                                <% if ("USER".equals(userRole)) { %>
                                    <a href="registerComplaint.jsp" class="btn btn--primary mt-4">Register Your First
                                        Complaint</a>
                                    <% } %>
                            </div>
                            <% } else { %>
                                <div class="table-wrapper">
                                    <table class="table complaints-table">
                                        <thead>
                                            <tr>
                                                <th>ID</th>
                                                <th>Title</th>
                                                <th>Category</th>
                                                <th>Priority</th>
                                                <th>Status</th>
                                                <th>Level</th>
                                                <th>Assigned To</th>
                                                <th>Created At</th>
                                                <% if (!"USER".equals(userRole)) { %>
                                                    <th>Actions</th>
                                                    <% } %>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <% for (Complaint c : complaints) { String statusClass="" ; if
                                                ("OPEN".equals(c.getStatus())) { statusClass="badge--open" ; } else if
                                                ("IN_PROGRESS".equals(c.getStatus())) { statusClass="badge--in-progress"
                                                ; } else if ("ESCALATED".equals(c.getStatus())) {
                                                statusClass="badge--escalated" ; } else if
                                                ("RESOLVED".equals(c.getStatus())) { statusClass="badge--resolved" ; }
                                                else if ("CLOSED".equals(c.getStatus())) { statusClass="badge--closed" ;
                                                } %>
                                                <tr>
                                                    <td><a href="viewComplaint.jsp?id=<%= c.getId() %>">#<%= c.getId()
                                                                %></a></td>
                                                    <td><strong>
                                                            <%= c.getTitle() %>
                                                        </strong></td>
                                                    <td>
                                                        <%= c.getCategoryName() !=null ? c.getCategoryName() : "-" %>
                                                    </td>
                                                    <td><span class="badge badge--<%= c.getPriority().toLowerCase() %>">
                                                            <%= c.getPriority() %>
                                                        </span></td>
                                                    <td><span class="badge <%= statusClass %>">
                                                            <%= c.getStatus() %>
                                                        </span></td>
                                                    <td>L<%= c.getCurrentLevel() %>
                                                    </td>
                                                    <td>
                                                        <%= c.getAssignedToName() !=null ? c.getAssignedToName() : "-"
                                                            %>
                                                    </td>
                                                    <td>
                                                        <%= c.getCreatedAt() !=null ? c.getCreatedAt() : "-" %>
                                                    </td>
                                                    <% if (!"USER".equals(userRole)) { %>
                                                        <td>
                                                            <form action="updateStatus" method="post"
                                                                class="complaint-actions">
                                                                <input type="hidden" name="complaintId"
                                                                    value="<%= c.getId() %>" />
                                                                <select name="status" class="form-select"
                                                                    style="width: auto; padding: 4px 8px; font-size: 12px;">
                                                                    <option value="OPEN" <%="OPEN"
                                                                        .equals(c.getStatus()) ? "selected" : "" %>>OPEN
                                                                    </option>
                                                                    <option value="IN_PROGRESS" <%="IN_PROGRESS"
                                                                        .equals(c.getStatus()) ? "selected" : "" %>
                                                                        >IN_PROGRESS</option>
                                                                    <option value="RESOLVED" <%="RESOLVED"
                                                                        .equals(c.getStatus()) ? "selected" : "" %>
                                                                        >RESOLVED</option>
                                                                    <option value="CLOSED" <%="CLOSED"
                                                                        .equals(c.getStatus()) ? "selected" : "" %>
                                                                        >CLOSED</option>
                                                                </select>
                                                                <button type="submit"
                                                                    class="btn btn--primary btn--sm">Update</button>
                                                            </form>
                                                        </td>
                                                        <% } %>
                                                </tr>
                                                <% } %>
                                        </tbody>
                                    </table>
                                </div>
                                <% } %>
                                    <%@ include file="footer.jsp" %>