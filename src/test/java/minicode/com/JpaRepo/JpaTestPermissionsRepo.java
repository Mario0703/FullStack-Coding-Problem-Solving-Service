package minicode.com.JpaRepo;

import minicode.com.entity.Roles.Permissions;
import minicode.com.entity.Roles.Role;
import minicode.com.entity.Roles.UserRole;
import minicode.com.entity.User.User;
import minicode.com.repository.PermissionsRepo;
import minicode.com.repository.RoleRepo;
import minicode.com.repository.UserRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class JpaTestPermissionsRepo {

    @Autowired
    private PermissionsRepo permissionsRepo;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void findByNameReturnsPermissionWhenRoleHasPermission() {
        Permissions permission = new Permissions();
        permission.setName("VIEW_CHALLENGES");
        Permissions savedPermission = permissionsRepo.save(permission);

        Role role = new Role();
        role.setName("ROLE_USER");
        role.setPermissions(Set.of(savedPermission));
        Role savedRole = roleRepo.save(role);

        User user = new User();
        user.setUserName("testUser");
        user.setEmail("testuser@example.com");
        user.setPassword("testpassword");
        user.setRole(UserRole.USER);
        user.setScore(0);
        user.setNumberOfEasyChallengesSolved(0);
        user.setNumberOfMediumChallengesSolved(0);
        user.setNumberOfHardChallengesSolved(0);
        user.setTokenUsage(0);
        user.setRoles(Set.of(savedRole));
        userRepo.saveAndFlush(user);

        // Reload from the database so the inverse (mappedBy) collection reflects
        // the join table instead of the still-empty in-memory collection.
        entityManager.clear();

        Optional<Permissions> foundPermission = permissionsRepo.findByName("VIEW_CHALLENGES");

        assertThat(foundPermission.isPresent()).isTrue();
        assertThat(foundPermission.get().getName()).isEqualTo("VIEW_CHALLENGES");
        assertThat(foundPermission.get().getRoles().size()).isEqualTo(1);
    }

}
