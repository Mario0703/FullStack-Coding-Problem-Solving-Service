package minicode.com.JpaRepo;

import minicode.com.entity.Challange.SolvedChallenges;
import minicode.com.entity.Roles.UserRole;
import minicode.com.entity.User.User;
import minicode.com.repository.UserRepo;
import minicode.com.repository.solvedChallengesRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class JpaSolvedChallengesRepo {

    private SolvedChallenges solvedChallenge1;
    private SolvedChallenges solvedChallenge2;
    private User user;

    @Autowired
    private solvedChallengesRepo solvedChallengesRepo;

    @Autowired
    private UserRepo userRepo;

    private UUID userId;


    @BeforeEach
    public void setUp() {
        this.user = new User();
        user.setUserName("testUser");
        user.setEmail("testuser@example.com");
        user.setPassword("testpassword");
        user.setRole(UserRole.USER);
        user.setScore(0);
        user.setNumberOfEasyChallengesSolved(0);
        user.setNumberOfMediumChallengesSolved(0);
        user.setNumberOfHardChallengesSolved(0);
        user.setTokenUsage(0);
        User savedUser = userRepo.save(user);
        userId = savedUser.getUserId();

        this.solvedChallenge1 = new SolvedChallenges();
        solvedChallenge1.setUserId(userId);
        solvedChallenge1.setChallengeId(UUID.randomUUID());
        solvedChallenge1.setCode("def add(a, b): return a + b");
        solvedChallenge1.setLanguage("python");
        solvedChallengesRepo.save(solvedChallenge1);

        this.solvedChallenge2 = new SolvedChallenges();
        solvedChallenge2.setUserId(userId);
        solvedChallenge2.setChallengeId(UUID.randomUUID());
        solvedChallenge2.setCode("public int add(int a, int b) { return a + b; }");
        solvedChallenge2.setLanguage("java");
        solvedChallengesRepo.save(solvedChallenge2);
    }

    @Test
    public void numberOfSolvedChallengesByUserIdTest() {
        long count = solvedChallengesRepo.countDistinctChallengesByUserId(userId);
        assertThat(count).isEqualTo(2);
    }

    @Test
    public void userHasSolvedTwoChallengesTest(){
        boolean hasSolvedChallengeOne = solvedChallengesRepo.existsByUserIdAndChallengeId(user.getUserId(), solvedChallenge1.getChallengeId());
        boolean hasSolvedChallengeTwo = solvedChallengesRepo.existsByUserIdAndChallengeId(user.getUserId(), solvedChallenge2.getChallengeId());
        assertThat(hasSolvedChallengeOne).isTrue();
        assertThat(hasSolvedChallengeTwo).isTrue();
    }
    @Test
    public void deletingOneSolvedChallengeObjectMakesNoneLeft(){
        solvedChallengesRepo.deleteAllByUserId(userId);
        long numberOfSolvedChallenges = solvedChallengesRepo.count();
        assertThat(numberOfSolvedChallenges).isEqualTo(0);
    }

    @Test
    public void findAllByUserIdTest() {
        List<SolvedChallenges> solvedChallenges = solvedChallengesRepo.findAllByUserId(userId);
        assertThat(solvedChallenges.size()).isEqualTo(2);
    }

    @Test
    public void findChallengeIdsByUserIdTest() {
        List<UUID> challengeIds = solvedChallengesRepo.findChallengeIdsByUserId(userId);
        assertThat(challengeIds.size()).isEqualTo(2);
    }
    @Test
    public void countDistinctChallengesTest() {
        long count = solvedChallengesRepo.countDistinctChallenges();
        assertThat(count).isEqualTo(2);
    }


    @AfterEach
    public void tearDown() {
        solvedChallengesRepo.deleteAll();
        userRepo.deleteAll();
    }

}
