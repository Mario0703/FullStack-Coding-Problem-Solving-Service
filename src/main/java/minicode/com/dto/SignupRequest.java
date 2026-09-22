package minicode.com.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class SignupRequest {



    @NotBlank(message = "All users must have a username")
    @Size(max = 100, message = "Username cannot exceed 100 characters")
    private String userName;

    @NotBlank(message = "All users must have an email")
    @Email(message = "Email should be valid")
    @Size(max = 100, message = "Email cannot exceed 100 characters")
    private String email;


    @NotBlank(message = "All users must have a password")
    @Size(min = 8, max = 72, message = "Password must be 8 to 72 characters")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{8,}$",
            message = "Password must contain an uppercase letter, lowercase letter, and digit"
    )
    private String password;

    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {

        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }




}
