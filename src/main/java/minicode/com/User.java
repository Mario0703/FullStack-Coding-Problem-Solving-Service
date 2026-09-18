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

    protected User() {
    }

    public User(String userName, String email, String password) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.userId = UUID.randomUUID();
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


}
