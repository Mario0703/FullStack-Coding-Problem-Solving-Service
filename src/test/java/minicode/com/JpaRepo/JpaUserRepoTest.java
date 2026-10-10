package minicode.com.JpaRepo;

import minicode.com.entity.Roles.UserRole;
import minicode.com.entity.User.User;
import minicode.com.repository.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

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


}
