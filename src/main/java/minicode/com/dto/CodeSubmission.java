package minicode.com.dto;

import java.util.UUID;

public record CodeSubmission(
        UUID userId,
        UUID challengeId,
        String language,
        String code
) {
}
