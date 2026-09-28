package minicode.com.service;

import minicode.com.dto.AdminLoginRequest;
import minicode.com.dto.AdminSignUpRequest;
import minicode.com.entity.AdminUser;
import minicode.com.repository.AdminRepo;
import minicode.com.repository.UserRepo;
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


        AdminUser admin = adminRepo.findByAdminUserName(adminUserName)
                .orElseThrow(() -> new IllegalArgumentException("Admin user not found"));
        if (!admin.getAdminPassword().equals(adminPassword)) {
            throw new IllegalArgumentException("Invalid admin password");
        }

        return admin;


    }

}
