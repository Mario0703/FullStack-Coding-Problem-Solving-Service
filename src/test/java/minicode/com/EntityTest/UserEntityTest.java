package minicode.com.EntityTest;

import jakarta.persistence.PersistenceException;
import minicode.com.entity.Roles.UserRole;
import minicode.com.entity.User.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
public class UserEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void savingUserWithNullEmailThrows() {
        // Given a user whose required email is null
        User user = new User();
        user.setUserName("testUser");
        user.setEmail(null);
        user.setPassword("password");
        user.setRole(UserRole.USER);

        // When it is saved and flushed to the database
        // Then the null required field is rejected
        assertThrows(PersistenceException.class, () -> entityManager.persistAndFlush(user));
    }

}
