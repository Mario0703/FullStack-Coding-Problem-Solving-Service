package minicode.com.EntityTest;

import jakarta.persistence.PersistenceException;
import minicode.com.entity.Roles.Permissions;
import minicode.com.entity.Roles.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
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
    @Test
    public void testThatManyRolesCanHaveManyPermissions(){
        //given two roles and two permissions
        Role role1 = new Role();
        Role role2 = new Role();
        Permissions permission1 = new Permissions();
        Permissions permission2 = new Permissions();
        role1.setName("Admin");
        role2.setName("User");
        permission1.setName("READ");
        permission2.setName("WRITE");
        entityManager.persistAndFlush(role1);
        entityManager.persistAndFlush(role2);
        entityManager.persistAndFlush(permission1);
        entityManager.persistAndFlush(permission2);
        // roles -> permissions
        permission1.getRoles().add(role1);
        permission2.getRoles().add(role2);
        entityManager.persistAndFlush(permission1);
        entityManager.persistAndFlush(permission2);
        // Find the roles
        List<Role> roles = new ArrayList<>(permission1.getRoles());
        List<Role> roles2 = new ArrayList<>(permission2.getRoles());
        Role fetchedRole1 = roles.get(0);
        Role fetchedRole2 = roles2.get(0);
        assertThat(fetchedRole1.getName().equals("Admin"));
        assertThat(fetchedRole2.getName().equals("User"));

    }

}
