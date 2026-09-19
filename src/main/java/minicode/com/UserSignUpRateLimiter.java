package minicode.com;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.LongSupplier;

public class UserSignUpRateLimiter implements Filter{

    private static final int MAX_REQUESTS_PER_MINUTE = 10;
    private static final long WINDOW_MILLIS = Duration.ofMinutes(1).toMillis();
    private static final int SC_TOO_MANY_REQUESTS = 429;

    private final Map<String, RequestWindow> requestWindows = new ConcurrentHashMap<>();
    private final LongSupplier currentTimeMillis;

    public UserSignUpRateLimiter() {
        this(System::currentTimeMillis);
    }

    UserSignUpRateLimiter(LongSupplier currentTimeMillis) {
        this.currentTimeMillis = currentTimeMillis;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String clientIp = httpRequest.getRemoteAddr();
        long now = currentTimeMillis.getAsLong();
        RequestWindow window = requestWindows.computeIfAbsent(
                clientIp,
                ignored -> new RequestWindow(now)
        );

        boolean requestAllowed;

        synchronized (window) {
            if (now - window.startedAt >= WINDOW_MILLIS) {
                window.startedAt = now;
                window.requestCount = 0;
            }

            window.requestCount++;
            requestAllowed = window.requestCount <= MAX_REQUESTS_PER_MINUTE;
        }

        if (!requestAllowed) {
            httpResponse.setStatus(SC_TOO_MANY_REQUESTS);
            httpResponse.setHeader("Retry-After", "60");
            httpResponse.getWriter().write(
                    "Too many requests. Please try again later."
            );
            return;
        }

        chain.doFilter(request, response);
    }

    private static class RequestWindow {
        private long startedAt;
        private int requestCount;

        private RequestWindow(long startedAt) {
            this.startedAt = startedAt;
        }
    }
}
