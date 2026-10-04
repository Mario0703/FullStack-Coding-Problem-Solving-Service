package minicode.com.service.User;

import jakarta.transaction.Transactional;
import jakarta.validation.Validator;
import minicode.com.dto.User.ChangeEmailRequest;
import minicode.com.dto.User.ChangePasswordRequest;
import minicode.com.dto.User.ChangeUserNameRequest;
import minicode.com.dto.User.LoginRequest;
import minicode.com.dto.User.SignupRequest;
import minicode.com.dto.User.SolverResponse;
import minicode.com.dto.User.UserSummary;
import minicode.com.entity.Challange.SolvedChallenges;
import minicode.com.entity.User.User;
import minicode.com.repository.UserRepo;
import minicode.com.repository.RoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
public class UserService {

    private final UserRepo userRepo;
    private final Validator validator;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepo roleRepo;

    public UserService(UserRepo userRepo, Validator validator, PasswordEncoder passwordEncoder) {
        this(userRepo, validator, passwordEncoder, null);
    }

    @Autowired
    public UserService(
            UserRepo userRepo,
            Validator validator,
            PasswordEncoder passwordEncoder,
            RoleRepo roleRepo
    ) {
        this.userRepo = userRepo;
        this.validator = validator;
        this.passwordEncoder = passwordEncoder;
        this.roleRepo = roleRepo;
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

        if (roleRepo != null) {
            roleRepo.findByName("ROLE_USER").ifPresent(role ->
                    user.setRoles(new HashSet<>(Set.of(role)))
            );
        }

        return userRepo.save(user);
    }

    @Transactional
    public User changeUserName(UUID userId, ChangeUserNameRequest request) {
        if (!validator.validate(request).isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Username change request is invalid"
            );
        }

        User user = findById(userId);
        String normalizedUserName = request.newUserName().trim();

        Optional<User> existingUser = userRepo.findFirstByUserName(normalizedUserName);
        if (existingUser.isPresent() && !existingUser.get().getUserId().equals(userId)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Username already exists"
            );
        }

        user.setUserName(normalizedUserName);
        return userRepo.save(user);
    }

    @Transactional
    public User changeUserEmail(UUID userId, ChangeEmailRequest request) {
        if (!validator.validate(request).isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Email change request is invalid"
            );
        }

        User user = findById(userId);
        String normalizedEmail = request.newEmail().trim();

        Optional<User> existingUser = userRepo.findFirstByEmail(normalizedEmail);
        if (existingUser.isPresent() && !existingUser.get().getUserId().equals(userId)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Email already exists"
            );
        }

        user.setEmail(normalizedEmail);
        return userRepo.save(user);
    }

    @Transactional
    public User changePassword(UUID userId, ChangePasswordRequest request) {
        if (!validator.validate(request).isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Password change request is invalid"
            );
        }

        User user = findById(userId);
        if (!passwordEncoder.matches(request.currentPassword(), user.getPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Current password is incorrect"
            );
        }

        user.setPassword(passwordEncoder.encode(request.newPassword()));
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
            user = findByUserName(userName);
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Password is incorrect"
            );
        }

        return user;
    }

    private User findUserByEmail(String email) {
        Optional<User> userByEmail = userRepo.findFirstByEmail(email);
        if (!userByEmail.isPresent()) {
            throw new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Email does not exist"
            );
        }

        return userByEmail.get();
    }

    public User findById(UUID userId) {
        Optional<User> user = userRepo.findById(userId);
        if (user.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "User not found"
            );
        }

        return user.get();
    }

    public UserSummary getUserSummaryById(UUID userId) {
        User user = findById(userId);
        return new UserSummary(
                user.getUserId(),
                user.getUserName(),
                user.getEmail(),
                user.getScore(),
                user.getNumberOfEasyChallengesSolved(),
                user.getNumberOfMediumChallengesSolved(),
                user.getNumberOfHardChallengesSolved()
        );
    }

    public User findByUserName(String userName) {
        Optional<User> userByUserName = userRepo.findFirstByUserName(userName);
        if (!userByUserName.isPresent()) {
            throw new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Username does not exist"
            );
        }

        return userByUserName.get();
    }

    public UUID getAuthenticatedUserId(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(
                    org.springframework.http.HttpStatus.UNAUTHORIZED,
                    "User is not authenticated"
            );
        }
        return findByUserName(authentication.getName()).getUserId();
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }


    public List<SolverResponse> getSolversBySolvedChallenges(List<SolvedChallenges> solvedChallenges) {
        return solvedChallenges.stream()
                .map(solvedChallenge -> {
                    User user = findById(solvedChallenge.getUserId());
                    return new SolverResponse(user.getUserName(), solvedChallenge.getCode());
                })
                .toList();
    }


}




