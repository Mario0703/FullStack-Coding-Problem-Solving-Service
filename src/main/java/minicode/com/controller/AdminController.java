package minicode.com.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import minicode.com.dto.AdminLoginRequest;
import minicode.com.entity.User;
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
    public User login(@Valid @RequestBody AdminLoginRequest request, HttpServletRequest httpRequest) {
        User admin = adminService.login(request);
        var session = httpRequest.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        session = httpRequest.getSession(true);
        session.setAttribute("user_id", admin.getUserId());
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
