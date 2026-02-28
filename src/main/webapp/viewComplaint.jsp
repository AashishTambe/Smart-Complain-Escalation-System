<%@ page contentType="text/html;charset=UTF-8" %>
    <%@ page import="com.complaintsystem.dao.ComplaintDAO" %>
        <%@ page import="com.complaintsystem.model.Complaint" %>

            <%@ include file="header.jsp" %>

                <% String idParam=request.getParameter("id"); Complaint complaint=null; if (idParam !=null) { try { int
                    id=Integer.parseInt(idParam); ComplaintDAO dao=new ComplaintDAO(); complaint=dao.getById(id); }
                    catch (Exception e) { e.printStackTrace(); } } String statusBadgeClass="" ; if (complaint !=null) {
                    if ("OPEN".equals(complaint.getStatus())) statusBadgeClass="badge--open" ; else if
                    ("IN_PROGRESS".equals(complaint.getStatus())) statusBadgeClass="badge--in-progress" ; else if
                    ("ESCALATED".equals(complaint.getStatus())) statusBadgeClass="badge--escalated" ; else if
                    ("RESOLVED".equals(complaint.getStatus())) statusBadgeClass="badge--resolved" ; else if
                    ("CLOSED".equals(complaint.getStatus())) statusBadgeClass="badge--closed" ; } %>

                    <div class="flex-between mb-6">
                        <h2 style="margin: 0;">Complaint Details</h2>
                        <a href="myComplaints" class="btn btn--secondary btn--sm">← Back to List</a>
                    </div>

                    <% if (complaint==null) { %>
                        <div class="card text-center py-8">
                            <div style="font-size: 48px; margin-bottom: 16px; opacity: 0.3;">⚠️</div>
                            <p class="text-muted">Complaint not found or invalid ID.</p>
                        </div>
                        <% } else { %>
                            <div class="card">
                                <div
                                    style="display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 24px;">
                                    <div>
                                        <h2 style="margin-top: 0;">
                                            <%= complaint.getTitle() %>
                                        </h2>
                                        <p class="text-muted">Complaint ID: #<%= complaint.getId() %>
                                        </p>
                                    </div>
                                    <div>
                                        <span class="badge <%= statusBadgeClass %>"
                                            style="font-size: 13px; padding: 6px 12px;">
                                            <%= complaint.getStatus() %>
                                        </span>
                                    </div>
                                </div>

                                <div
                                    style="display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: 20px; margin-bottom: 24px;">
                                    <div>
                                        <div class="card-subtitle">Category</div>
                                        <div style="font-weight: 500; margin-top: 4px;">
                                            <%= complaint.getCategoryName() !=null ? complaint.getCategoryName() : "-"
                                                %>
                                        </div>
                                    </div>
                                    <div>
                                        <div class="card-subtitle">Priority</div>
                                        <div style="margin-top: 4px;">
                                            <span class="badge badge--<%= complaint.getPriority().toLowerCase() %>">
                                                <%= complaint.getPriority() %>
                                            </span>
                                        </div>
                                    </div>
                                    <div>
                                        <div class="card-subtitle">Escalation Level</div>
                                        <div style="font-weight: 500; margin-top: 4px;">Level <%=
                                                complaint.getCurrentLevel() %>
                                        </div>
                                    </div>
                                    <div>
                                        <div class="card-subtitle">Assigned To</div>
                                        <div style="font-weight: 500; margin-top: 4px;">
                                            <%= complaint.getAssignedToName() !=null ? complaint.getAssignedToName()
                                                : "Not assigned" %>
                                        </div>
                                    </div>
                                </div>

                                <div style="margin-bottom: 24px;">
                                    <div class="card-subtitle">Created At</div>
                                    <div style="margin-top: 4px;">
                                        <%= complaint.getCreatedAt() !=null ? complaint.getCreatedAt() : "-" %>
                                    </div>
                                </div>

                                <div>
                                    <div class="card-subtitle" style="margin-bottom: 8px;">Description</div>
                                    <div
                                        style="background: var(--color-bg-muted); padding: 16px; border-radius: var(--radius-md); white-space: pre-wrap; line-height: 1.6;">
                                        <%= complaint.getDescription() %>
                                    </div>
                                </div>
                            </div>
                            <% } %>

                                <%@ include file="footer.jsp" %>