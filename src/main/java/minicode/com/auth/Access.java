package minicode.com.auth;

import minicode.com.exception.ForbiddenAccessException;
import minicode.com.exception.UnauthenticatedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.function.Predicate;

public final class Access {

    private Access() {
    }

    public static boolean isAuthenticated(Authentication authentication) {
        return authentication != null
                && authentication.isAuthenticated()
                // AnonymousAuthenticationToken.isAuthenticated() is true, so
                // exclude it to report 401 instead of 403.
                && !(authentication instanceof AnonymousAuthenticationToken);
    }

    public static boolean isAdmin(Authentication authentication) {
        return hasAuthority(authentication, "ROLE_ADMIN");
    }

    public static boolean isUser(Authentication authentication) {
        return hasAuthority(authentication, "ROLE_USER");
    }

    public static void requireAuthenticated(Authentication authentication) {
        if (!isAuthenticated(authentication)) {
            throw UnauthenticatedException.authenticationRequired();
        }
    }

    public static void requireAdmin(Authentication authentication) {
        requireRole(authentication, Access::isAdmin, ForbiddenAccessException.adminRequired());
    }

    public static void requireUser(Authentication authentication) {
        requireRole(authentication, Access::isUser, ForbiddenAccessException.userRequired());
    }

    public static void requireAdminOrUser(Authentication authentication) {
        requireRole(
                authentication,
                auth -> isAdmin(auth) || isUser(auth),
                ForbiddenAccessException.adminOrUserRequired()
        );
    }

    private static void requireRole(
            Authentication authentication,
            Predicate<Authentication> hasRole,
            ForbiddenAccessException denial
    ) {
        if (hasRole.test(authentication)) {
            return;
        }
        if (!isAuthenticated(authentication)) {
            throw UnauthenticatedException.authenticationRequired();
        }
        throw denial;
    }

    private static boolean hasAuthority(Authentication authentication, String authority) {
        return isAuthenticated(authentication)
                && authentication.getAuthorities().stream()
                        .anyMatch(granted -> granted.getAuthority().equals(authority));
    }
}
