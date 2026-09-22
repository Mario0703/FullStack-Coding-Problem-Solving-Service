package minicode.com.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class changeUserName {

    @NotBlank(message = "All users must have a new username")
    @Size(max = 100, message = "Username cannot exceed 100 characters")
    private String newUserName;
}
