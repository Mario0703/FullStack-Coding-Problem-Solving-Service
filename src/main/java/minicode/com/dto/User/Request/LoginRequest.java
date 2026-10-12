package minicode.com.dto.User.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import minicode.com.validation.EitherEmailOrUserName;

@Getter
@Setter
@NoArgsConstructor
@EitherEmailOrUserName
public class LoginRequest {

    private String userName;

    private String email;

    @NotBlank(message = "All users must have a password")
    private String password;


}
