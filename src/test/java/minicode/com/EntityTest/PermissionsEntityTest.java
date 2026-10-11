package minicode.com.EntityTest;

import minicode.com.entity.Roles.Permissions;
import minicode.com.entity.Roles.UserPermissions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DataJpaTest
public class PermissionsEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testPermissionCreationForAValidPermission() {
        // Given a permission with a valid name
        String name = UserPermissions.VIEW_STATISTICS.name();
        Permissions permission = new Permissions();
        permission.setName(name);

        // And it is saved and flushed to the database
        Permissions saved = entityManager.persistAndFlush(permission);
        entityManager.clear(); // read the row back from the database, not the cache

        // Then it can be read back with the same properties
        Permissions retrivedPermissions = entityManager.find(Permissions.class, saved.getId());
        assertThat(retrivedPermissions).isNotNull();
        assertThat(retrivedPermissions.getId()).isEqualTo(saved.getId());
        assertThat(retrivedPermissions.getName()).isEqualTo(name);
    }

    @Test
    public void savingPermissionWithNullNameSucceeds() {
        // Given a permission whose nullable name is null
        Permissions permission = new Permissions();
        permission.setName(null);

        // When it is saved and flushed to the database
        // Then the null nullable field is accepted
        assertDoesNotThrow(() -> {
            entityManager.persistAndFlush(permission);
        });
    }

}
