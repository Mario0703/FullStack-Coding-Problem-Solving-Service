package minicode.com.config;

import minicode.com.security.RateLimitingFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * Rate-limiting filters for the public endpoints. Kept out of
 * {@code MiniLeetCodeApplication} so slice tests (e.g. {@code @DataJpaTest})
 * do not need a Redis connection just to start a context.
 */
@Configuration
public class RateLimitingConfig {

    private static final int MAX_REQUESTS_PER_MINUTE = 1000;
    private static final String WINDOW_SECONDS = "60";

    @Bean
    public FilterRegistrationBean<RateLimitingFilter> rateLimiterFilterForUserSignup(
            StringRedisTemplate redisTemplate
    ) {
        return rateLimitingFilter(redisTemplate, "/api/user/signup");
    }

    @Bean
    public FilterRegistrationBean<RateLimitingFilter> rateLimiterFilterForUserLogin(
            StringRedisTemplate redisTemplate
    ) {
        return rateLimitingFilter(redisTemplate, "/api/user/login");
    }

    @Bean
    public FilterRegistrationBean<RateLimitingFilter> rateLimiterFilterForForgottenPassword(
            StringRedisTemplate redisTemplate
    ) {
        return rateLimitingFilter(redisTemplate, "/api/user/forgot-password");
    }

    @Bean
    public FilterRegistrationBean<RateLimitingFilter> rateLimiterFilterForAi(
            StringRedisTemplate redisTemplate
    ) {
        return rateLimitingFilter(redisTemplate, "/api/ai/hint");
    }

    private FilterRegistrationBean<RateLimitingFilter> rateLimitingFilter(
            StringRedisTemplate redisTemplate,
            String urlPattern
    ) {
        FilterRegistrationBean<RateLimitingFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new RateLimitingFilter(redisTemplate, WINDOW_SECONDS, MAX_REQUESTS_PER_MINUTE));
        registration.addUrlPatterns(urlPattern);
        return registration;
    }
}
