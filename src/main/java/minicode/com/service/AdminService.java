package minicode.com.service;

import jakarta.annotation.PostConstruct;
import minicode.com.dto.AdminLoginRequest;
import minicode.com.entity.AdminUser;
import minicode.com.repository.AdminRepo;
import minicode.com.security.BcryptPasswordEncoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AdminService {
    private final AdminRepo adminRepo;
    private final String startupAdminUsername;
    private final String startupAdminPassword;

    public AdminService(
            AdminRepo adminRepo,
            @Value("${ADMIN_USERNAME}") String startupAdminUsername,
            @Value("${ADMIN_PASSWORD}") String startupAdminPassword
    ) {
        this.adminRepo = adminRepo;
        this.startupAdminUsername = startupAdminUsername;
        this.startupAdminPassword = startupAdminPassword;
    }

    public AdminUser login(AdminLoginRequest adminSignUpRequest){
        String adminUserName = adminSignUpRequest.getAdminUserName();
        String adminPassword = adminSignUpRequest.getAdminPassword();


        if (adminUserName.isBlank() || adminPassword.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Admin Username and Password cannot be blank");
        }

        AdminUser adminUser = adminRepo.findByAdminUserName(adminUserName)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Admin user not found"));

        BcryptPasswordEncoder bcryptEncrypter = new BcryptPasswordEncoder();
        if (!bcryptEncrypter.matches(adminPassword, adminUser.getAdminPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid password");
        }

        return adminUser;

    }

    @PostConstruct
    public void createAdminUserOnStartup() {
        createAdminOnStartUp(startupAdminUsername, startupAdminPassword);
    }

    public void createAdminOnStartUp(String adminUserName, String adminPassword) {
        Boolean isAdminTableEmpty = adminRepo.isThereAnStartAdmin();
        if (!isAdminTableEmpty) {
            return;
        }
        BcryptPasswordEncoder bcryptEncrypter = new BcryptPasswordEncoder();
        String encodedPassword = bcryptEncrypter.encode(adminPassword);
        AdminUser admin = new AdminUser(adminUserName, encodedPassword);
        adminRepo.save(admin);


    }

}
