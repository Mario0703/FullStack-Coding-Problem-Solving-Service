package minicode.com.controller;

import minicode.com.dto.User.Response.UserStatsResponse;
import minicode.com.service.scoreService.ScoreBoardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/scoreboard")
public class ScoreBoardController {
    private final ScoreBoardService scoreBoardService;

    public ScoreBoardController(ScoreBoardService scoreBoardService) {
        this.scoreBoardService = scoreBoardService;
    }

    @GetMapping("/user-stats")
    public List<UserStatsResponse> getUserStats() {
        return scoreBoardService.getUserStats();
    }

}
