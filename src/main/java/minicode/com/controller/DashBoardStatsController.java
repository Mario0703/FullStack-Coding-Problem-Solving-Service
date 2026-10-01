package minicode.com.controller;

import jakarta.servlet.http.HttpSession;
import minicode.com.dto.AdminUserStatistics;
import minicode.com.entity.User;
import minicode.com.entity.UserRole;
import minicode.com.repository.UserRepo;
import minicode.com.service.DashBoardStatsService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/api/admin/stats")
public class DashBoardStatsController {

    private final DashBoardStatsService statsService;
    private final UserRepo userRepo;

    public DashBoardStatsController(DashBoardStatsService statsService, UserRepo userRepo) {
        this.statsService = statsService;
        this.userRepo = userRepo;
    }

    @GetMapping
    public Map<String, Long> getPlatformStatistics(HttpSession session) {
        requireAdmin(session);
        return statsService.getPlatformStatistics();
    }

    @GetMapping("/users")
    public long getNumberOfUsers(HttpSession session) {
        requireAdmin(session);
        return statsService.getNumberOfUsers();
    }

    @GetMapping("/users/list")
    public List<AdminUserStatistics> getUserStatistics(HttpSession session) {
        requireAdmin(session);
        return statsService.getUserStatistics();
    }

    @GetMapping("/challenges")
    public long getNumberOfChallenges(HttpSession session) {
        requireAdmin(session);
        return statsService.getNumberOfChallenges();
    }

    @GetMapping("/submissions")
    public long getNumberOfSubmittedSolutions(HttpSession session) {
        requireAdmin(session);
        return statsService.getNumberOfSubmittedSolutions();
    }

    @GetMapping("/solved-challenges")
    public long getNumberOfSolvedChallenges(HttpSession session) {
        requireAdmin(session);
        return statsService.getNumberOfSolvedChallenges();
    }

    private void requireAdmin(HttpSession session) {
        if (session == null || !(session.getAttribute("user_id") instanceof java.util.UUID)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Admin access required"
            );
        }
        User user = userRepo.findById((java.util.UUID) session.getAttribute("user_id")).orElse(null);
        if (user == null || user.getRole() != UserRole.ADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Admin access required");
        }
    }
}
