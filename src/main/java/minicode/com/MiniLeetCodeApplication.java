package minicode.com;

import jakarta.validation.Valid;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class MiniLeetCodeApplication {

    private final UserRepo userRepo;

    public MiniLeetCodeApplication(UserRepo userRepo) {
        this.userRepo = userRepo;
    }


    @PostMapping("/api/user/signup")
    public User signup(@Valid @RequestBody SignupRequest request) {
        User user = new User(
                request.getUserName(),
                request.getEmail(),
                request.getPassword()
        );

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
