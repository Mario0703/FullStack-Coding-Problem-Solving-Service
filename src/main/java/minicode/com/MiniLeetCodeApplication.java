package minicode.com;

import jakarta.validation.Valid;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@SpringBootApplication
@RestController
public class MiniLeetCodeApplication {

    private final UserRepo userRepo;

    public MiniLeetCodeApplication(UserRepo userRepo) {
        this.userRepo = userRepo;
    }


    @PostMapping("/api/user/signup")
    public User signup(@Valid @RequestBody SignupRequest request) {

        BcryptPasswordEncoder passwordEncoder = new BcryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User user = new User(
                request.getUserName(),
                request.getEmail(),
                encodedPassword
        );

        if (userRepo.findFirstByUserName(request.getUserName()).isPresent()) {
            throw new ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST,
                    "Username already exists"
            );
        }

        if (userRepo.findFirstByEmail(request.getEmail()).isPresent()) {
            throw new ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST,
                    "Email already exists"
            );
        }



        User savedUser = userRepo.save(user);

        System.out.printf(
                "Saved user: %s, email: %s, id: %s%n",
                savedUser.getUserName(),
                savedUser.getEmail(),
                savedUser.getUserId()
        );

        return savedUser;
    }

    public static void main(String[] args) {
        SpringApplication.run(MiniLeetCodeApplication.class, args);
    }

}
