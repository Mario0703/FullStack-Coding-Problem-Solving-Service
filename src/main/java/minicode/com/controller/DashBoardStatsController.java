package minicode.com.controller;

import minicode.com.dto.Admin.AdminUserStatistics;
import minicode.com.auth.Access;
import minicode.com.entity.Submissions.Submission;
import minicode.com.entity.User.User;
import minicode.com.service.Admin.DashBoardStatsService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;

import java.util.Map;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/stats")
public class DashBoardStatsController {

    private final DashBoardStatsService statsService;

    public DashBoardStatsController(DashBoardStatsService statsService) {
        this.statsService = statsService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('VIEW_STATISTICS')")
    public Map<String, Long> getPlatformStatistics(Authentication authentication) {
        Access.requireAdmin(authentication);
        return statsService.getPlatformStatistics();
    }

    @GetMapping("/permission")
    @PreAuthorize("hasAuthority('VIEW_STATISTICS')")
    public Map<String, Boolean> canViewStatistics() {
        return Map.of("allowed", true);
    }

    @GetMapping("/users")
    public long getNumberOfUsers(Authentication authentication) {
        Access.requireAdmin(authentication);
        return statsService.getNumberOfUsers();
    }

    @GetMapping("/users/all")
    public List<User> getAllUsers(Authentication authentication) {
        Access.requireAdmin(authentication);
        return statsService.getAllUsers();
    }

    @GetMapping("/users/search")
    public User searchForUserByName(
            @RequestParam String userName,
            Authentication authentication
    ) {
        Access.requireAdmin(authentication);
        return statsService.searchForUserByName(userName);
    }

    @DeleteMapping("/users/{userId}")
    public void deleteUser(
            @PathVariable UUID userId,
            Authentication authentication
    ) {
        Access.requireAdmin(authentication);
        statsService.deleteUser(userId);
    }

    @GetMapping("/submissions/all")
    public List<Submission> getAllSubmissions(Authentication authentication) {
        Access.requireAdmin(authentication);
        return statsService.getAllSubmissions();
    }

    @GetMapping("/submissions/user/{userId}")
    public List<Submission> getAllSubmissionsByUser(
            @PathVariable UUID userId,
            Authentication authentication
    ) {
        Access.requireAdmin(authentication);
        return statsService.getAllSubmissionsByUser(userId);
    }

    @GetMapping("/users/list")
    @PreAuthorize("hasAuthority('VIEW_USERS')")
    public List<AdminUserStatistics> getUserStatistics(Authentication authentication) {
        Access.requireAdmin(authentication);
        return statsService.getUserStatistics();
    }

    @GetMapping("/challenges")
    public long getNumberOfChallenges(Authentication authentication) {
        Access.requireAdmin(authentication);
        return statsService.getNumberOfChallenges();
    }

    @GetMapping("/submissions")
    public long getNumberOfSubmittedSolutions(Authentication authentication) {
        Access.requireAdmin(authentication);
        return statsService.getNumberOfSubmittedSolutions();
    }

    @GetMapping("/solved-challenges")
    public long getNumberOfSolvedChallenges(Authentication authentication) {
        Access.requireAdmin(authentication);
        return statsService.getNumberOfSolvedChallenges();
    }
}



