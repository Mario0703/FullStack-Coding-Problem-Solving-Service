package minicode.com.service.User;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

/**
 * Single-use password reset tokens kept in Redis. Redis expires each key
 * automatically, which is what gives a token its five-minute lifetime.
 */
@Service
public class ResetTokenStore {

    private static final String KEY_PREFIX = "password-reset:token:";
    private static final Duration TOKEN_LIFETIME = Duration.ofMinutes(5);

    private final StringRedisTemplate redis;

    public ResetTokenStore(StringRedisTemplate redis) {
        this.redis = redis;
    }

    public String issueFor(String email) {
        String token = UUID.randomUUID().toString();
        redis.opsForValue().set(key(token), email, TOKEN_LIFETIME);
        return token;
    }

    public Optional<String> consume(String token) {
        String email = redis.opsForValue().get(key(token));
        if (email == null) {
            return Optional.empty();
        }

        redis.delete(key(token));
        return Optional.of(email);
    }

    private String key(String token) {
        return KEY_PREFIX + token;
    }
}