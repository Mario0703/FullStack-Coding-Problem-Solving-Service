package minicode.com.service.Ai;

import minicode.com.repository.UserRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.util.UUID;

/**
 * Central helper for all miniHelper token accounting.
 *
 * <p>It enforces a per-user token budget inside a rolling window kept in Redis
 * (so the quota resets automatically when the key expires) and mirrors the
 * lifetime total into the {@code users.token_usage} column. Every token
 * recording goes through here so the policy lives in one place.
 */
@Service
public class TokenUsageRateLimiter {

    private static final String KEY_PREFIX = "minihelper:tokens:";

    private final StringRedisTemplate redis;
    private final UserRepo userRepo;
    private final long tokenBudget;
    private final Duration window;

    public TokenUsageRateLimiter(
            StringRedisTemplate redis,
            UserRepo userRepo,
            @Value("${MINIHELPER_TOKEN_BUDGET:50000}") long tokenBudget,
            @Value("${MINIHELPER_TOKEN_WINDOW:PT24H}") Duration window
    ) {
        this.redis = redis;
        this.userRepo = userRepo;
        this.tokenBudget = tokenBudget;
        this.window = window;
    }

    /**
     * Rejects the request with 429 when the user has already spent their whole
     * budget for the current window. Call this before asking OpenAI.
     */
    public void ensureWithinBudget(UUID userId) {
        if (currentUsage(userId) >= tokenBudget) {
            throw new ResponseStatusException(
                    HttpStatus.TOO_MANY_REQUESTS,
                    "You have reached your miniHelper token budget. Please try again later."
            );
        }
    }

    /**
     * Records the tokens a completed hint consumed: bumps the rolling Redis
     * counter (starting the window timer on the first write) and the user's
     * lifetime total in the database.
     */
    public void recordUsage(UUID userId, long tokens) {
        if (tokens <= 0) {
            return;
        }

        String key = key(userId);
        Long windowTotal = redis.opsForValue().increment(key, tokens);
        if (windowTotal != null && windowTotal == tokens) {
            redis.expire(key, window);
        }

        userRepo.addTokenUsage(userId, tokens);
    }

    /** Tokens the user has spent in the current window (0 if none). */
    public long currentUsage(UUID userId) {
        String value = redis.opsForValue().get(key(userId));
        return value == null ? 0 : Long.parseLong(value);
    }

    private String key(UUID userId) {
        return KEY_PREFIX + userId;
    }
}
