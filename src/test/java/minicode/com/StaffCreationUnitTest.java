package minicode.com;

import minicode.com.dto.Admin.CreateStaffRequest;
import minicode.com.entity.Roles.Role;
import minicode.com.entity.Roles.UserRole;
import minicode.com.entity.User.User;
import minicode.com.exception.SignupException;
import minicode.com.repository.RoleRepo;
import minicode.com.repository.UserRepo;
import minicode.com.security.BcryptPasswordEncoder;
import minicode.com.service.User.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class StaffCreationUnitTest {

    private static final String USER_NAME = "StaffUser";
    private static final String EMAIL = "staff@example.com";
    private static final String PASSWORD = "Password123";

    private UserRepo userRepo;
    private RoleRepo roleRepo;
    private PasswordEncoder passwordEncoder;
    private UserService userService;

    @BeforeEach
    public void setUp() {
        userRepo = mock(UserRepo.class);
        roleRepo = mock(RoleRepo.class);
        passwordEncoder = new BcryptPasswordEncoder();
        userService = new UserService(userRepo, passwordEncoder, roleRepo);

        when(userRepo.save(any(User.class))).thenAnswer(save -> save.getArgument(0));
    }

    @Test
    public void givenAUniqueUserNameAndEmailWhenAnAdminStaffMemberIsCreatedThenTheUserIsSavedWithTheAdminRoleAndItsSeededRole() {
        // given a unique username, email and a seeded ROLE_ADMIN role
        CreateStaffRequest request = staffRequest(UserRole.ADMIN);
        Role adminRole = roleNamed("ROLE_ADMIN");
        when(roleRepo.findByName("ROLE_ADMIN")).thenReturn(Optional.of(adminRole));

        // when an admin staff member is created with them
        User staff = userService.createStaff(request);

        // then the user is saved with the ADMIN role and the seeded role that carries its permissions
        assertEquals(UserRole.ADMIN, staff.getRole());
        assertTrue(staff.getRoles().contains(adminRole));
        assertEquals(USER_NAME, staff.getUserName());
        assertEquals(EMAIL, staff.getEmail());
        assertTrue(passwordEncoder.matches(PASSWORD, staff.getPassword()));
        verify(userRepo).save(staff);
    }

    @Test
    public void givenAUniqueUserNameAndEmailWhenAModeratorStaffMemberIsCreatedThenTheUserIsSavedWithTheModeratorRoleAndItsSeededRole() {
        // given a unique username, email and a seeded ROLE_MODERATOR role
        CreateStaffRequest request = staffRequest(UserRole.MODERATOR);
        Role moderatorRole = roleNamed("ROLE_MODERATOR");
        when(roleRepo.findByName("ROLE_MODERATOR")).thenReturn(Optional.of(moderatorRole));

        // when a moderator staff member is created with them
        User staff = userService.createStaff(request);

        // then the user is saved with the MODERATOR role and the seeded role that carries its permissions
        assertEquals(UserRole.MODERATOR, staff.getRole());
        assertTrue(staff.getRoles().contains(moderatorRole));
        assertTrue(passwordEncoder.matches(PASSWORD, staff.getPassword()));
        verify(userRepo).save(staff);
    }

    @Test
    public void givenAnAlreadyTakenUserNameWhenAStaffMemberIsCreatedThenTheCreationIsRejected() {
        // given a username that is already taken
        CreateStaffRequest request = staffRequest(UserRole.ADMIN);
        when(userRepo.findFirstByUserName(USER_NAME)).thenReturn(Optional.of(new User()));

        // when a staff member is created with it
        SignupException exception = assertThrows(
                SignupException.class,
                () -> userService.createStaff(request)
        );

        // then the creation is rejected and nothing is saved
        assertEquals("Username already exists: " + USER_NAME, exception.getMessage());
        verify(userRepo, never()).save(any(User.class));
    }

    @Test
    public void givenAnAlreadyTakenEmailWhenAStaffMemberIsCreatedThenTheCreationIsRejected() {
        // given an email that is already taken
        CreateStaffRequest request = staffRequest(UserRole.MODERATOR);
        when(userRepo.findFirstByEmail(EMAIL)).thenReturn(Optional.of(new User()));

        // when a staff member is created with it
        SignupException exception = assertThrows(
                SignupException.class,
                () -> userService.createStaff(request)
        );

        // then the creation is rejected and nothing is saved
        assertEquals("Email already exists: " + EMAIL, exception.getMessage());
        verify(userRepo, never()).save(any(User.class));
    }

    private CreateStaffRequest staffRequest(UserRole staffRole) {
        CreateStaffRequest request = new CreateStaffRequest();
        request.setUserName(USER_NAME);
        request.setEmail(EMAIL);
        request.setPassword(PASSWORD);
        request.setStaffRole(staffRole);
        return request;
    }

    private Role roleNamed(String name) {
        Role role = new Role();
        role.setName(name);
        return role;
    }
}