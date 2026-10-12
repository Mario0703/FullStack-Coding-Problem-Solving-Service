package minicode.com;

import minicode.com.controller.Admin.AdminController;
import minicode.com.dto.Admin.CreateStaffRequest;
import minicode.com.entity.Roles.UserRole;
import minicode.com.entity.User.User;
import minicode.com.exception.ForbiddenAccessException;
import minicode.com.service.Admin.AdminService;
import minicode.com.service.User.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.web.context.SecurityContextRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StaffControllerUnitTest {

    private CreateStaffRequest createStaffRequest;
    private User staffUser;
    private UserService userService;
    private AdminController adminController;

    @BeforeEach
    public void setUp() {
        createStaffRequest = new CreateStaffRequest();
        createStaffRequest.setUserName("StaffUser");
        createStaffRequest.setEmail("staff@example.com");
        createStaffRequest.setPassword("Password123");
        createStaffRequest.setStaffRole(UserRole.ADMIN);

        staffUser = new User();
        staffUser.setUserName("StaffUser");
        staffUser.setEmail("staff@example.com");
        staffUser.setRole(UserRole.ADMIN);

        userService = mock(UserService.class);
        adminController = new AdminController(
                mock(AdminService.class),
                userService,
                mock(AuthenticationManager.class),
                mock(SecurityContextRepository.class)
        );
    }

    @Test
    public void givenAnAuthenticatedAdminWhenAStaffMemberIsCreatedThenTheStaffUserIsReturned() {
        // given an authenticated admin
        when(userService.createStaff(createStaffRequest)).thenReturn(staffUser);
        TestingAuthenticationToken adminAuthentication =
                new TestingAuthenticationToken("admin", "password", "ROLE_ADMIN");

        // when a staff member is created through the controller
        User created = adminController.createStaff(createStaffRequest, adminAuthentication);

        // then the created staff user is returned
        assertEquals(staffUser, created);
    }

    @Test
    public void givenARequesterWithoutAdminPermissionsWhenAStaffMemberIsCreatedThenTheRequestIsRejected() {
        // given a requester that only has the moderator role
        TestingAuthenticationToken moderatorAuthentication =
                new TestingAuthenticationToken("moderator", "password", "ROLE_MODERATOR");

        // then creating a staff member through the controller is rejected
        assertThrows(ForbiddenAccessException.class,
                () -> adminController.createStaff(createStaffRequest, moderatorAuthentication));
    }
}