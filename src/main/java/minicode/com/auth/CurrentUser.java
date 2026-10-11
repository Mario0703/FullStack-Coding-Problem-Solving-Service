package minicode.com.auth;

import minicode.com.exception.UnauthenticatedException;
import minicode.com.repository.UserRepo;
import org.springframework.security.core.Authentication;

import java.util.UUID;

/**
 * Resolves the authenticated {@link Authentication} to the persisted user id.
 * Only sessions holding the user role are accepted; the lookup is by username.
 */
public final class CurrentUser {

    private CurrentUser() {
    }

    public static UUID id(UserRepo userRepo, Authentication authentication) {
        Access.requireUser(authentication);
        return userRepo.findFirstByUserName(authentication.getName())
                .orElseThrow(UnauthenticatedException::authenticationRequired)
                .getUserId();
    }
}
