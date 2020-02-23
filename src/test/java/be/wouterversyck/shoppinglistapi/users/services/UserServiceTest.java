package be.wouterversyck.shoppinglistapi.users.services;

import be.wouterversyck.shoppinglistapi.mail.services.MailService;
import be.wouterversyck.shoppinglistapi.users.exceptions.UserNotFoundException;
import be.wouterversyck.shoppinglistapi.users.models.DangerUserView;
import be.wouterversyck.shoppinglistapi.users.models.RoleEntity;
import be.wouterversyck.shoppinglistapi.users.models.SecureUserView;
import be.wouterversyck.shoppinglistapi.users.models.User;
import be.wouterversyck.shoppinglistapi.users.persistence.RolesDao;
import be.wouterversyck.shoppinglistapi.users.persistence.UserDao;
import be.wouterversyck.shoppinglistapi.users.testmodels.DangerUserImpl;
import be.wouterversyck.shoppinglistapi.users.testmodels.SecureUserImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.mail.MessagingException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.ignoreCase;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private static final String USERNAME = "USERNAME";
    private static final String EMAIL = "EMAIL";
    private static final String NOT_KNOWN = "NOT_KNOWN";
    private static final String USERNAME_2 = "USERNAME_2";
    private static final String PASSWORD = "PASSWORD";
    private static final String ROLE_NAME = "USER";

    @Mock
    private UserDao userDao;
    @Mock
    private RolesDao rolesDao;
    @Mock
    private MailService mailService;

    private UserService userService;

    @BeforeEach
    void setup() {
        userService = new UserService(userDao, rolesDao, new BCryptPasswordEncoder(), mailService);
    }

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

    @Test
    void shouldSendMailAndDelegateToDao_WhenUserIsAdded() throws MessagingException {
        var user = new User();
        user.setUsername(USERNAME);
        user.setEmail(EMAIL);

        userService.addUser(user);

        verify(mailService).sendPasswordSetMail(EMAIL);
        verify(userDao).save(user);
    }

    @Test
    void shouldStillAddUser_WhenMailErrorOccurs() throws MessagingException {
        var user = new User();
        user.setId(1);
        user.setEmail(EMAIL);

        doThrow(new MessagingException()).when(mailService).sendPasswordSetMail(EMAIL);

        assertThrows(MessagingException.class, () -> userService.addUser(user));

        verify(userDao).save(user);
    }

    @Test
    void shouldReturnRoles_WhenRolesAreRequested() {
        var role = new RoleEntity();
        role.setName(ROLE_NAME);

        when(rolesDao.findAll()).thenReturn(Collections.singletonList(role));

        var roles = userService.getRoles();

        assertThat(roles.size()).isEqualTo(1);
        assertThat(roles).extracting("name")
                .contains(ROLE_NAME);
    }

    @Test
    void shouldSendPasswordSetMail_WhenMethodIsCalled() throws MessagingException, UserNotFoundException {
        var user = new User();
        user.setId(1);
        user.setEmail(EMAIL);
        when(userDao.findById(1L)).thenReturn(Optional.of(user));

        userService.sendPasswordSetMailForUser(1L);

        verify(mailService).sendPasswordSetMail(EMAIL);
    }

    @Test
    void shouldNotSendPasswordSetMail_WhenUserIsNotFound() {
        when(userDao.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.sendPasswordSetMailForUser(1L));

        verifyNoInteractions(mailService);
    }

    @Test
    void shouldReturnTrue_WhenUserNameExists() {
        var user = new User();
        user.setUsername(USERNAME);
        var example = Example.of(user,
                ExampleMatcher.matching()
                        .withIgnorePaths("id")
                        .withMatcher("username", ignoreCase()));
        when(userDao.exists(example)).thenReturn(true);

        var result = userService.userExistsByUsername(USERNAME);

        assertThat(result).isTrue();
    }

    @Test
    void shouldReturnTrue_WhenEmailExists() {
        var user = new User();
        user.setEmail(EMAIL);
        var example = Example.of(user,
                ExampleMatcher.matching()
                        .withIgnorePaths("id")
                        .withMatcher("email", ignoreCase()));
        when(userDao.exists(example)).thenReturn(true);

        var result = userService.userExistsByEmail(EMAIL);

        assertThat(result).isTrue();
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
