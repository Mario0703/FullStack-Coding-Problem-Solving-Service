package minicode.com.dto.User;

import java.util.UUID;

public record SolverResponse(UUID userId, String userName, String code) {
}
