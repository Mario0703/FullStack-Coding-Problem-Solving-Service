package minicode.com.FullAplicationTest;
import minicode.com.entity.Challange.Challenge;
import minicode.com.entity.Challange.Difficulty;
import minicode.com.entity.User.User;
import minicode.com.repository.ChallengeRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static javax.swing.UIManager.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
public class UserChallengesIntergrationTest {

    @Autowired
    private MockMvc mockMvc;

    private ChallengeRepo challengeRepo;


    @BeforeEach
    public void setUo(){

    }



    @Test
    public void givenAUserWhenTheyGoToChallengesBoardAndOpenAchallangeTheyWilGetTheDetails() throws Exception {
        Challenge challenge1 = new Challenge();
        challenge1.setName("Challenge 1");
        challenge1.setDescription("This is the first challenge.");
        challenge1.setDifficulty(Difficulty.EASY);
        challenge1.setNumberOfTestCases(3);
        challengeRepo.saveAndFlush(challenge1);

        User user = new User();
        user.setUserName("testuser");
        user.setEmail("email@email.com");
        user.setPassword("password");
        user = userRepository.saveAndFlush(user);

        mockMvc.perform(post("/api/user/challenges/" + challenge1.getId()).contentType("application/json")
                .content("{\"userId\": \"" + user.getId() + "\"}")


    }
}
