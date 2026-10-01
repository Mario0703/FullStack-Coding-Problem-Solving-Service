package minicode.com.config;

import jakarta.transaction.Transactional;
import minicode.com.entity.Roles.Permissions;
import minicode.com.entity.Roles.Role;
import minicode.com.entity.User.User;
import minicode.com.entity.Roles.UserRole;
import minicode.com.repository.PermissionsRepo;
import minicode.com.repository.RoleRepo;
import minicode.com.repository.UserRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PermissionsRepo permissionsRepo;
    private final PasswordEncoder passwordEncoder;
    private final JdbcTemplate jdbcTemplate;
    private final String startupAdminUsername;
    private final String startupAdminPassword;
    private boolean alreadySetup;

    public SetupDataLoader(
            UserRepo userRepo,
            RoleRepo roleRepo,
            PermissionsRepo permissionsRepo,
            PasswordEncoder passwordEncoder,
            JdbcTemplate jdbcTemplate,
            @Value("${ADMIN_USERNAME}") String startupAdminUsername,
            @Value("${ADMIN_PASSWORD}") String startupAdminPassword
    ) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.permissionsRepo = permissionsRepo;
        this.passwordEncoder = passwordEncoder;
        this.jdbcTemplate = jdbcTemplate;
        this.startupAdminUsername = startupAdminUsername;
        this.startupAdminPassword = startupAdminPassword;
    }

    @Override
    @Transactional
    public synchronized void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }

        Permissions viewChallenges = createPermissionIfNotFound("VIEW_CHALLENGES");
        Permissions submitSolutions = createPermissionIfNotFound("SUBMIT_SOLUTIONS");
        Permissions manageChallenges = createPermissionIfNotFound("MANAGE_CHALLENGES");
        Permissions viewStatistics = createPermissionIfNotFound("VIEW_STATISTICS");

        Role userRole = createRoleIfNotFound("ROLE_USER", Set.of(viewChallenges, submitSolutions));
        Role adminRole = createRoleIfNotFound(
                "ROLE_ADMIN",
                Set.of(viewChallenges, submitSolutions, manageChallenges, viewStatistics)
        );

        migrateLegacyAdmins();
        assignRolesToExistingUsers(userRole, adminRole);
        createStartupAdmin(adminRole);

        alreadySetup = true;
    }

    private Permissions createPermissionIfNotFound(String name) {
        return permissionsRepo.findByName(name).orElseGet(() -> permissionsRepo.save(new Permissions(name)));
    }

    private Role createRoleIfNotFound(String name, Set<Permissions> permissions) {
        Role role = roleRepo.findByName(name).orElseGet(() -> new Role(name));
        role.getPermissions().addAll(permissions);
        return roleRepo.save(role);
    }

    private void assignRolesToExistingUsers(Role userRole, Role adminRole) {
        for (User user : userRepo.findAll()) {
            Role role = user.getRole() == UserRole.ADMIN ? adminRole : userRole;
            user.setRoles(new HashSet<>(Set.of(role)));
            userRepo.save(user);
        }
    }

    private void createStartupAdmin(Role adminRole) {
        User admin = userRepo.findFirstByUserName(startupAdminUsername).orElseGet(() ->
                new User(
                        startupAdminUsername,
                        startupAdminUsername + "@admin.local",
                        passwordEncoder.encode(startupAdminPassword),
                        UserRole.ADMIN
                )
        );
        admin.setRole(UserRole.ADMIN);
        admin.setRoles(new HashSet<>(Set.of(adminRole)));
        userRepo.save(admin);
    }

    private void migrateLegacyAdmins() {
        Integer legacyTableCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM information_schema.tables " +
                        "WHERE table_schema = 'public' AND table_name = 'admin_users'",
                Integer.class
        );
        if (legacyTableCount == null || legacyTableCount == 0) {
            return;
        }

        boolean roleColumnExists = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM information_schema.columns " +
                        "WHERE table_schema = 'public' AND table_name = 'users' AND column_name = 'role'",
                Integer.class
        ) > 0;

        if (roleColumnExists) {
            jdbcTemplate.update("""
                    INSERT INTO users (user_id, user_name, email, password, role)
                    SELECT admin_id, admin_user_name, admin_user_name || '@admin.local', admin_password, 'ADMIN'
                    FROM admin_users legacy_admin
                    WHERE NOT EXISTS (
                        SELECT 1 FROM users existing_user
                        WHERE existing_user.user_name = legacy_admin.admin_user_name
                    )
                    ON CONFLICT DO NOTHING
                    """);
        } else {
            jdbcTemplate.update("""
                    INSERT INTO users (user_id, user_name, email, password)
                    SELECT admin_id, admin_user_name, admin_user_name || '@admin.local', admin_password
                    FROM admin_users legacy_admin
                    WHERE NOT EXISTS (
                        SELECT 1 FROM users existing_user
                        WHERE existing_user.user_name = legacy_admin.admin_user_name
                    )
                    ON CONFLICT DO NOTHING
                    """);
        }

        List<String> legacyAdminNames = jdbcTemplate.queryForList(
                "SELECT admin_user_name FROM admin_users", String.class
        );
        for (String name : legacyAdminNames) {
            userRepo.findFirstByUserName(name).ifPresent(user -> {
                user.setRole(UserRole.ADMIN);
                userRepo.save(user);
            });
        }
    }
}



