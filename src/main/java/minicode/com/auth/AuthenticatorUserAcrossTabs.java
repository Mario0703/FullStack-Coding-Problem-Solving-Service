package minicode.com.auth;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import minicode.com.entity.AdminUser;
import minicode.com.entity.User;
import minicode.com.repository.UserRepo;
import minicode.com.repository.AdminRepo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth/me")
public class AuthenticatorUserAcrossTabs {


    private final UserRepo userRepo;
    private final AdminRepo adminUserRepo;

    public AuthenticatorUserAcrossTabs(UserRepo userRepo, AdminRepo adminUserRepo) {
        this.adminUserRepo = adminUserRepo;
        this.userRepo = userRepo;
    }

    @GetMapping
    public User authenticateUserAcrossTabs(HttpServletRequest httpRequest){
        HttpSession session = httpRequest.getSession(false);
        if(session == null){

            throw new ResponseStatusException(
                    org.springframework.http.HttpStatus.UNAUTHORIZED,
                    "User is not authenticated"
            );

        }
        UUID userId = (UUID) session.getAttribute("user_id");
        UUID adminUserId = (UUID) session.getAttribute("admin_user_id");

        if(adminUserId != null){
            Optional<AdminUser> adminUser = adminUserRepo.findById(adminUserId);
            if(!adminUser.isPresent()){
                throw new ResponseStatusException(
                        org.springframework.http.HttpStatus.UNAUTHORIZED,
                        "Admin User is not authenticated"
                );
            }
            return null;
        }
        if(userId == null){
            throw new ResponseStatusException(
                    org.springframework.http.HttpStatus.UNAUTHORIZED,
                    "User is not authenticated"
            );

        }
        Optional<User> user = userRepo.findById(userId);
        if(!user.isPresent()){
            throw new ResponseStatusException(
                    org.springframework.http.HttpStatus.UNAUTHORIZED,
                    "User is not authenticated"
            );
        }

        return user.get();
    }


}
