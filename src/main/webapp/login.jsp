<%@ page contentType="text/html;charset=UTF-8" %>
    <%@ include file="header.jsp" %>

        <div class="form-card" style="max-width: 400px; margin: 40px auto;">
            <h2 style="margin-top: 0; margin-bottom: 24px;">Login to Your Account</h2>

            <form action="${pageContext.request.contextPath}/login" method="post">
                <div class="form-group">
                    <label class="form-label" for="identifier">Email or Phone Number</label>
                    <input type="text" id="identifier" name="identifier" class="form-control" required
                        placeholder="Enter your email or phone number" />
                </div>

                <div class="form-group">
                    <label class="form-label" for="password">Password</label>
                    <input type="password" id="password" name="password" class="form-control" required
                        placeholder="Enter your password" />
                </div>

                <% if (request.getAttribute("error") !=null) { %>
                    <div class="alert alert--danger">
                        <span>⚠️</span>
                        <span>
                            <%= request.getAttribute("error") %>
                        </span>
                    </div>
                    <% } %>

                        <button type="submit" class="btn btn--primary"
                            style="width: 100%; margin-top: 8px;">Login</button>
            </form>

            <p style="margin-top: 20px; text-align: center; font-size: 13px;">
                Don't have an account? <a href="register.jsp">Register here</a>
            </p>
            <p style="margin-top: 10px; text-align: center; font-size: 13px;">
                <a href="index.jsp">← Back to Home</a>
            </p>
        </div>

        <%@ include file="footer.jsp" %>