package minicode.com.dto.User.Request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChangeEmailRequest {
        @NotBlank(message = "All users must have a new email")
        @Email(message = "New email should be valid")
        @Size(max = 100, message = "Email cannot exceed 100 characters")
        private String newEmail;
}



