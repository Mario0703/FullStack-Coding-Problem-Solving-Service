package minicode.com.service.scoreService.strategy.impl;

import minicode.com.entity.User.User;
import minicode.com.service.scoreService.strategy.DifficultyUpdateStrategy;

public class HardDifficultyUpdateStrategy implements DifficultyUpdateStrategy {
    @Override
    public void update(User user) {
        user.setNumberOfHardChallengesSolved(user.getNumberOfHardChallengesSolved() + 1);
    }
}
