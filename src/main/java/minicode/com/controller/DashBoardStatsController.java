package minicode.com.controller;

import jakarta.servlet.http.HttpSession;
import minicode.com.service.DashBoardStatsService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/stats")
public class DashBoardStatsController {

    private final DashBoardStatsService statsService;

    public DashBoardStatsController(DashBoardStatsService statsService) {
        this.statsService = statsService;
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
        if (session == null || session.getAttribute("admin_user_id") == null) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Admin access required"
            );
        }
    }
}
