package minicode.com.dto.User.Request;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A bio may be cleared by sending a blank value, so unlike the username or
 * email it only bounds the length.
 */
@Getter
@Setter
@NoArgsConstructor
public class ChangeBioRequest {
    @Size(max = 500, message = "Bio cannot exceed 500 characters")
    private String newBio;
}