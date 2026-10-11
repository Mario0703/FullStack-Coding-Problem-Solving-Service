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
    public void savingUserWithNullUserNameThrows() {
        // Given a user whose required username is null
        User user = new User();
        user.setUserName(null);
        assertThrows(PersistenceException.class, () -> entityManager.persistAndFlush(user));
    }

    @Test
    public void savingUserWithNullEmailThrows() {
        // Given a user whose required email is null
        User user = new User();
        user.setEmail(null);
        assertThrows(PersistenceException.class, () -> entityManager.persistAndFlush(user));
    }

    @Test
    public void savingUserWithNullPasswordThrows() {
        // Given a user whose required password is null
        User user = new User();
        user.setPassword(null);
        assertThrows(PersistenceException.class, () -> entityManager.persistAndFlush(user));
    }

    @Test
    public void savingUserWithNullRoleThrows() {
        // Given a user whose required role is null
        User user = new User();
        user.setRole(null);
        assertThrows(PersistenceException.class, () -> entityManager.persistAndFlush(user));
    }



}
