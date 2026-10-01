package minicode.com.auth;

import java.util.UUID;

public record userResponse(UUID userId, String userName, String email, String role) {

}