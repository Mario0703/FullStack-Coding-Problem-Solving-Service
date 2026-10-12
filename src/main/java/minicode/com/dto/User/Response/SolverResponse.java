package minicode.com.dto.User.Response;

import java.time.Instant;
import java.util.UUID;

public record SolverResponse(UUID userId, String userName, String code, String language, Instant solvedAt) {
}
