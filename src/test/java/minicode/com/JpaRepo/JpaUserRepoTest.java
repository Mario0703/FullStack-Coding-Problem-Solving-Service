package minicode.com.JpaRepo;

import minicode.com.entity.Roles.UserRole;
import minicode.com.entity.User.User;
import minicode.com.repository.UserRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class JpaUserRepoTest {

    @Autowired
    private UserRepo userRepo;

    @BeforeEach
    public void setUp(){
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
        userRepo.save(user);
    }

    @Test
    public void saveAndFindByIdTest() {
        UUID userId = userRepo.findAll().get(0).getUserId();

        User foundUser = userRepo.findById(userId).orElse(null);
        assertThat(foundUser.getUserName().equals("testUser"));
    }

    @Test
    public void findFirstByUserNameTest() {
        Optional<User> foundUser = userRepo.findFirstByUserName("testUser");
        assertThat(foundUser.isPresent()).isTrue();
        assertThat(foundUser.get().getEmail().equals("testuser@example.com"));
    }

    @Test
    public void findFirstByEmailTest() {
        Optional<User> foundUser = userRepo.findFirstByEmail("testuser@example.com");
        assertThat(foundUser.isPresent()).isTrue();
        assertThat(foundUser.get().getUserName().equals("testUser"));
    }

    @Test
    public void addTokenUsageTest() {
        Optional<User> foundUser = userRepo.findFirstByEmail("testuser@example.com");
        UUID userId = foundUser.get().getUserId();
        long tokens = 1;
        userRepo.addTokenUsage(userId, tokens);
        User updatedUser = userRepo.findById(userId).orElse(null);
        assertThat(updatedUser.getTokenUsage()).isEqualTo(1);
    }



    @AfterEach // release data after each test to avoid interference between tests
    public void tearDown() {
        userRepo.deleteAll();
    }

}
