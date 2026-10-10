package minicode.com.JpaRepo;

import minicode.com.entity.Challange.Category;
import minicode.com.entity.Challange.Challenge;
import minicode.com.entity.Challange.Difficulty;
import minicode.com.entity.Roles.UserRole;
import minicode.com.entity.Submissions.Submission;
import minicode.com.entity.Submissions.SubmissionStatus;
import minicode.com.entity.User.User;
import minicode.com.repository.ChallengeRepo;
import minicode.com.repository.SubmissionRepo;
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
public class JpaTestSubmissionRepo {
    @Autowired
    SubmissionRepo submissionRepo;

    @Autowired
    UserRepo userRepo;

    @Autowired
    ChallengeRepo challengeRepo;

    private User user;

    private Challenge challenge;

    private Submission firstSubmission;

    @BeforeEach
    public void setUp() {
        this.user = new User();
        this.user.setUserName("testUser");
        this.user.setEmail("testuser@example.com");
        this.user.setPassword("password");
        this.user.setRole(UserRole.USER);
        this.user.setScore(0);
        this.user.setNumberOfEasyChallengesSolved(0);
        this.user.setNumberOfMediumChallengesSolved(0);
        this.user.setNumberOfHardChallengesSolved(0);
        this.user.setTokenUsage(0);
        userRepo.saveAndFlush(user);

        // A submission requires a parent challenge (challenge_id is NOT NULL).
        this.challenge = new Challenge();
        this.challenge.setName("Hello, World!");
        this.challenge.setDescription("Print Hello, World!");
        this.challenge.setDifficulty(Difficulty.EASY);
        this.challenge.setCategory(Category.GENERAL);
        this.challenge.setNumberOfTestCases(1);
        this.challenge.setLanguage("python");
        challengeRepo.saveAndFlush(challenge);

        // First submission: flushed now, so its @CreationTimestamp is stamped before the second.
        this.firstSubmission = new Submission();
        this.firstSubmission.setUser(user);
        this.firstSubmission.setChallenge(challenge);
        this.firstSubmission.setCode("first submission");
        this.firstSubmission.setLanguage("python");
        this.firstSubmission.setExitCode(0);
        this.firstSubmission.setStatus(SubmissionStatus.ACCEPTED);
        // @CreationTimestamp fills createdAt here; do NOT set it yourself (it would be overwritten).
        submissionRepo.saveAndFlush(firstSubmission);
    }


    @Test
    public void testFindById() {
        Optional<User> user = userRepo.findFirstByEmail("testuser@example.com");
        UUID submissionId = submissionRepo.findAllByUser_UserId(user.get().getId()).get(0).getId();
        Submission submission = submissionRepo.findById(submissionId).orElse(null);

        assertThat(submission).isNotNull();
        assertThat(submission.getCode()).isEqualTo("first submission");
        assertThat(submission.getUser().getId()).isEqualTo(user.get().getId());
        assertThat(submission.getChallenge()).isNotNull();
    }

    @Test
    public void testfindFirstByUser_UserIdAndChallenge_IdOrderByCreatedAtDesc() {
        // A second submission is sent afterwards for the same user + challenge.
        Submission secondSubmission = new Submission();
        secondSubmission.setUser(user);
        secondSubmission.setChallenge(challenge);
        secondSubmission.setCode("second submission");
        secondSubmission.setLanguage("python");
        secondSubmission.setExitCode(0);
        secondSubmission.setStatus(SubmissionStatus.ACCEPTED);
        submissionRepo.saveAndFlush(secondSubmission);

        // Sanity check: the second submission really is the most recent one.
        assertThat(secondSubmission.getCreatedAt()).isAfter(firstSubmission.getCreatedAt());

        // When we ask for the most recent submission of this user + challenge...
        Submission latest = submissionRepo
                .findFirstByUser_UserIdAndChallenge_IdOrderByCreatedAtDesc(
                        user.getUserId(), challenge.getId())
                .orElse(null);

        // Then the second one is returned, not the first.
        assertThat(latest).isNotNull();
        assertThat(latest.getId()).isEqualTo(secondSubmission.getId());
        assertThat(latest.getCode()).isEqualTo("second submission");
    }
    @Test
    public void whenAddingASecondUserAndDeletingTheFirstUserThenTheConfuserIsStillPresent() {
        // Given a second user with their own submission for the same challenge
        User secondUser = new User();
        secondUser.setUserName("secondUser");
        secondUser.setEmail("seconduser@example.com");
        secondUser.setPassword("password");
        secondUser.setRole(UserRole.USER);
        userRepo.saveAndFlush(secondUser);

        Submission secondUserSubmission = new Submission();
        secondUserSubmission.setUser(secondUser);
        secondUserSubmission.setChallenge(challenge);
        secondUserSubmission.setCode("second user's submission");
        secondUserSubmission.setLanguage("python");
        secondUserSubmission.setExitCode(0);
        secondUserSubmission.setStatus(SubmissionStatus.ACCEPTED);
        submissionRepo.saveAndFlush(secondUserSubmission);

        UUID firstUserId = user.getUserId();
        UUID secondUserId = secondUser.getUserId();

        // When the first user's submissions are deleted and the first user is removed
        submissionRepo.deleteAllByUser_UserId(firstUserId);
        userRepo.delete(user);
        userRepo.flush();

        // Then the first user and their submission are gone...
        assertThat(userRepo.findById(firstUserId).isPresent()).isFalse();
        assertThat(submissionRepo.findAllByUser_UserId(firstUserId).isEmpty()).isTrue();

        // ...but the second user and their submission are still present
        assertThat(userRepo.findById(secondUserId).isPresent()).isTrue();
        assertThat(submissionRepo.findAllByUser_UserId(secondUserId).size()).isEqualTo(1);
    }

    @Test
    public void whenUserHasSubmissionsThenCountIsCorrect() {
        long submissionCount = submissionRepo.countByUser_UserId(user.getUserId());
        assertThat(submissionCount).isEqualTo(1);
    }

    @AfterEach
    public void tearDown() {
        userRepo.deleteAll();
    }
}
