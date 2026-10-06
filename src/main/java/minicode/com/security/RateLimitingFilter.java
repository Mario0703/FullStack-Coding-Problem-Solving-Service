package minicode.com.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.io.IOException;
import java.time.Duration;

public class RateLimitingFilter implements Filter {

    private final String WINDOW_SECONDS;
    private final int MAX_REQUESTS_PER_MINUTE;
    private final StringRedisTemplate redisTemplate;

    public RateLimitingFilter(StringRedisTemplate redisTemplate, String windowSeconds, int MAX_REQUESTS_PER_MINUTE) {
        this.redisTemplate = redisTemplate;
        this.WINDOW_SECONDS = windowSeconds;
        this.MAX_REQUESTS_PER_MINUTE = MAX_REQUESTS_PER_MINUTE;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String clientIp = httpRequest.getRemoteAddr();
        String redisKey = "rate-limit:signup:" + clientIp;

        Long numberOfRequestsSent = redisTemplate.opsForValue().increment(redisKey, 1);

        boolean isFirstRequest = numberOfRequestsSent != null && numberOfRequestsSent == 1;
        if (isFirstRequest) {
            //Start a 1 minutte timer on the first request
            redisTemplate.expire(redisKey, Duration.ofMinutes(1));
        }

        if (numberOfRequestsSent > MAX_REQUESTS_PER_MINUTE) {
            httpResponse.setStatus(429);
            httpResponse.setHeader("Retry-After", WINDOW_SECONDS);
            httpResponse.getWriter().write("Too many requests. Please try again later.");
            return;
        }

        chain.doFilter(request, response);
    }
}
