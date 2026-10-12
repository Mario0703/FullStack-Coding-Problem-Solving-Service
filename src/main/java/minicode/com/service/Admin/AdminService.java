package minicode.com.service.Admin;

import minicode.com.dto.Admin.AdminLoginRequest;
import minicode.com.entity.User.User;
import minicode.com.entity.Roles.UserRole;
import minicode.com.exception.LoginErrorException;
import minicode.com.repository.UserRepo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

        User adminUser = userRepo.findFirstByUserName(adminUserName)
                .orElseThrow(() -> LoginErrorException.invalidCredentials("Admin user not found"));

        if (adminUser.getRole() != UserRole.ADMIN) {
            throw LoginErrorException.invalidCredentials("Admin user not found");
        }

        if (!passwordEncoder.matches(adminPassword, adminUser.getPassword())) {
            throw LoginErrorException.invalidCredentials("Invalid password");
        }

        return adminUser;

    }

}
