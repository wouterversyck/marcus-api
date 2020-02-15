package be.wouterversyck.shoppinglistapi.users.services;

import be.wouterversyck.shoppinglistapi.users.exceptions.UserNotFoundException;
import be.wouterversyck.shoppinglistapi.users.models.DangerUserView;
import be.wouterversyck.shoppinglistapi.users.models.SecureUserView;
import be.wouterversyck.shoppinglistapi.users.persistence.UserDao;
import be.wouterversyck.shoppinglistapi.users.testmodels.DangerUserImpl;
import be.wouterversyck.shoppinglistapi.users.testmodels.SecureUserImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private static final String USERNAME = "USERNAME";
    private static final String NOT_KNOWN = "NOT_KNOWN";
    private static final String USERNAME_2 = "USERNAME_2";
    private static final String PASSWORD = "PASSWORD";

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldReturnUser_WhenUsernameIsPassed() throws UserNotFoundException {
        when(userDao.findByUsername(USERNAME, SecureUserView.class))
                .thenReturn(createSecureUser());

        var user = userService.getUserByUsername(USERNAME);

        assertThat(user.getUsername()).isEqualTo(USERNAME);
    }

    @Test
    void shouldThrowException_WhenUnknownUsernameIsPassed() {
        when(userDao.findByUsername(NOT_KNOWN, SecureUserView.class))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserByUsername(NOT_KNOWN));
    }

    @Test
    void shouldReturnDangerUser_WhenUsernameIsPassedToSecurityMethod() throws UserNotFoundException {
        when(userDao.findByUsername(USERNAME, DangerUserView.class))
                .thenReturn(createDangerUser());

        var user = userService.getSecurityUserByUsername(USERNAME);

        assertThat(user.getUsername()).isEqualTo(USERNAME);
    }

    @Test
    void shouldThrowException_WhenUnknownUsernameIsPassedToSecurityMethod() {
        when(userDao.findByUsername(NOT_KNOWN, DangerUserView.class))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getSecurityUserByUsername(NOT_KNOWN));
    }

    @Test
    void shouldReturnAllUser_WhenPageIsPassed() {
        Pageable page = PageRequest.of(1, 20);
        when(userDao.findAllProjectedBy(page, SecureUserView.class))
                .thenReturn(createSecureUserCollection());

        var usersCollection = userService.getAllUsers(page);

        assertThat(usersCollection.getTotalElements()).isEqualTo(2);
        assertThat(usersCollection.getContent()).extracting("username")
            .contains(USERNAME, USERNAME_2);
    }

    private Optional<SecureUserView> createSecureUser() {
        return Optional.of(SecureUserImpl.builder()
                .username(USERNAME)
                .build());
    }

    private Page<SecureUserView> createSecureUserCollection() {
        return new PageImpl<>(List.of(
                SecureUserImpl.builder()
                        .username(USERNAME).build(),
                SecureUserImpl.builder()
                        .username(USERNAME_2).build()));
    }

    private Optional<DangerUserView> createDangerUser() {
        return Optional.of(DangerUserImpl.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .build());
    }
}
