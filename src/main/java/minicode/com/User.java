package minicode.com;

import jakarta.persistence.*;

@Entity // This class should be tracked as a table
@Table(name = "users") // provides the name of the table
public class User {
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    @Id // This is the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // This is auto incremented
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String userName;
    private long userId;
    private String email;
}
