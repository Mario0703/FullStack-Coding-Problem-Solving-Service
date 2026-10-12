package minicode.com.dto.Admin;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AdminLoginRequest {
    @NotBlank(message = "Admin Username is required")
    private String adminUserName;

    @NotBlank(message = "Admin Password is required")
    private String adminPassword;
}




