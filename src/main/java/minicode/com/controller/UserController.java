package minicode.com.controller;

import minicode.com.dto.User.LoginRequest;
import minicode.com.dto.User.SignupRequest;
import minicode.com.dto.User.ChangeEmailRequest;
import minicode.com.dto.User.ChangePasswordRequest;
import minicode.com.dto.User.ChangeUserNameRequest;
import minicode.com.entity.User.User;
import minicode.com.service.User.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;

import java.util.UUID;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;

    public UserController(
            UserService userService,
            AuthenticationManager authenticationManager,
            SecurityContextRepository securityContextRepository
    ) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.securityContextRepository = securityContextRepository;
    }

    @PostMapping("/signup")
    public User signup(@Valid @RequestBody SignupRequest request) {

        return userService.signup(request);
    }

    @PostMapping("/login")
    public User login(
            @Valid @RequestBody LoginRequest request,
            HttpServletRequest httpRequest,
            HttpServletResponse httpResponse
    ) {
        User user = userService.login(request); // authenticate the user
        String identifier = request.getEmail() != null && !request.getEmail().isBlank()
                ? request.getEmail()
                : request.getUserName();
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(identifier, request.getPassword())
        );
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
        securityContextRepository.saveContext(context, httpRequest, httpResponse);

        return user;

    }
    @PostMapping("/change-username")
    public User changeUserName(
            @Valid @RequestBody ChangeUserNameRequest request,
            Authentication authentication
    ) {
        return userService.changeUserName(getAuthenticatedUserId(authentication), request);
    }

    @PostMapping("/change-email")
    public User changeUserEmail(
            @Valid @RequestBody ChangeEmailRequest request,
            Authentication authentication
    ) {
        return userService.changeUserEmail(getAuthenticatedUserId(authentication), request);
    }

    @PostMapping("/change-password")
    public User changePassword(
            @Valid @RequestBody ChangePasswordRequest request,
            Authentication authentication
    ) {
        return userService.changePassword(getAuthenticatedUserId(authentication), request);
    }

    private UUID getAuthenticatedUserId(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(
                    org.springframework.http.HttpStatus.UNAUTHORIZED,
                    "User is not authenticated"
            );
        }
        return userService.findByUserName(authentication.getName()).getUserId();
    }


}



