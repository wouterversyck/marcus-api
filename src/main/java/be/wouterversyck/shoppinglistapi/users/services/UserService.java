package be.wouterversyck.shoppinglistapi.users.services;

import be.wouterversyck.shoppinglistapi.mail.services.MailService;
import be.wouterversyck.shoppinglistapi.security.utils.JwtService;
import be.wouterversyck.shoppinglistapi.users.exceptions.UserNotFoundException;
import be.wouterversyck.shoppinglistapi.users.models.RoleEntity;
import be.wouterversyck.shoppinglistapi.users.models.SecureUserView;
import be.wouterversyck.shoppinglistapi.users.models.DangerUserView;
import be.wouterversyck.shoppinglistapi.users.models.SecureUserViewImpl;
import be.wouterversyck.shoppinglistapi.users.models.User;
import be.wouterversyck.shoppinglistapi.users.persistence.RolesDao;
import be.wouterversyck.shoppinglistapi.users.persistence.UserDao;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.List;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.ignoreCase;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {
    private UserDao userDao;
    private RolesDao rolesDao;
    private MailService mailService;
    private JwtService jwtService;

    // only for internal use
    public DangerUserView getSecurityUserByUsername(final String username) throws UserNotFoundException {
        log.info("retrieving user model with password, use only internally");
        return userDao.findByUsername(username, DangerUserView.class)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    public SecureUserView getUserByUsername(final String username) throws UserNotFoundException {
        return userDao.findByUsername(username, SecureUserView.class)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    public SecureUserView getUserByEmail(final String email) throws UserNotFoundException {
        return userDao.findByEmail(email, SecureUserView.class)
                .orElseThrow(() -> new UserNotFoundException(email));
    }

    public Page<SecureUserView> getAllUsers(final Pageable page) {
        return userDao.findAllProjectedBy(page, SecureUserView.class);
    }

    public SecureUserView addUser(final User user) {
        final var userResult = userDao.save(user);
        log.info("user {} added, sending mail", user.getUsername());
        return SecureUserViewImpl.builder()
                .email(userResult.getEmail())
                .id(userResult.getId())
                .role(userResult.getRole())
                .username(userResult.getUsername())
                .build();
    }

    public void deleteUser(final long id) {
        log.info("Deleting user with user id: {}", id);
        userDao.deleteById(id);
    }

    @Cacheable(value = "be.wouterversyck.shoppinglistapi.users.role")
    public List<RoleEntity> getRoles() {
        return rolesDao.findAll();
    }

    public boolean userExistsByUsername(final String username) {
        final var user = new User();
        user.setUsername(username);
        return userDao.exists(
                Example.of(
                        user,
                        ExampleMatcher.matching()
                                .withIgnorePaths("id")
                                .withMatcher("username", ignoreCase())));
    }

    public boolean userExistsByEmail(final String email) {
        final var user = new User();
        user.setEmail(email);
        return userDao.exists(
                Example.of(
                        user,
                        ExampleMatcher.matching()
                                .withIgnorePaths("id")
                                .withMatcher("email", ignoreCase())));
    }

    public void sendPasswordSetMailForUser(final long id) throws MessagingException, UserNotFoundException {
        log.info("sending email for userId {}, verifying user exists", id);
        final var user = userDao.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        log.info("sending email for userId {}, user exists [username: {}] -> sending email", id, user.getUsername());
        mailService.sendPasswordSetMail(user.getUsername(), user.getEmail(), jwtService.generatePasswordResetToken(user));

        log.info("mail sent for [userId: {}, username: {}]", user.getId(), user.getUsername());
    }
}
