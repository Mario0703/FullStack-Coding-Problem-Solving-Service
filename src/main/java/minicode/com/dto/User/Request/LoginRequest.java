package minicode.com.dto.User.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginRequest {

    @NotBlank(message = "All users must have a username")
    private String userName;

    @NotBlank(message = "All users must have an email")
    private String email;

    @NotBlank(message = "All users must have a password")
    private String password;


}




