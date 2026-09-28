package minicode.com.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "admin_users", schema = "public") // provides the name of the table
@Getter
@Setter
public class AdminUser {

    private String adminUserName;
    private String adminPassword;
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID adminId;
}
