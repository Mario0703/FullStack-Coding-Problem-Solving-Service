package minicode.com.controller;

import minicode.com.dto.Admin.AdminUserStatistics;
import minicode.com.service.Admin.DashBoardStatsService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.core.Authentication;

import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/api/admin/stats")
public class DashBoardStatsController {

    private final DashBoardStatsService statsService;

    public DashBoardStatsController(DashBoardStatsService statsService) {
        this.statsService = statsService;
    }

    @GetMapping
    public Map<String, Long> getPlatformStatistics(Authentication authentication) {
        requireAdmin(authentication);
        return statsService.getPlatformStatistics();
    }

    @GetMapping("/users")
    public long getNumberOfUsers(Authentication authentication) {
        requireAdmin(authentication);
        return statsService.getNumberOfUsers();
    }

    @GetMapping("/users/list")
    @PreAuthorize("hasAuthority('VIEW_USERS')")
    public List<AdminUserStatistics> getUserStatistics(Authentication authentication) {
        requireAdmin(authentication);
        return statsService.getUserStatistics();
    }

    @GetMapping("/challenges")
    public long getNumberOfChallenges(Authentication authentication) {
        requireAdmin(authentication);
        return statsService.getNumberOfChallenges();
    }

    @GetMapping("/submissions")
    public long getNumberOfSubmittedSolutions(Authentication authentication) {
        requireAdmin(authentication);
        return statsService.getNumberOfSubmittedSolutions();
    }

    @GetMapping("/solved-challenges")
    public long getNumberOfSolvedChallenges(Authentication authentication) {
        requireAdmin(authentication);
        return statsService.getNumberOfSolvedChallenges();
    }

    private void requireAdmin(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Admin access required"
            );
        }
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
        if (!isAdmin) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Admin access required");
        }
    }
}



