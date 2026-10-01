package minicode.com.dto.User;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangeUserNameRequest(
        @NotBlank(message = "All users must have a new username")
        @Size(max = 100, message = "Username cannot exceed 100 characters")
        String newUserName
) {
}




