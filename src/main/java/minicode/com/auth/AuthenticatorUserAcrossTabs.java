package minicode.com.auth;


import minicode.com.entity.User.User;
import minicode.com.repository.UserRepo;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.core.Authentication;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthenticatorUserAcrossTabs {


    private final UserRepo userRepo;

    public AuthenticatorUserAcrossTabs(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @GetMapping("/me")
    public userResponse authenticateUserAcrossTabs(Authentication authentication){
        boolean isAuthenticatedValid = isAuthenticated(authentication);
        if (!isAuthenticatedValid) {
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
        String role = getRoleFromAuthentication(authentication);
        UUID id = user.get().getId();
        String userName = user.get().getUserName();
        String email = user.get().getEmail();


        userResponse UserResponse = new userResponse(
                id,
                userName,
                email,
                role
        );
        return UserResponse;
    }

    @GetMapping("/admin")
    public String checkIfAdmin(Authentication authentication) {
        boolean isAuthenticatedValid = isAuthenticated(authentication);
        if (!isAuthenticatedValid){
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Authentication required"
            );
        }
        String role = getRoleFromAuthentication(authentication);

        if(!role.equals("ROLE_ADMIN")){
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Admin role required"
            );
        }
        return role;

    }

    private String getRoleFromAuthentication(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(authority -> authority.startsWith("ROLE_"))
                .findFirst()
                .orElse(null);
    }

    private Boolean isAuthenticated(Authentication authentication) {
        return authentication != null && authentication.isAuthenticated();
    }


}



