package minicode.com.service.User;

import jakarta.transaction.Transactional;
import jakarta.validation.Validator;
import minicode.com.auth.Access;
import minicode.com.dto.User.Request.ChangeEmailRequest;
import minicode.com.dto.User.Request.ChangePasswordRequest;
import minicode.com.dto.User.Request.ChangeUserNameRequest;
import minicode.com.dto.User.Request.LoginRequest;
import minicode.com.dto.User.Request.SignupRequest;
import minicode.com.dto.User.Response.SolverResponse;
import minicode.com.dto.User.Response.UserSummaryResponse;
import minicode.com.entity.Challange.SolvedChallenges;
import minicode.com.entity.Roles.UserRole;
import minicode.com.entity.User.User;
import minicode.com.exception.LoginErrorException;
import minicode.com.exception.SignupException;
import minicode.com.exception.UnauthenticatedException;
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
            throw SignupException.invalidRequest("Signup request is invalid");
        }

        if (userRepo.findFirstByUserName(request.getUserName()).isPresent()) {
            throw SignupException.usernameTaken(request.getUserName());
        }

        if (userRepo.findFirstByEmail(request.getEmail()).isPresent()) {
            throw SignupException.emailTaken(request.getEmail());
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        User user = new User();
        user.setUserName(request.getUserName());
        user.setEmail(request.getEmail());
        user.setPassword(encodedPassword);
        user.setRole(UserRole.USER);

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
        request.setNewUserName(request.getNewUserName().trim());

        String normalizedUserName = request.getNewUserName();
        Optional<User> existingUser = userRepo.findFirstByUserName(normalizedUserName);
        boolean IsUserConflicting = existingUser.isPresent()
                && !existingUser.get().getUserId().equals(userId);
        if (IsUserConflicting) {
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
        String normalizedEmail = request.getNewEmail();

        Optional<User> existingUser = userRepo.findFirstByEmail(normalizedEmail);
        boolean IsUserConflicting = existingUser.isPresent()
                && !existingUser.get().getUserId().equals(userId);
        if (IsUserConflicting) {
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
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Current password is incorrect"
            );
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        return userRepo.save(user);
    }


    public User login(LoginRequest request) {
        String email = request.getEmail();
        String userName = request.getUserName();
        String password = request.getPassword();
        User user;


        boolean hasBlankEmailAndUserName = isBlank(email) && isBlank(userName);
        if (hasBlankEmailAndUserName) {
            throw LoginErrorException.invalidCredentials("Email or username is required");
        }

        boolean hasEmailAndUserName = !isBlank(email) && !isBlank(userName);
        if (hasEmailAndUserName) {
            throw LoginErrorException.invalidCredentials("Provide either an email or a username");
        }

        if (isBlank(password)) {
            throw LoginErrorException.invalidCredentials("Password is required");
        }

        if (!isBlank(email)) {
            user = findUserByEmail(email);
        } else {
            user = findUserByUserName(userName);
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw LoginErrorException.invalidCredentials("Password is incorrect");
        }

        return user;
    }

    private User findUserByEmail(String email) {
        Optional<User> userByEmail = userRepo.findFirstByEmail(email);
        if (!userByEmail.isPresent()) {
            throw LoginErrorException.invalidCredentials("Email does not exist");
        }

        return userByEmail.get();
    }

    private User findUserByUserName(String userName) {
        Optional<User> userByUserName = userRepo.findFirstByUserName(userName);
        if (!userByUserName.isPresent()) {
            throw LoginErrorException.invalidCredentials("Username does not exist");
        }

        return userByUserName.get();
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

    public UserSummaryResponse getUserSummaryById(UUID userId) {
        User user = findById(userId);
        return new UserSummaryResponse(
                user.getUserId(),
                user.getUserName(),
                user.getEmail(),
                user.getScore(),
                user.getNumberOfEasyChallengesSolved(),
                user.getNumberOfMediumChallengesSolved(),
                user.getNumberOfHardChallengesSolved(),
                user.getTotalChallengesSolved()
        );
    }

    public User findByUserName(String userName) {
        Optional<User> userByUserName = userRepo.findFirstByUserName(userName);
        if (!userByUserName.isPresent()) {
            throw UnauthenticatedException.notAuthenticated("Username does not exist");
        }

        return userByUserName.get();
    }

    public UUID getAuthenticatedUserId(Authentication authentication) {
        Access.requireAuthenticated(authentication);
        return findByUserName(authentication.getName()).getUserId();
    }

    private boolean isBlank(String value) {
        boolean hasBlankValue = value == null || value.isBlank();
        return hasBlankValue;
    }


    public List<SolverResponse> getSolversBySolvedChallenges(List<SolvedChallenges> solvedChallenges) {
        return solvedChallenges.stream()
                .map(solvedChallenge -> {
                    User user = findById(solvedChallenge.getUserId());
                    return new SolverResponse(user.getUserId(), user.getUserName(), solvedChallenge.getCode(), solvedChallenge.getLanguage(), solvedChallenge.getSolvedAt());
                })
                .toList();
    }


}




