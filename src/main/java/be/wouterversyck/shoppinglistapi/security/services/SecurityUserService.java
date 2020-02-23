package be.wouterversyck.shoppinglistapi.security.services;

import be.wouterversyck.shoppinglistapi.security.models.JwtUserDetails;
import be.wouterversyck.shoppinglistapi.users.exceptions.UserNotFoundException;
import be.wouterversyck.shoppinglistapi.users.models.DangerUserView;
import be.wouterversyck.shoppinglistapi.users.services.UserService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;


/*
    Will be picked up by spring boot security auto config
 */
@Service
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

        return new JwtUserDetails(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name())));
    }
}
