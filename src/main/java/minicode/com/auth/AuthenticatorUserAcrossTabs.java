package minicode.com.auth;


import minicode.com.auth.Access;
import minicode.com.entity.User.User;
import minicode.com.exception.UnauthenticatedException;
import minicode.com.repository.UserRepo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
        Access.requireAuthenticated(authentication);
        Optional<User> user = userRepo.findFirstByUserName(authentication.getName());

        if(!user.isPresent()){
            throw UnauthenticatedException.authenticationRequired();
        }
        String role = getRoleFromAuthentication(authentication);
        UUID id = user.get().getId();
        String userName = user.get().getUserName();
        String email = user.get().getEmail();
        String bio = user.get().getBio();


        userResponse UserResponse = new userResponse(
                id,
                userName,
                email,
                role,
                bio
        );
        return UserResponse;
    }

    @GetMapping("/admin")
    public String checkIfAdmin(Authentication authentication) {
        Access.requireAdmin(authentication);
        return getRoleFromAuthentication(authentication);
    }

    private String getRoleFromAuthentication(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(authority -> authority.startsWith("ROLE_"))
                .findFirst()
                .orElse(null);
    }


}



