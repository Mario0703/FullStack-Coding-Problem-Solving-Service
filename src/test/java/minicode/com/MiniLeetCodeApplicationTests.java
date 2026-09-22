package minicode.com;

import minicode.com.security.RateLimitingFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class MiniLeetCodeApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void blocksSignupAfterTenRequestsFromTheSameIp()
            throws ServletException, IOException {

        StringRedisTemplate redisTemplate = mock(StringRedisTemplate.class);
        RateLimitingFilterTestSupport testSupport = new RateLimitingFilterTestSupport(redisTemplate);
        int MAX_REQUESTS_PER_MINUTE = 10;
        String WINDOW_SECONDS = "60";


        RateLimitingFilter rateLimiter = new RateLimitingFilter(redisTemplate, WINDOW_SECONDS, MAX_REQUESTS_PER_MINUTE);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);
        StringWriter responseBody = new StringWriter();

        when(request.getRemoteAddr()).thenReturn("192.0.2.10");
        when(response.getWriter()).thenReturn(new PrintWriter(responseBody));
        testSupport.returnIncrementingCounts("rate-limit:signup:192.0.2.10");

        for (int requestNumber = 0; requestNumber < 11; requestNumber++) {
            rateLimiter.doFilter(request, response, chain);
        }

        verify(chain, times(10)).doFilter(request, response);
        verify(response).setStatus(429);
        assertEquals(
                "Too many requests. Please try again later.",
                responseBody.toString()
        );
    }

    @Test
    void registersLimiterForSignupEndpoint() {
        StringRedisTemplate redisTemplate = mock(StringRedisTemplate.class);

        FilterRegistrationBean<RateLimitingFilter> registration =
                new MiniLeetCodeApplication()
                        .rateLimiterFilterForUserSignup(redisTemplate);

        assertEquals(Set.of("/api/user/signup"), registration.getUrlPatterns());
    }

    private record RateLimitingFilterTestSupport(StringRedisTemplate redisTemplate) {

        private void returnIncrementingCounts(String key) {
                AtomicInteger count = new AtomicInteger();
                ValueOperations<String, String> valueOperations = mock(ValueOperations.class);
                when(redisTemplate.opsForValue()).thenReturn(valueOperations);
                when(valueOperations.increment(key, 1)).thenAnswer(invocation -> (long) count.incrementAndGet());
            }
        }
}
