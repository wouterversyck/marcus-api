package be.wouterversyck.shoppinglistapi.security.services;

import be.wouterversyck.shoppinglistapi.users.models.User;
import be.wouterversyck.shoppinglistapi.users.services.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static java.lang.String.format;

public class SecurityUserService implements UserDetailsService {
    private UserService userService;

    public SecurityUserService(final UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final User user = userService.getUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(format("User with username %s was not found", username));
        }

        return org.springframework.security.core.userdetails.User.builder()
                .authorities("USER")
                .password(user.getPassword())
                .username(user.getUsername())
                .build();
    }
}
