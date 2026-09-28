package minicode.com.service;

import jakarta.annotation.PostConstruct;
import minicode.com.dto.AdminLoginRequest;
import minicode.com.entity.AdminUser;
import minicode.com.repository.AdminRepo;
import minicode.com.security.BcryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    private final AdminRepo adminRepo;

    public AdminService(AdminRepo adminRepo) {
        this.adminRepo = adminRepo;
    }

    public AdminUser login(AdminLoginRequest adminSignUpRequest){
        String adminUserName = adminSignUpRequest.getAdminUserName();
        String adminPassword = adminSignUpRequest.getAdminPassword();


        if (adminUserName.isBlank() || adminPassword.isBlank()) {
            throw new IllegalArgumentException("Admin Username and Password cannot be blank");
        }

        AdminUser adminUser = adminRepo.findByAdminUserName(adminUserName)
                .orElseThrow(() -> new IllegalArgumentException("Admin user not found"));

        BcryptPasswordEncoder bcryptEncrypter = new BcryptPasswordEncoder();
        if (!bcryptEncrypter.matches(adminPassword, adminUser.getAdminPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }

        return adminUser;

    }

    @PostConstruct
    public void createAdminUserOnStartup() {
        String adminUserName = "admin";
        String adminPassword = "admin";
        createAdminOnStartUp(adminUserName, adminPassword);
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
