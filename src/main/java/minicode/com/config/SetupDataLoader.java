package minicode.com.config;

import jakarta.transaction.Transactional;
import minicode.com.entity.Roles.Permissions;
import minicode.com.entity.Roles.Role;
import minicode.com.entity.Roles.UserPermissions;
import minicode.com.entity.User.User;
import minicode.com.entity.Roles.UserRole;
import minicode.com.entity.Container.Container;
import minicode.com.entity.TestRunner.TestRunner;
import minicode.com.repository.ContainerRepo;
import minicode.com.repository.PermissionsRepo;
import minicode.com.repository.RoleRepo;
import minicode.com.repository.TestRunnerRepo;
import minicode.com.repository.UserRepo;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PermissionsRepo permissionsRepo;
    private final ContainerRepo containerRepo;
    private final TestRunnerRepo testRunnerRepo;
    private final PasswordEncoder passwordEncoder;
    private boolean alreadySetup;

    public SetupDataLoader(
            UserRepo userRepo,
            RoleRepo roleRepo,
            PermissionsRepo permissionsRepo,
            PasswordEncoder passwordEncoder,
            ContainerRepo containerRepo,
            TestRunnerRepo testRunnerRepo
    ) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.permissionsRepo = permissionsRepo;
        this.passwordEncoder = passwordEncoder;
        this.containerRepo = containerRepo;
        this.testRunnerRepo = testRunnerRepo;
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

        seedExecutionMetadata();

        Role adminRole = roleRepo.findByName("ROLE_" + UserRole.ADMIN.name())
                .orElseThrow(() -> new IllegalStateException("ROLE_ADMIN was not initialized"));
        createStartupAdmin(adminRole);

        alreadySetup = true;
    }


    private Permissions createPermissionOnStartUp(String name) {
        return permissionsRepo.findByName(name).orElseGet(() -> permissionsRepo.save(new Permissions(name)));
    }

    private void seedExecutionMetadata() {
        List<ExecutionDefinition> definitions = List.of(
                new ExecutionDefinition("python", "minileetcode-python"),
                new ExecutionDefinition("java", "minileetcode-java"),
                new ExecutionDefinition("c++", "minileetcode-cpp"),
                new ExecutionDefinition("c#", "minileetcode-csharp")
        );

        if (containerRepo.count() == 0) {
            containerRepo.saveAll(definitions.stream()
                    .map(definition -> new Container(
                            definition.language(),
                            definition.imageName(),
                            readMetadataFile(Path.of("CodeEnv", definition.language(), "Dockerfile"))
                    ))
                    .toList());
        }

        if (testRunnerRepo.count() == 0) {
            testRunnerRepo.saveAll(definitions.stream()
                    .map(definition -> new TestRunner(
                            definition.language(),
                            readMetadataFile(Path.of("CodeEnv", "TestSuite", "runCommandTest.bs"))
                    ))
                    .toList());
        }
    }

    private String readMetadataFile(Path path) {
        try {
            return Files.readString(path);
        } catch (IOException exception) {
            throw new IllegalStateException("Could not load execution metadata from " + path, exception);
        }
    }

    private record ExecutionDefinition(String language, String imageName) {
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



