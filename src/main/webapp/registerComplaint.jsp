<%@ page contentType="text/html;charset=UTF-8" %>
    <% if (session.getAttribute("userId")==null) { response.sendRedirect("login.jsp"); return; } %>

        <%@ include file="header.jsp" %>

            <div class="form-card" style="max-width: 600px; margin: 20px auto;">
                <h2 style="margin-top: 0;">Register a New Complaint</h2>
                <p class="text-muted">Fill in the details below to submit your complaint. Our system will automatically
                    assign it and track its progress.</p>

                <% if (request.getAttribute("error") !=null) { %>
                    <div class="alert alert--danger">
                        <span>⚠️</span>
                        <span>${error}</span>
                    </div>
                    <% } %>

                        <form action="registerComplaint" method="post">
                            <div class="form-group">
                                <label class="form-label" for="categoryId">Category</label>
                                <select id="categoryId" name="categoryId" class="form-select" required>
                                    <option value="">Select a category</option>
                                    <option value="1">IT</option>
                                    <option value="2">Service</option>
                                    <option value="3">Infrastructure</option>
                                </select>
                            </div>

                            <div class="form-group">
                                <label class="form-label" for="priority">Priority</label>
                                <select id="priority" name="priority" class="form-select" required>
                                    <option value="">Select priority</option>
                                    <option value="LOW">Low</option>
                                    <option value="MEDIUM">Medium</option>
                                    <option value="HIGH">High</option>
                                </select>
                            </div>

                            <div class="form-group">
                                <label class="form-label" for="title">Title</label>
                                <input type="text" id="title" name="title" class="form-control" required
                                    placeholder="Brief description of your complaint" />
                            </div>

                            <div class="form-group">
                                <label class="form-label" for="description">Description</label>
                                <textarea id="description" name="description" class="form-textarea" required
                                    placeholder="Provide detailed information about your complaint..."></textarea>
                            </div>

                            <div style="display: flex; gap: 8px; margin-top: 24px;">
                                <button type="submit" class="btn btn--primary">Submit Complaint</button>
                                <a href="myComplaints" class="btn btn--secondary">Cancel</a>
                            </div>
                        </form>
            </div>

            <%@ include file="footer.jsp" %>