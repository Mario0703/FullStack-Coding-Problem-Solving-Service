package minicode.com.controller.User;

import minicode.com.dto.User.Request.LoginRequest;
import minicode.com.dto.User.Request.SignupRequest;
import minicode.com.dto.User.Request.ChangeEmailRequest;
import minicode.com.dto.User.Request.ChangePasswordRequest;
import minicode.com.dto.User.Request.ChangeUserNameRequest;
import minicode.com.entity.User.User;
import minicode.com.auth.AuthenticateLogin;
import minicode.com.service.User.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.util.StringUtils;

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
        User user = userService.login(request);
        String loginIdentifier = StringUtils.hasText(request.getEmail())
                ? request.getEmail()
                : request.getUserName();

        AuthenticateLogin.login(
                authenticationManager,
                securityContextRepository,
                loginIdentifier,
                request.getPassword(),
                httpRequest,
                httpResponse
        );

        return user;

    }
    @PostMapping("/change-username")
    public User changeUserName(
            @Valid @RequestBody ChangeUserNameRequest request,
            Authentication authentication
    ) {
        return userService.changeUserName(userService.getAuthenticatedUserId(authentication), request);
    }

    @PostMapping("/change-email")
    public User changeUserEmail(
            @Valid @RequestBody ChangeEmailRequest request,
            Authentication authentication
    ) {
        return userService.changeUserEmail(userService.getAuthenticatedUserId(authentication), request);
    }

    @PostMapping("/change-password")
    public User changePassword(
            @Valid @RequestBody ChangePasswordRequest request,
            Authentication authentication
    ) {
        return userService.changePassword(userService.getAuthenticatedUserId(authentication), request);
    }


}



