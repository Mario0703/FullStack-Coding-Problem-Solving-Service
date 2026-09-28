package minicode.com.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "admin_users", schema = "public") // provides the name of the table
@Getter
@Setter
public class AdminUser {

    public AdminUser(String adminUserName, String adminPassword) {
        this.adminUserName = adminUserName;
        this.adminPassword = adminPassword;

    }
    protected AdminUser() {
    }

    private String adminUserName;

    @JsonIgnore
    private String adminPassword;
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID adminId;
}
