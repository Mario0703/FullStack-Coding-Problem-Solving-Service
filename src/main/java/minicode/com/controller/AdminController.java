package minicode.com.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import minicode.com.dto.AdminLoginRequest;
import minicode.com.dto.LoginRequest;
import minicode.com.entity.AdminUser;
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
    public AdminUser login(@Valid @RequestBody AdminLoginRequest request, HttpServletRequest httpRequest, HttpSession httpSession) {

        HttpSession session = httpRequest.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        session.setAttribute("admin_id", request.getAdminUserName());

        return adminService.login(request);
    }
}
