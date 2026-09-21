package minicode.com.dto;

import jakarta.validation.constraints.NotBlank;

public class LoginRequest {

    private String userName;

    private String email;

    @NotBlank(message = "All users must have a password")
    private String password;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
