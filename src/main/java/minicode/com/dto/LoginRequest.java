package minicode.com.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginRequest {

    private String userName;

    private String email;

    @NotBlank(message = "All users must have a password")
    private String password;

}
