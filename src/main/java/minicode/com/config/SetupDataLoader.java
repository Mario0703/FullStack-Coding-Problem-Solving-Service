package minicode.com.config;

import jakarta.transaction.Transactional;
import minicode.com.entity.Roles.Permissions;
import minicode.com.entity.Roles.Role;
import minicode.com.entity.Roles.UserPermissions;
import minicode.com.entity.User.User;
import minicode.com.entity.Roles.UserRole;
import minicode.com.repository.PermissionsRepo;
import minicode.com.repository.RoleRepo;
import minicode.com.repository.UserRepo;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PermissionsRepo permissionsRepo;
    private final PasswordEncoder passwordEncoder;
    private boolean alreadySetup;

    public SetupDataLoader(
            UserRepo userRepo,
            RoleRepo roleRepo,
            PermissionsRepo permissionsRepo,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.permissionsRepo = permissionsRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public synchronized void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }

        for(UserRole role : UserRole.values()) {
            createRolesOnStartUp("ROLE_" + role.name(),
                    role.getPermissions().stream().map(permission ->
                            createPermissionOnStartUp(permission.name())).collect(java.util.stream.Collectors.toSet()));
        }

        for (UserPermissions permission : UserPermissions.values()) {
            createPermissionOnStartUp(permission.name());
        }

        Role adminRole = roleRepo.findByName("ROLE_" + UserRole.ADMIN.name())
                .orElseThrow(() -> new IllegalStateException("ROLE_ADMIN was not initialized"));
        createStartupAdmin(adminRole);

        alreadySetup = true;
    }


    private Permissions createPermissionOnStartUp(String name) {
        return permissionsRepo.findByName(name).orElseGet(() -> permissionsRepo.save(new Permissions(name)));
    }


    private Role createRolesOnStartUp(String name, Set<Permissions> permissions) {
        Role role = roleRepo.findByName(name).orElseGet(() -> new Role(name));
        role.getPermissions().addAll(permissions);
        return roleRepo.save(role);
    }


    private void createStartupAdmin(Role adminRole) {
        User admin = userRepo.findFirstByUserName("admin").orElseGet(() ->
                new User(
                        "admin",
                        "admin@admin.local",
                        passwordEncoder.encode("admin"),
                        UserRole.ADMIN
                )
        );
        admin.setRole(UserRole.ADMIN);
        admin.getRoles().add(adminRole);
        userRepo.save(admin);
    }
}



