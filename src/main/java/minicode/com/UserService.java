package minicode.com;

import jakarta.transaction.Transactional;
import jakarta.validation.Validator;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {

    private final UserRepo userRepo;
    private final Validator validator;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepo userRepo,
            Validator validator,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepo = userRepo;
        this.validator = validator;
        this.passwordEncoder = passwordEncoder;
    }
    @Transactional // Atomic operations, either it all succeed or all fail
    public User signup(SignupRequest request) {
        if (!validator.validate(request).isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Signup request is invalid"
            );
        }

        if (userRepo.findFirstByUserName(request.getUserName()).isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Username already exists"
            );
        }

        if (userRepo.findFirstByEmail(request.getEmail()).isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Email already exists"
            );
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        User user = new User(
                request.getUserName(),
                request.getEmail(),
                encodedPassword
        );

        return userRepo.save(user);
    }
}
