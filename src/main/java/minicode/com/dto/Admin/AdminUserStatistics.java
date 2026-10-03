package minicode.com.dto.Admin;

import java.util.UUID;

public record AdminUserStatistics(
        UUID id,
        String name,
        String email,
        long numberOfSubmissions,
        long numberOfSolvedChallenges
) {
}




