<%@ page contentType="text/html;charset=UTF-8" %>
    <%@ include file="header.jsp" %>
        <div class="card card--auth">
            <h2 class="text-center mb-6">User Registration</h2>
            <% String error=request.getParameter("error"); %>
                <% if (error !=null) { %>
                    <div class="alert alert--danger mb-4">Registration failed. Please try again.</div>
                    <% } %>
                        <form action="register" method="post">
                            <div class="form-group">
                                <label class="form-label">Full Name</label>
                                <input type="text" name="name" class="form-control" required
                                    placeholder="Enter your name">
                            </div>
                            <div class="form-group">
                                <label class="form-label">Email Address</label>
                                <input type="email" name="email" class="form-control" required
                                    placeholder="name@example.com">
                            </div>
                            <div class="form-group">
                                <label class="form-label">Password</label>
                                <input type="password" name="password" class="form-control" required
                                    placeholder="Create a password">
                            </div>
                            <div class="form-group">
                                <label class="form-label">Phone Number</label>
                                <input type="tel" name="phone" class="form-control" required
                                    placeholder="Enter phone number">
                            </div>
                            <button type="submit" class="btn btn--primary btn--block">Create Account</button>
                        </form>
                        <div class="text-center mt-6">
                            <p class="text-muted">Already have an account? <a href="login.jsp">Login here</a></p>
                        </div>
        </div>
        <%@ include file="footer.jsp" %>