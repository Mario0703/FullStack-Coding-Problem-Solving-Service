package minicode.com.controller.User;

import minicode.com.dto.User.Response.UserBioResponse;
import minicode.com.dto.User.Response.UserSummaryResponse;
import minicode.com.service.User.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/public-profile")
public class PublicProfileController {

    private final UserService userService;

    public PublicProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId}")
    public UserSummaryResponse getPublicProfile(@PathVariable UUID userId) {
        return userService.getUserSummaryById(userId);
    }

    @GetMapping("/{userId}/bio")
    public UserBioResponse getUserBio(@PathVariable UUID userId) {
        return userService.getUserBioById(userId);
    }
}
