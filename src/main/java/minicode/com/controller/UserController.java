package minicode.com.controller;

import minicode.com.dto.LoginRequest;
import minicode.com.dto.SignupRequest;
import minicode.com.entity.User;
import minicode.com.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

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
    public User login(@Valid @RequestBody LoginRequest request, HttpSession session, HttpServletRequest httprequest) {
        session.invalidate(); // clea session then login in
        session = httprequest.getSession();
        User user = userService.login(request);
        session.setAttribute("user_name", user.getUserName());
        return user;

    }

}
