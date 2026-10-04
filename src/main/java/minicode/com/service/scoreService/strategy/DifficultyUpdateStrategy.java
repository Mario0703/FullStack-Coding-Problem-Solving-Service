package minicode.com.service.scoreService.strategy;

import minicode.com.entity.User.User;

@FunctionalInterface
public interface DifficultyUpdateStrategy {
    void update(User user);
}
