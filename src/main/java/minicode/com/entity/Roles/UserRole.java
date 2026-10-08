package minicode.com.entity.Roles;

import java.util.Set;

public enum UserRole {
    USER(Set.of(UserPermissions.VIEW_CHALLENGES, UserPermissions.SUBMIT_SOLUTIONS)),
    ADMIN(Set.of(UserPermissions.VIEW_CHALLENGES, UserPermissions.SUBMIT_SOLUTIONS, UserPermissions.MANAGE_CHALLENGES, UserPermissions.VIEW_STATISTICS, UserPermissions.VIEW_USERS)),
    OWNER(Set.of(UserPermissions.VIEW_CHALLENGES, UserPermissions.SUBMIT_SOLUTIONS, UserPermissions.MANAGE_CHALLENGES, UserPermissions.VIEW_STATISTICS)),
    MODERATOR(Set.of(UserPermissions.VIEW_CHALLENGES, UserPermissions.SUBMIT_SOLUTIONS));

    private final Set<UserPermissions> permissions;

    UserRole(Set<UserPermissions> permissions) {
        this.permissions = permissions;
    }

    public Set<UserPermissions> getPermissions() {
        return permissions;
    }
}




