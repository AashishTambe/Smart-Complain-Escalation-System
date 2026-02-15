document.addEventListener("DOMContentLoaded", function () {
    setupLoginForm();
    setupComplaintForm();
    setupStatusUpdateForms();
    setupSidebarToggle();
    setupDarkMode();
});

function setupLoginForm() {
    var form = document.querySelector('form[action="login"]');
    if (!form) return;

    form.addEventListener("submit", function (e) {
        var email = form.querySelector('input[name="email"]');
        var password = form.querySelector('input[name="password"]');
        if (!email || !password) return;

        if (!email.value.trim() || !password.value.trim()) {
            e.preventDefault();
            alert("Please enter both email and password.");
        }
    });
}

function setupComplaintForm() {
    var form = document.querySelector('form[action="registerComplaint"]');
    if (!form) return;

    form.addEventListener("submit", function (e) {
        var title = form.querySelector('input[name="title"]');
        var description = form.querySelector('textarea[name="description"]');
        if (!title || !description) return;

        if (title.value.trim().length < 5) {
            e.preventDefault();
            alert("Title should be at least 5 characters long.");
            title.focus();
            return;
        }
        if (description.value.trim().length < 10) {
            e.preventDefault();
            alert("Description should be at least 10 characters long.");
            description.focus();
        }
    });
}

function setupStatusUpdateForms() {
    var forms = document.querySelectorAll('form[action="updateStatus"]');
    if (!forms || forms.length === 0) return;

    forms.forEach(function (form) {
        form.addEventListener("submit", function (e) {
            var select = form.querySelector('select[name="status"]');
            if (!select) return;
            var status = select.value;
            var ok = confirm("Change complaint status to \"" + status + "\"?");
            if (!ok) {
                e.preventDefault();
            }
        });
    });
}

function setupSidebarToggle() {
    var sidebarToggle = document.querySelector('.sidebar-toggle');
    var sidebar = document.querySelector('.sidebar');
    
    if (!sidebarToggle || !sidebar) return;

    sidebarToggle.addEventListener('click', function() {
        sidebar.classList.toggle('sidebar--open');
    });

    // Close sidebar when clicking outside on mobile
    document.addEventListener('click', function(e) {
        if (window.innerWidth <= 960) {
            if (sidebar && sidebar.classList.contains('sidebar--open')) {
                if (!sidebar.contains(e.target) && !sidebarToggle.contains(e.target)) {
                    sidebar.classList.remove('sidebar--open');
                }
            }
        }
    });
}

function setupDarkMode() {
    // Check for saved theme preference or default to light mode
    var darkMode = localStorage.getItem('darkMode') === 'true';
    
    if (darkMode) {
        document.body.classList.add('dark-mode');
    }

    // Toggle dark mode (can be triggered by a button if you add one)
    window.toggleDarkMode = function() {
        document.body.classList.toggle('dark-mode');
        var isDark = document.body.classList.contains('dark-mode');
        localStorage.setItem('darkMode', isDark);
    };
}

