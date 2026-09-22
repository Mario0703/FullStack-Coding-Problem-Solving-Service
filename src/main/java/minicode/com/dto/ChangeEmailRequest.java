package minicode.com.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ChangeEmailRequest {

    @NotBlank(message = "All users must have a new email")
    @Email(message = "New email should be valid")
    @Size(max = 100, message = "Email cannot exceed 100 characters")
    private String newEmail;

    public String getNewEmail() {
        return newEmail;
    }

    public void setNewEmail(String newEmail) {
        this.newEmail = newEmail;
    }
}
