package minicode.com.dto.User.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChangeUserNameRequest {
    @NotBlank(message = "All users must have a new username")
    @Size(max = 100, message = "Username cannot exceed 100 characters")
    private String newUserName;

}



