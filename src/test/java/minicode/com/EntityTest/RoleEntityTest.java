package minicode.com.EntityTest;

import jakarta.persistence.PersistenceException;
import minicode.com.entity.Roles.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
public class RoleEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void savingRoleWithNullNameThrows() {
        // Given a role whose required name is null
        Role role = new Role();
        role.setName(null);

        // When it is saved and flushed to the database
        // Then the null required field is rejected
        assertThrows(PersistenceException.class, () -> entityManager.persistAndFlush(role));
    }

}
