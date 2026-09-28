package minicode.com.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;
@Getter
@Setter
@NoArgsConstructor
public class AdminSignUpRequest {

    @NotBlank(message = "All users must have a username")
    private String adminUserName;

    @NotBlank(message = "All users must have a password")
    @Size(min = 12, max = 72, message = "Password must be 12 to 72 characters")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{12,}$",
            message = "Password must contain an uppercase letter, lowercase letter, digit, and special character"
    )
    private String adminPassword;
}
