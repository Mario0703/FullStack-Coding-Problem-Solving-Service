package minicode.com.service.scoreService;

import minicode.com.dto.User.UserStats;
import minicode.com.repository.UserRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScoreBoardService {
    private final UserRepo userRepo;

    public ScoreBoardService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public List<UserStats> getUserStats() {
        return userRepo.findAll().stream()
                .map(user -> new UserStats(
                        user.getUserName(),
                        user.getScore(),
                        user.getNumberOfEasyChallengesSolved(),
                        user.getNumberOfMediumChallengesSolved(),
                        user.getNumberOfHardChallengesSolved(),
                        user.getTotalChallengesSolved()
                ))
                .toList();
    }
}
