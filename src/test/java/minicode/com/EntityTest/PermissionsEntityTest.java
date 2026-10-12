package minicode.com.EntityTest;

import minicode.com.entity.Roles.Permissions;
import minicode.com.entity.Roles.Role;
import minicode.com.entity.Roles.UserPermissions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
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

    @Test
    public void testThatManyPermissionsCanHaveManyRoles() {
        // Given two permissions
        Permissions permission1 = new Permissions();
        permission1.setName(UserPermissions.VIEW_STATISTICS.name());
        Permissions permission2 = new Permissions();
        permission2.setName(UserPermissions.VIEW_USERS.name());
        entityManager.persistAndFlush(permission1);
        entityManager.persistAndFlush(permission2);

        // And two roles that own them (Role.permissions is the owning side)
        Role role1 = new Role();
        role1.setName("Admin");
        role1.setPermissions(Set.of(permission1));
        entityManager.persistAndFlush(role1);

        Role role2 = new Role();
        role2.setName("User");
        role2.setPermissions(Set.of(permission2));
        entityManager.persistAndFlush(role2);

        // Then each permission is linked to its role
        entityManager.clear(); // read the join table back from the database
        Permissions reloadedPermission1 = entityManager.find(Permissions.class, permission1.getId());
        Permissions reloadedPermission2 = entityManager.find(Permissions.class, permission2.getId());

        List<Role> roles = new ArrayList<>(reloadedPermission1.getRoles());
        List<Role> roles2 = new ArrayList<>(reloadedPermission2.getRoles());
        Role fetchedRole1 = roles.get(0);
        Role fetchedRole2 = roles2.get(0);

        assertThat(fetchedRole1.getName()).isEqualTo("Admin");
        assertThat(fetchedRole2.getName()).isEqualTo("User");
    }

}
