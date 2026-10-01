package minicode.com.service;

import minicode.com.entity.UserRole;
import minicode.com.repository.UserRepo;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
public class AdminUserRoleService implements UserDetailsService {
    private UserRepo userRepo;

    public AdminUserRoleService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }


    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findFirstByUserName(username)
                .map(user -> {
                    Set<String> authorities = new HashSet<>();
                    user.getRoles().forEach(role -> {
                        authorities.add(role.getName());
                        role.getPermissions().forEach(permission -> authorities.add(permission.getName()));
                    });
                    if (authorities.isEmpty()) {
                        authorities.add(user.getRole() == UserRole.ADMIN ? "ROLE_ADMIN" : "ROLE_USER");
                    }
                    return User.withUsername(user.getUserName())
                            .password(user.getPassword())
                            .authorities(authorities.stream().map(SimpleGrantedAuthority::new).toList())
                            .build();
                })
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    }
}
