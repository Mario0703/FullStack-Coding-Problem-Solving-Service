package minicode.com.dto.Ai;

import jakarta.validation.constraints.NotBlank;

/**
 * A single miniHelper hint request from the frontend. The browser only sends
 * what the assistant needs; the challenge data and the OpenAI call stay on the
 * server.
 */
public record MiniHelperRequest(
        @NotBlank(message = "challengeId is required") String challengeId,
        @NotBlank(message = "question is required") String question,
        String code,
        String language,
        String conversationId
) {
}
