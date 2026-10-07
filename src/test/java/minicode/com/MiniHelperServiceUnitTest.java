package minicode.com;

import minicode.com.dto.Ai.MiniHelperRequest;
import minicode.com.service.Ai.MiniHelperService;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/** Pure unit tests for miniHelper that need no database, Redis or OpenAI access. */
class MiniHelperServiceUnitTest {

    /** A service with no API key, so its OpenAI client is null. */
    private MiniHelperService unconfiguredService() {
        return new MiniHelperService(null, null, null, "", "gpt-4o-mini");
    }

    @Test
    void reportsServiceUnavailableWhenNoApiKeyConfigured() {
        MiniHelperService service = unconfiguredService();
        MiniHelperRequest request =
                new MiniHelperRequest("challenge", "help", null, null, "session-1");

        ResponseStatusException error = assertThrows(
                ResponseStatusException.class,
                () -> service.ask(request, UUID.randomUUID())
        );

        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, error.getStatusCode());
    }

    @Test
    void clearingAnUnknownOrBlankConversationIsANoOp() {
        MiniHelperService service = unconfiguredService();

        assertDoesNotThrow(() -> service.clearConversation(UUID.randomUUID(), "session-1"));
        assertDoesNotThrow(() -> service.clearConversation(UUID.randomUUID(), ""));
        assertDoesNotThrow(() -> service.clearConversation(UUID.randomUUID(), null));
    }
}
