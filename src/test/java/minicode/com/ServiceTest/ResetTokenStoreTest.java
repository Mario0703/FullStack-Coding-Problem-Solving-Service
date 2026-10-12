package minicode.com.ServiceTest;

import minicode.com.service.User.ResetTokenStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Duration;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ResetTokenStoreTest {

    private static final String EMAIL = "user@example.com";

    private StringRedisTemplate redis;
    private ValueOperations<String, String> valueOperations;
    private ResetTokenStore tokenStore;

    @BeforeEach
    @SuppressWarnings("unchecked")
    public void setUp() {
        redis = mock(StringRedisTemplate.class);
        valueOperations = mock(ValueOperations.class);
        when(redis.opsForValue()).thenReturn(valueOperations);
        tokenStore = new ResetTokenStore(redis);
    }

    @Test
    public void givenAnEmailWhenATokenIsIssuedThenItIsStoredForFiveMinutes() {
        // given an email

        // when a reset token is issued for it
        String token = tokenStore.issueFor(EMAIL);

        // then the token is stored together with the email for exactly five minutes
        assertFalse(token.isBlank());
        verify(valueOperations).set("password-reset:token:" + token, EMAIL, Duration.ofMinutes(5));
    }

    @Test
    public void givenAStoredTokenWhenItIsConsumedThenItsEmailIsReturnedAndTheTokenIsRemoved() {
        // given a stored reset token
        when(valueOperations.get("password-reset:token:known-token")).thenReturn(EMAIL);

        // when the token is consumed
        Optional<String> email = tokenStore.consume("known-token");

        // then its email is returned and the token can never be used again
        assertTrue(email.isPresent());
        assertEquals(EMAIL, email.get());
        verify(redis).delete("password-reset:token:known-token");
    }

    @Test
    public void givenAnExpiredOrUnknownTokenWhenItIsConsumedThenNoEmailIsReturned() {
        // given a token that expired or never existed
        when(valueOperations.get("password-reset:token:expired-token")).thenReturn(null);

        // when the token is consumed
        Optional<String> email = tokenStore.consume("expired-token");

        // then no email is returned and nothing is removed from the store
        assertTrue(email.isEmpty());
        verify(redis, never()).delete(anyString());
    }
}