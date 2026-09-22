package minicode.com.auth;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import minicode.com.entity.User;
import minicode.com.repository.UserRepo;
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

    public AuthenticatorUserAcrossTabs(UserRepo userRepo) {
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
