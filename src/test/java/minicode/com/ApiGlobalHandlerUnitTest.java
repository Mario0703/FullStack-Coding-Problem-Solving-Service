package minicode.com;

import minicode.com.exception.ApiGlobalHandler;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.mock.http.MockHttpInputMessage;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ApiGlobalHandlerUnitTest {

    @Test
    public void givenAnUnreadableRequestBodyWhenTheRequestIsHandledThenItRespondsWithBadRequest() {
        // given a request body that cannot be read, e.g. an unknown staff role
        ApiGlobalHandler handler = new ApiGlobalHandler();
        HttpMessageNotReadableException unreadableBody = new HttpMessageNotReadableException(
                "Cannot deserialize staff role", unreadableRequestBody());

        // when the handler handles it
        ResponseEntity<String> response = handler.handleUnreadableBody(unreadableBody);

        // then the response is a bad request with a clear message
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("The request body is not valid", response.getBody());
    }

    private MockHttpInputMessage unreadableRequestBody() {
        return new MockHttpInputMessage(InputStream.nullInputStream());
    }
}