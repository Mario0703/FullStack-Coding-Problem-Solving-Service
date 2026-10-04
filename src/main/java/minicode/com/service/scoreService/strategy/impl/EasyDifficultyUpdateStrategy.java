package minicode.com.service.scoreService.strategy.impl;

import minicode.com.entity.User.User;
import minicode.com.service.scoreService.strategy.DifficultyUpdateStrategy;

public class EasyDifficultyUpdateStrategy implements DifficultyUpdateStrategy {
    @Override
    public void update(User user) {
        user.setNumberOfEasyChallengesSolved(user.getNumberOfEasyChallengesSolved() + 1);
    }
}
