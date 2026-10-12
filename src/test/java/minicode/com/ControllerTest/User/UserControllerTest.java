package minicode.com.ControllerTest.User;
import minicode.com.controller.User.UserController;
import minicode.com.service.User.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.client.RestTestClient;

import static org.hamcrest.MatcherAssert.assertThat;

@AutoConfigureRestTestClient
@WebMvcTest(UserController.class)
public class UserControllerTest {


    @MockitoBean
    UserService userService;
    @MockitoBean
    AuthenticationManager authenticationManager;
    @MockitoBean
    SecurityContextRepository securityContextRepository;
    @Autowired RestTestClient restTestClient;


    @BeforeEach
    public void setUp() {
        // Setup code before each test
    }


    @Test
    public void givenANewUserWithValidDetainsWhenSigningUpThenUserIsCreated() {
        restTestClient.post().uri("api/user/signup").body("""
                        {"userName":"testuser","email":"example","password":"ThisIsAValidPassword123!"}""")
                .exchange().expectStatus().isForbidden();
    }
}
