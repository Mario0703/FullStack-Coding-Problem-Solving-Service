package minicode.com.dto;

import java.util.UUID;

public record AdminUserStatistics(
        UUID id,
        String name,
        String email,
        long numberOfSubmissions,
        long numberOfSolvedChallenges
) {
}
