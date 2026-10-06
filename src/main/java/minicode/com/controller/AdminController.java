package minicode.com.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import minicode.com.dto.Admin.AdminLoginRequest;
import minicode.com.auth.AuthenticateLogin;
import minicode.com.entity.User.User;
import minicode.com.service.Admin.AdminService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.context.SecurityContextRepository;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final AdminService adminService;
    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;

    public AdminController(
            AdminService adminService,
            AuthenticationManager authenticationManager,
            SecurityContextRepository securityContextRepository
    ) {
        this.adminService = adminService;
        this.authenticationManager = authenticationManager;
        this.securityContextRepository = securityContextRepository;
    }
    @PostMapping("/login")
    public User login(
            @Valid @RequestBody AdminLoginRequest request,
            HttpServletRequest httpRequest,
            HttpServletResponse httpResponse
    ) {
        User admin = adminService.login(request);
        AuthenticateLogin.login(
                authenticationManager,
                securityContextRepository,
                request.getAdminUserName(),
                request.getAdminPassword(),
                httpRequest,
                httpResponse
        );
        return admin;
    }
}



