package minicode.com;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class UserSignUpRateLimiter implements Filter{

    private static final int MAX_REQUESTS_PER_MINUTE = 10;
    private final Map<String, AtomicInteger> requestCounts = new ConcurrentHashMap<>();
    private final int SC_TOO_MANY_REQUESTS =  429;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String clientIp = httpRequest.getRemoteAddr();
        requestCounts.putIfAbsent(clientIp, new AtomicInteger(0));
        int currentCount = requestCounts.get(clientIp).incrementAndGet();

        if (currentCount > MAX_REQUESTS_PER_MINUTE) {
            httpResponse.setStatus(SC_TOO_MANY_REQUESTS);
            httpResponse.getWriter().write(
                    "Too many requests. Please try again later."
            );
            return;
        }

        chain.doFilter(request, response);
    }
}
