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


}
