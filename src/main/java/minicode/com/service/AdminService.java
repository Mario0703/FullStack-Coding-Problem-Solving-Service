package minicode.com.service;

import minicode.com.dto.AdminLoginRequest;
import minicode.com.entity.User;
import minicode.com.entity.UserRole;
import minicode.com.repository.UserRepo;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AdminService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    public AdminService(
            UserRepo userRepo,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public User login(AdminLoginRequest adminSignUpRequest){
        String adminUserName = adminSignUpRequest.getAdminUserName();
        String adminPassword = adminSignUpRequest.getAdminPassword();


        if (adminUserName.isBlank() || adminPassword.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Admin Username and Password cannot be blank");
        }

        User adminUser = userRepo.findFirstByUserName(adminUserName)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Admin user not found"));

        if (adminUser.getRole() != UserRole.ADMIN) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Admin user not found");
        }

        if (!passwordEncoder.matches(adminPassword, adminUser.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid password");
        }

        return adminUser;

    }

}
