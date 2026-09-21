package minicode.com.service;

import jakarta.transaction.Transactional;
import jakarta.validation.Validator;
import minicode.com.dto.LoginRequest;
import minicode.com.dto.SignupRequest;
import minicode.com.entity.User;
import minicode.com.repository.UserRepo;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

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

    @Transactional
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

    public User login(LoginRequest request) {
        String email = request.getEmail();
        String userName = request.getUserName();
        String password = request.getPassword();
        User user;


        if (isBlank(email) && isBlank(userName)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Email or username is required"
            );
        }

        if (!isBlank(email) && !isBlank(userName)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Provide either an email or a username"
            );
        }

        if (isBlank(password)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Password is required"
            );
        }

        if (!isBlank(email)) {
            user = findUserByEmail(email);
        } else {
            user = findUserByUserName(userName);
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Password is incorrect"
            );
        }

        return user;
    }

    private User findUserByEmail(String email) {
        Optional<User> userByEmail = userRepo.findFirstByEmail(email);
        if (!userByEmail.isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Email does not exist"
            );
        }

        return userByEmail.get();
    }

    private User findUserByUserName(String userName) {
        Optional<User> userByUserName = userRepo.findFirstByUserName(userName);
        if (!userByUserName.isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Username does not exist"
            );
        }

        return userByUserName.get();
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
