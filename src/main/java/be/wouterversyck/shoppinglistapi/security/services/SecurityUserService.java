package be.wouterversyck.shoppinglistapi.security.services;

import be.wouterversyck.shoppinglistapi.users.exceptions.UserNotFoundException;
import be.wouterversyck.shoppinglistapi.users.models.DangerUserView;
import be.wouterversyck.shoppinglistapi.users.services.UserService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class SecurityUserService implements UserDetailsService {
    private UserService userService;

    public SecurityUserService(final UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final DangerUserView user;
        try {
            user = userService.getSecurityUserByUsername(username);
        } catch (final UserNotFoundException e) {
            throw new UsernameNotFoundException(e.getMessage());
        }

        return User.builder()
                .password(user.getPassword())
                .username(user.getUsername())
                .roles(user.getRole().name())
                .build();
    }
}
