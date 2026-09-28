package minicode.com.controller;

import minicode.com.dto.LoginRequest;
import minicode.com.dto.SignupRequest;
import minicode.com.dto.ChangeEmailRequest;
import minicode.com.dto.ChangePasswordRequest;
import minicode.com.dto.ChangeUserNameRequest;
import minicode.com.entity.Challenge;
import minicode.com.entity.User;
import minicode.com.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public User signup(@Valid @RequestBody SignupRequest request) {

        return userService.signup(request);
    }

    @PostMapping("/login")
    public User login(@Valid @RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        User user = userService.login(request); // authenticate the user
        HttpSession oldSession = httpRequest.getSession(false);
        if (oldSession != null) {
            oldSession.invalidate();
        }

        HttpSession newSession = httpRequest.getSession(true);
        newSession.setAttribute("user_id", user.getUserId());


        return user;

    }
    @PostMapping("/logout")
    public void logout(HttpServletRequest httpRequest) {
        HttpSession session = httpRequest.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }
    @PostMapping("/change-username")
    public User changeUserName(
            @Valid @RequestBody ChangeUserNameRequest request,
            HttpServletRequest httpRequest
    ) {
        return userService.changeUserName(getAuthenticatedUserId(httpRequest), request);
    }

    @PostMapping("/change-email")
    public User changeUserEmail(
            @Valid @RequestBody ChangeEmailRequest request,
            HttpServletRequest httpRequest
    ) {
        return userService.changeUserEmail(getAuthenticatedUserId(httpRequest), request);
    }

    @PostMapping("/change-password")
    public User changePassword(
            @Valid @RequestBody ChangePasswordRequest request,
            HttpServletRequest httpRequest
    ) {
        return userService.changePassword(getAuthenticatedUserId(httpRequest), request);
    }

    private UUID getAuthenticatedUserId(HttpServletRequest httpRequest) {
        HttpSession session = httpRequest.getSession(false);
        if (session == null) {
            throw new ResponseStatusException(
                    org.springframework.http.HttpStatus.UNAUTHORIZED,
                    "User is not authenticated"
            );
        }

        Object userId = session.getAttribute("user_id");
        if (!(userId instanceof UUID)) {
            if (session.getAttribute("admin_user_id") != null) {
                throw new ResponseStatusException(
                        org.springframework.http.HttpStatus.FORBIDDEN,
                        "User access is not permitted for an admin session"
                );
            }
            throw new ResponseStatusException(
                    org.springframework.http.HttpStatus.UNAUTHORIZED,
                    "User is not authenticated"
            );
        }

        return (UUID) userId;
    }


}
