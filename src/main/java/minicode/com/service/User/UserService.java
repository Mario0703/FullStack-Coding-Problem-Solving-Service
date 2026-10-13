package minicode.com.service.User;

import jakarta.transaction.Transactional;
import minicode.com.auth.Access;
import minicode.com.dto.Admin.CreateStaffRequest;
import minicode.com.dto.User.Request.ChangeBioRequest;
import minicode.com.dto.User.Request.ChangeEmailRequest;
import minicode.com.dto.User.Request.ChangePasswordRequest;
import minicode.com.dto.User.Request.ChangeUserNameRequest;
import minicode.com.dto.User.Request.LoginRequest;
import minicode.com.dto.User.Request.SignupRequest;
import minicode.com.dto.User.Response.SolverResponse;
import minicode.com.dto.User.Response.UserBioResponse;
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

    private static final String ROLE_NAME_PREFIX = "ROLE_";

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepo roleRepo;

    @Autowired
    public UserService(
            UserRepo userRepo,
            PasswordEncoder passwordEncoder,
            RoleRepo roleRepo
    ) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.roleRepo = roleRepo;
    }

    @Transactional
    public User signup(SignupRequest request) {
        rejectTakenUserName(request.getUserName());
        rejectTakenEmail(request.getEmail());

        return saveNewUser(
                request.getUserName(),
                request.getEmail(),
                request.getPassword(),
                UserRole.USER
        );
    }

    @Transactional
    public User createStaff(CreateStaffRequest request) {
        rejectTakenUserName(request.getUserName());
        rejectTakenEmail(request.getEmail());

        return saveNewUser(
                request.getUserName(),
                request.getEmail(),
                request.getPassword(),
                request.getStaffRole()
        );
    }

    private void rejectTakenUserName(String userName) {
        if (userRepo.findFirstByUserName(userName).isPresent()) {
            throw SignupException.usernameTaken(userName);
        }
    }

    private void rejectTakenEmail(String email) {
        if (userRepo.findFirstByEmail(email).isPresent()) {
            throw SignupException.emailTaken(email);
        }
    }

    private User saveNewUser(String userName, String email, String password, UserRole role) {
        User user = new User();
        user.setUserName(userName);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);

        roleRepo.findByName(ROLE_NAME_PREFIX + role.name())
                .ifPresent(foundRole -> user.setRoles(new HashSet<>(Set.of(foundRole))));

        return userRepo.save(user);
    }

    @Transactional
    public User changeUserName(UUID userId, ChangeUserNameRequest request) {
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
    public User changeBio(UUID userId, ChangeBioRequest request) {
        User user = findById(userId);
        String newBio = request.getNewBio() == null ? "" : request.getNewBio().trim();

        // A blank bio clears the field, so profiles never show empty strings.
        user.setBio(newBio.isBlank() ? null : newBio);
        return userRepo.save(user);
    }

    @Transactional
    public User changePassword(UUID userId, ChangePasswordRequest request) {
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

        boolean isUserSigningInWithEmail = email != null && !email.isBlank();

        if (isUserSigningInWithEmail) {
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
                user.getBio(),
                user.getScore(),
                user.getNumberOfEasyChallengesSolved(),
                user.getNumberOfMediumChallengesSolved(),
                user.getNumberOfHardChallengesSolved(),
                user.getTotalChallengesSolved()
        );
    }

    public UserBioResponse getUserBioById(UUID userId) {
        findById(userId);
        return new UserBioResponse(userRepo.findBioById(userId).orElse(null));
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



    public List<SolverResponse> getSolversBySolvedChallenges(List<SolvedChallenges> solvedChallenges) {
        return solvedChallenges.stream()
                .map(solvedChallenge -> {
                    User user = findById(solvedChallenge.getUserId());
                    return new SolverResponse(user.getUserId(), user.getUserName(), solvedChallenge.getCode(), solvedChallenge.getLanguage(), solvedChallenge.getSolvedAt());
                })
                .toList();
    }


}




