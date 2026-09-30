package minicode.com.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import minicode.com.dto.AdminLoginRequest;
import minicode.com.entity.AdminUser;
import minicode.com.service.AdminService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }
    @PostMapping("/login")
    public AdminUser login(@Valid @RequestBody AdminLoginRequest request, HttpServletRequest httpRequest) {
        AdminUser admin = adminService.login(request);
        var session = httpRequest.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        session = httpRequest.getSession(true);
        session.setAttribute("admin_user_id", admin.getAdminId());
        return admin;
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest httpRequest) {
        var session = httpRequest.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }
}
