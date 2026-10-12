package minicode.com.dto.Admin;

import java.util.UUID;

public record AdminUserStatisticsResponse(
        UUID id,
        String name,
        String email,
        long numberOfSubmissions,
        long numberOfSolvedChallenges
) {
}




