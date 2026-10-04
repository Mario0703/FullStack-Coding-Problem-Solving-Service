package minicode.com.controller;

import minicode.com.dto.User.UserStats;
import minicode.com.service.scoreService.ScoreBoardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/scoreboard")
public class ScoreBoardController {
    private ScoreBoardService scoreBoardService;

    public ScoreBoardController(ScoreBoardService scoreBoardService) {
        this.scoreBoardService = scoreBoardService;
    }

    @GetMapping("/user-stats")
    public List<UserStats> getUserStats(UUID userId) {
        return scoreBoardService.getUserStats(userId);
    }

}
