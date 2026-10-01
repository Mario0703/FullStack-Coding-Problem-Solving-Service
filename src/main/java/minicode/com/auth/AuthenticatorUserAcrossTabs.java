package minicode.com.auth;


import minicode.com.entity.User.User;
import minicode.com.repository.UserRepo;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.core.Authentication;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthenticatorUserAcrossTabs {


    private final UserRepo userRepo;

    public AuthenticatorUserAcrossTabs(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @GetMapping("/me")
    public User authenticateUserAcrossTabs(Authentication authentication){
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(
                    org.springframework.http.HttpStatus.UNAUTHORIZED,
                    "User is not authenticated"
            );
        }
        Optional<User> user = userRepo.findFirstByUserName(authentication.getName());
        if(!user.isPresent()){
            throw new ResponseStatusException(
                    org.springframework.http.HttpStatus.UNAUTHORIZED,
                    "User is not authenticated"
            );
        }

        return user.get();
    }

    @GetMapping("/admin")
    public void checkIfAdmin(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Authentication required"
            );
        }

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
        if (!isAdmin) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Admin access required");
        }
    }


}



