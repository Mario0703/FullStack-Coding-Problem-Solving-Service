package minicode.com.EntityTest;

import jakarta.persistence.PersistenceException;
import minicode.com.entity.Roles.Role;
import minicode.com.entity.Roles.UserRole;
import minicode.com.entity.User.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
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
    @Test
    public void testThatManyUsersCanHaveManyRolesRelation(){
        // given a role shared by 2 users
        Role role = new Role();
        role.setName("USER");

        User user1 = new User();
        user1.setUserName("user1");
        user1.setEmail("user1@example.com");
        user1.setPassword("password1");
        user1.setRole(UserRole.USER);
        user1.setRoles(Set.of(role));

        User user2 = new User();
        user2.setUserName("user2");
        user2.setEmail("user2@example.com");
        user2.setPassword("password2");
        user2.setRole(UserRole.USER);
        user2.setRoles(Set.of(role));

        // When they are saved and flushed to the database
        entityManager.persistAndFlush(role);
        entityManager.persistAndFlush(user1);
        entityManager.persistAndFlush(user2);

        // Then we can retrieve the role and check the users it is associated with
        entityManager.clear(); // read the join table back from the database
        Role reloadedRole = entityManager.find(Role.class, role.getId());


        //Roles -> Users relation check
        assertThat(reloadedRole.getUsers())
                .extracting(User::getUserId)
                .containsExactlyInAnyOrder(user1.getUserId(), user2.getUserId());

        // Users -> Roles relation check
        User reloadedUser1 = entityManager.find(User.class, user1.getUserId());

        assertThat(reloadedUser1.getRoles())
                .extracting(Role::getId)
                .containsExactly(role.getId());
    }

    @Test
    public void getTotalChallengesSolved(){
        int numberOfMediumChallengesSolved = 5;
        int numberOfEasyChallengesSolved = 10;
        int numberOfHardChallengesSolved = 0;
        User user2 = new User();
        user2.setNumberOfEasyChallengesSolved(numberOfEasyChallengesSolved);
        user2.setNumberOfMediumChallengesSolved(numberOfMediumChallengesSolved);
        user2.setNumberOfHardChallengesSolved(numberOfHardChallengesSolved);

        int totalChallengesSolved = user2.getTotalChallengesSolved();
        assertThat(totalChallengesSolved).isEqualTo(15);
    }

}
