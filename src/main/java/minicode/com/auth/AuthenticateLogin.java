package minicode.com.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;

/**
 * Shared login flow for the user and admin login endpoints: build a
 * username/password token, authenticate it, and persist the resulting security
 * context to the HTTP session (so the JSESSIONID cookie is issued).
 */
public final class AuthenticateLogin {

    private AuthenticateLogin() {
    }

    public static Authentication authenticate(
            AuthenticationManager authenticationManager,
            String identifier,
            String password
    ) {
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(identifier, password)
        );
    }

    public static Authentication login(
            AuthenticationManager authenticationManager,
            SecurityContextRepository securityContextRepository,
            String identifier,
            String password,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        Authentication authentication = authenticate(authenticationManager, identifier, password);
        saveAuthentication(securityContextRepository, authentication, request, response);
        return authentication;
    }

    private static void saveAuthentication(
            SecurityContextRepository securityContextRepository,
            Authentication authentication,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
        securityContextRepository.saveContext(context, request, response);
    }
}
