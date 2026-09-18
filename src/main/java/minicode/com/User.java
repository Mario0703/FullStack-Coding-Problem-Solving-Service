package minicode.com;

import jakarta.persistence.*;
import java.util.UUID;

@Entity // This class should be tracked as a table
@Table(name = "users") // provides the name of the table
public class User {

    private String userName;
    private String email;
    private String password;
    private UUID userId;

    public User(String userName, String email, String password) {
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    public String getUserName() {

        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Id // This is the primary key
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
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
