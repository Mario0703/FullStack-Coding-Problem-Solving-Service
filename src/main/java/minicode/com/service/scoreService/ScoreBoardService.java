package minicode.com.service.scoreService;

import minicode.com.dto.User.UserStats;
import minicode.com.repository.UserRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ScoreBoardService {
    private final UserRepo userRepo;

    public ScoreBoardService(UserRepo userRepo) {
        this.userRepo = userRepo;

    }

    public List<UserStats> getUserStats(UUID userId) {
        List<UserStats> userStatsList = new ArrayList<>();
        long numberOfUsers = userRepo.count();
        if (numberOfUsers == 0) {
            throw new IllegalArgumentException("No users found in database");
        }
        for(int i = 0; i < numberOfUsers; i++) {
            UserStats userStats = userRepo.findById(userId).map(user -> new UserStats(
                    user.getUserName(),
                    user.getScore(),
                    user.getNumberOfEasyChallengesSolved(),
                    user.getNumberOfMediumChallengesSolved(),
                    user.getNumberOfHardChallengesSolved(),
                    user.getTotalChallengesSolved()
            )).orElse(null);
            userStatsList.add(userStats);

        }
        return userStatsList;
    }

}
