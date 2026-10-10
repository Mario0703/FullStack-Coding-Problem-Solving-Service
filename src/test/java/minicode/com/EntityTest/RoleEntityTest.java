package minicode.com.EntityTest;

import minicode.com.entity.Roles.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@DataJpaTest
public class RoleEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testRoleEntity() {
        String roleName = "ADMIN";
        Role role = new Role(roleName);
        Role savedRole = entityManager.persistAndFlush(role);
        entityManager.clear();
        assertThat(savedRole.getName()).isEqualTo(roleName);

    }

}
