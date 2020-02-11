package be.wouterversyck.shoppinglistapi.security.services;

import be.wouterversyck.shoppinglistapi.users.models.User;
import be.wouterversyck.shoppinglistapi.users.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SecurityUserServiceTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private SecurityUserService securityUserService;

    private static final String USERNAME = "USERNAME";
    private static final String PASSWORD = "PASSWORD";

    @Test
    void shouldReturnCorrectUserDetails_WhenUserIsFound() {
        User user = new User();
        user.setPassword(PASSWORD);
        user.setUsername(USERNAME);
        when(userService.getUserByUsername(USERNAME)).thenReturn(user);

        UserDetails result = securityUserService.loadUserByUsername(USERNAME);

        assertThat(result.getPassword()).isEqualTo(PASSWORD);
        assertThat(result.getUsername()).isEqualTo(USERNAME);
    }

    @Test
    void shouldThrowException_WhenUserIsNotFound() {
        when(userService.getUserByUsername(USERNAME)).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> securityUserService.loadUserByUsername(USERNAME));
    }
}
