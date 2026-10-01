package minicode.com.auth;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import minicode.com.entity.User;
import minicode.com.entity.UserRole;
import minicode.com.repository.UserRepo;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

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
    public User authenticateUserAcrossTabs(HttpServletRequest httpRequest){
        HttpSession session = httpRequest.getSession(false);
        if(session == null){

            throw new ResponseStatusException(
                    org.springframework.http.HttpStatus.UNAUTHORIZED,
                    "User is not authenticated"
            );

        }
        Object userIdAttribute = session.getAttribute("user_id");
        if (!(userIdAttribute instanceof UUID userId)) {
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

    @GetMapping("/admin")
    private void checkIfAdmin(HttpSession session) {
        if (session == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Authentication required"
            );
        }

        Object userId = session.getAttribute("user_id");
        if (userId instanceof UUID id) {
            User user = userRepo.findById(id).orElse(null);
            if (user != null && user.getRole() == UserRole.ADMIN) {
                return;
            }
            if (user == null) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Admin is not authenticated");
            }
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Admin access required");
        }

        throw new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "Authentication required"
        );
    }


}
