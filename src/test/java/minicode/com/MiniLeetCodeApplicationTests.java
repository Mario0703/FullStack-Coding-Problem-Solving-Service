package minicode.com;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicLong;

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

        UserSignUpRateLimiter rateLimiter = new UserSignUpRateLimiter();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);
        StringWriter responseBody = new StringWriter();

        when(request.getRemoteAddr()).thenReturn("192.0.2.10");
        when(response.getWriter()).thenReturn(new PrintWriter(responseBody));

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
    void allowsSignupAgainAfterOneMinute()
            throws ServletException, IOException {

        AtomicLong currentTime = new AtomicLong(0);
        UserSignUpRateLimiter rateLimiter =
                new UserSignUpRateLimiter(currentTime::get);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        when(request.getRemoteAddr()).thenReturn("192.0.2.20");

        for (int requestNumber = 0; requestNumber < 10; requestNumber++) {
            rateLimiter.doFilter(request, response, chain);
        }

        currentTime.addAndGet(Duration.ofMinutes(1).toMillis());
        rateLimiter.doFilter(request, response, chain);

        verify(chain, times(11)).doFilter(request, response);
    }

}
