package be.wouterversyck.shoppinglistapi.users.services;

import be.wouterversyck.shoppinglistapi.users.exceptions.UserNotFoundException;
import be.wouterversyck.shoppinglistapi.users.models.Role;
import be.wouterversyck.shoppinglistapi.users.services.SecurityUserService;
import be.wouterversyck.shoppinglistapi.users.services.UserService;
import be.wouterversyck.shoppinglistapi.users.testmodels.DangerUserImpl;
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
    private static final String EMAIL = "user@test.be";

    @Test
    void shouldReturnCorrectUserDetails_WhenUserIsFound() throws UserNotFoundException {
        var user = DangerUserImpl.builder()
                .password(PASSWORD)
                .username(USERNAME)
                .role(Role.USER)
                .build();

        when(userService.getSecurityUserByUsername(USERNAME)).thenReturn(user);

        UserDetails result = securityUserService.loadUserByUsername(USERNAME);

        assertThat(result.getPassword()).isEqualTo(PASSWORD);
        assertThat(result.getUsername()).isEqualTo(USERNAME);
    }

    @Test
    void shouldReturnCorrectUserDetails_WhenEmailIsFound() throws UserNotFoundException {
        var user = DangerUserImpl.builder()
                .password(PASSWORD)
                .username(USERNAME)
                .role(Role.USER)
                .build();

        when(userService.getSecurityUserByEmail(EMAIL)).thenReturn(user);

        UserDetails result = securityUserService.loadUserByUsername(EMAIL);

        assertThat(result.getPassword()).isEqualTo(PASSWORD);
        assertThat(result.getUsername()).isEqualTo(USERNAME);
    }

    @Test
    void shouldThrowException_WhenUserIsNotFound() throws UserNotFoundException {
        when(userService.getSecurityUserByUsername(USERNAME)).thenThrow(new UserNotFoundException(""));

        assertThrows(UsernameNotFoundException.class, () -> securityUserService.loadUserByUsername(USERNAME));
    }
}
