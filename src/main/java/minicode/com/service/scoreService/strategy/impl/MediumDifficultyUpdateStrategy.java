package minicode.com.service.scoreService.strategy.impl;

import minicode.com.entity.User.User;
import minicode.com.service.scoreService.strategy.DifficultyUpdateStrategy;

public class MediumDifficultyUpdateStrategy implements DifficultyUpdateStrategy {
    @Override
    public void update(User user) {
        user.setNumberOfMediumChallengesSolved(user.getNumberOfMediumChallengesSolved() + 1);
    }
}
