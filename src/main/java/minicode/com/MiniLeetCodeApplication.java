package minicode.com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootApplication
public class MiniLeetCodeApplication {

    public static void main(String[] args) {
        SpringApplication.run(MiniLeetCodeApplication.class, args);
    }

    @Bean
    public FilterRegistrationBean<RateLimitingFilter> rateLimiterFilterForUserSignup(
            StringRedisTemplate redisTemplate
    ) {
        FilterRegistrationBean<RateLimitingFilter> registration = new FilterRegistrationBean<>();
        int MAX_REQUESTS_PER_MINUTE = 1000;
        String WINDOW_SECONDS = "60";
        registration.setFilter(new RateLimitingFilter(redisTemplate, WINDOW_SECONDS, MAX_REQUESTS_PER_MINUTE));
        registration.addUrlPatterns("/api/user/signup");
        return registration;
    }
}
