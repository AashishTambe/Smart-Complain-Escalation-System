<%@ page contentType="text/html;charset=UTF-8" %>
    <%@ page import="com.complaintsystem.model.Complaint" %>
        <%@ page import="com.complaintsystem.model.Department" %>
            <%@ page import="java.util.List" %>
                <%@ include file="header.jsp" %>
                    <% @SuppressWarnings("unchecked") List<Complaint> complaintsList = (List<Complaint>)
                            request.getAttribute("complaints"); %>
                            <% @SuppressWarnings("unchecked") List<Department> departments = (List<Department>)
                                    request.getAttribute("departments"); %>
                                    <% Boolean isAdminFlag=(Boolean) request.getAttribute("isAdmin"); %>
                                        <% if (isAdminFlag==null) { isAdminFlag=false; } %>
                                            <% String statusFilter=(String) request.getAttribute("statusFilter"); %>
                                                <% String priorityFilter=(String)
                                                    request.getAttribute("priorityFilter"); %>
                                                    <% String deptFilter=(String) request.getAttribute("deptFilter"); %>

                                                        <h2 style="margin-top: 0;">
                                                            <%= isAdminFlag ? "Complaint Management"
                                                                : "Assigned Complaints" %>
                                                        </h2>

                                                        <% if (isAdminFlag) { %>
                                                            <div class="card" style="margin-bottom: 20px;">
                                                                <form method="get" action="staffDashboard"
                                                                    class="d-flex"
                                                                    style="flex-wrap: wrap; gap: 12px; align-items: flex-end;">
                                                                    <div class="form-group"
                                                                        style="flex: 1; min-width: 150px; margin-bottom: 0;">
                                                                        <label class="form-label">Status</label>
                                                                        <select name="status" class="form-select">
                                                                            <option value="">All Status</option>
                                                                            <option value="OPEN" <%="OPEN"
                                                                                .equals(statusFilter) ? "selected" : ""
                                                                                %>>OPEN</option>
                                                                            <option value="IN_PROGRESS" <%="IN_PROGRESS"
                                                                                .equals(statusFilter) ? "selected" : ""
                                                                                %>>IN_PROGRESS</option>
                                                                            <option value="ESCALATED" <%="ESCALATED"
                                                                                .equals(statusFilter) ? "selected" : ""
                                                                                %>>ESCALATED</option>
                                                                            <option value="RESOLVED" <%="RESOLVED"
                                                                                .equals(statusFilter) ? "selected" : ""
                                                                                %>>RESOLVED</option>
                                                                            <option value="CLOSED" <%="CLOSED"
                                                                                .equals(statusFilter) ? "selected" : ""
                                                                                %>>CLOSED</option>
                                                                        </select>
                                                                    </div>
                                                                    <div class="form-group"
                                                                        style="flex: 1; min-width: 150px; margin-bottom: 0;">
                                                                        <label class="form-label">Priority</label>
                                                                        <select name="priority" class="form-select">
                                                                            <option value="">All Priority</option>
                                                                            <option value="LOW" <%="LOW"
                                                                                .equals(priorityFilter) ? "selected"
                                                                                : "" %>>LOW</option>
                                                                            <option value="MEDIUM" <%="MEDIUM"
                                                                                .equals(priorityFilter) ? "selected"
                                                                                : "" %>>MEDIUM</option>
                                                                            <option value="HIGH" <%="HIGH"
                                                                                .equals(priorityFilter) ? "selected"
                                                                                : "" %>>HIGH</option>
                                                                        </select>
                                                                    </div>
                                                                    <div class="form-group"
                                                                        style="flex: 1; min-width: 150px; margin-bottom: 0;">
                                                                        <label class="form-label">Department</label>
                                                                        <select name="departmentId" class="form-select">
                                                                            <option value="">All Departments</option>
                                                                            <% if (departments !=null) { for (Department
                                                                                d : departments) { %>
                                                                                <option value="<%= d.getId() %>"
                                                                                    <%=String.valueOf(d.getId()).equals(deptFilter)
                                                                                    ? "selected" : "" %>><%= d.getName()
                                                                                        %>
                                                                                </option>
                                                                                <% } } %>
                                                                        </select>
                                                                    </div>
                                                                    <button type="submit" class="btn btn--primary">🔍
                                                                        Filter</button>
                                                                </form>
                                                            </div>
                                                            <% } %>

                                                                <% if (complaintsList==null || complaintsList.isEmpty())
                                                                    { %>
                                                                    <div class="card text-center py-8">
                                                                        <div
                                                                            style="font-size: 48px; margin-bottom: 16px; opacity: 0.3;">
                                                                            📋</div>
                                                                        <p class="text-muted">
                                                                            <%= isAdminFlag
                                                                                ? "No complaints found matching your filters."
                                                                                : "No complaints assigned to you." %>
                                                                        </p>
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
                                                                                        <th>Actions</th>
                                                                                    </tr>
                                                                                </thead>
                                                                                <tbody>
                                                                                    <% for (Complaint c :
                                                                                        complaintsList) { String
                                                                                        statusClass="" ; if
                                                                                        ("OPEN".equals(c.getStatus())) {
                                                                                        statusClass="badge--open" ; }
                                                                                        else if
                                                                                        ("IN_PROGRESS".equals(c.getStatus()))
                                                                                        {
                                                                                        statusClass="badge--in-progress"
                                                                                        ; } else if
                                                                                        ("ESCALATED".equals(c.getStatus()))
                                                                                        { statusClass="badge--escalated"
                                                                                        ; } else if
                                                                                        ("RESOLVED".equals(c.getStatus()))
                                                                                        { statusClass="badge--resolved"
                                                                                        ; } else if
                                                                                        ("CLOSED".equals(c.getStatus()))
                                                                                        { statusClass="badge--closed" ;
                                                                                        } %>
                                                                                        <tr>
                                                                                            <td><a
                                                                                                    href="viewComplaint.jsp?id=<%= c.getId() %>">#
                                                                                                    <%= c.getId() %></a>
                                                                                            </td>
                                                                                            <td><strong>
                                                                                                    <%= c.getTitle() %>
                                                                                                </strong></td>
                                                                                            <td>
                                                                                                <%= c.getCategoryName()
                                                                                                    !=null ?
                                                                                                    c.getCategoryName()
                                                                                                    : "-" %>
                                                                                            </td>
                                                                                            <td><span
                                                                                                    class="badge badge--<%= c.getPriority().toLowerCase() %>">
                                                                                                    <%= c.getPriority()
                                                                                                        %>
                                                                                                </span></td>
                                                                                            <td><span
                                                                                                    class="badge <%= statusClass %>">
                                                                                                    <%= c.getStatus() %>
                                                                                                </span></td>
                                                                                            <td>L<%= c.getCurrentLevel()
                                                                                                    %>
                                                                                            </td>
                                                                                            <td>
                                                                                                <%= c.getAssignedToName()
                                                                                                    !=null ?
                                                                                                    c.getAssignedToName()
                                                                                                    : "-" %>
                                                                                            </td>
                                                                                            <td>
                                                                                                <%= c.getCreatedAt()
                                                                                                    !=null ?
                                                                                                    c.getCreatedAt()
                                                                                                    : "-" %>
                                                                                            </td>
                                                                                            <td>
                                                                                                <form
                                                                                                    action="updateStatus"
                                                                                                    method="post"
                                                                                                    class="complaint-actions">
                                                                                                    <input type="hidden"
                                                                                                        name="complaintId"
                                                                                                        value="<%= c.getId() %>" />
                                                                                                    <select
                                                                                                        name="status"
                                                                                                        class="form-select"
                                                                                                        style="width: auto; padding: 4px 8px; font-size: 12px;">
                                                                                                        <option
                                                                                                            value="OPEN"
                                                                                                            <%="OPEN"
                                                                                                            .equals(c.getStatus())
                                                                                                            ? "selected"
                                                                                                            : "" %>>OPEN
                                                                                                        </option>
                                                                                                        <option
                                                                                                            value="IN_PROGRESS"
                                                                                                            <%="IN_PROGRESS"
                                                                                                            .equals(c.getStatus())
                                                                                                            ? "selected"
                                                                                                            : "" %>
                                                                                                            >IN_PROGRESS
                                                                                                        </option>
                                                                                                        <option
                                                                                                            value="RESOLVED"
                                                                                                            <%="RESOLVED"
                                                                                                            .equals(c.getStatus())
                                                                                                            ? "selected"
                                                                                                            : "" %>
                                                                                                            >RESOLVED
                                                                                                        </option>
                                                                                                        <option
                                                                                                            value="CLOSED"
                                                                                                            <%="CLOSED"
                                                                                                            .equals(c.getStatus())
                                                                                                            ? "selected"
                                                                                                            : "" %>
                                                                                                            >CLOSED
                                                                                                        </option>
                                                                                                    </select>
                                                                                                    <button
                                                                                                        type="submit"
                                                                                                        class="btn btn--primary btn--sm">Update</button>
                                                                                                </form>
                                                                                            </td>
                                                                                        </tr>
                                                                                        <% } %>
                                                                                </tbody>
                                                                            </table>
                                                                        </div>
                                                                        <% } %>
                                                                            <%@ include file="footer.jsp" %>