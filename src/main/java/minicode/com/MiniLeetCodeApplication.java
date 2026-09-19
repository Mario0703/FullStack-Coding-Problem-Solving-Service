package minicode.com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MiniLeetCodeApplication {

    public static void main(String[] args) {
        SpringApplication.run(MiniLeetCodeApplication.class, args);
    }

    @Bean
    public FilterRegistrationBean<UserSignUpRateLimiter> rateLimiterFilerForUserSignUp() {
        FilterRegistrationBean<UserSignUpRateLimiter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new UserSignUpRateLimiter());
        registration.addUrlPatterns("/api/users/sign-up");
        return registration;
    }
}
