package be.wouterversyck.shoppinglistapi.users.services;

import be.wouterversyck.shoppinglistapi.mail.services.MailService;
import be.wouterversyck.shoppinglistapi.users.exceptions.UserNotFoundException;
import be.wouterversyck.shoppinglistapi.users.models.RoleEntity;
import be.wouterversyck.shoppinglistapi.users.models.SecureUserView;
import be.wouterversyck.shoppinglistapi.users.models.DangerUserView;
import be.wouterversyck.shoppinglistapi.users.models.User;
import be.wouterversyck.shoppinglistapi.users.persistence.RolesDao;
import be.wouterversyck.shoppinglistapi.users.persistence.UserDao;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.List;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.ignoreCase;

@Service
public class UserService {
    private UserDao userDao;
    private RolesDao rolesDao;
    private PasswordEncoder passwordEncoder;
    private MailService mailService;

    public UserService(final UserDao userDao, final RolesDao rolesDao,
                       final PasswordEncoder passwordEncoder, final MailService mailService) {
        this.userDao = userDao;
        this.rolesDao = rolesDao;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
    }

    // only for internal use
    public DangerUserView getSecurityUserByUsername(final String username) throws UserNotFoundException {
        return userDao.findByUsername(username, DangerUserView.class)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    public SecureUserView getUserByUsername(final String username) throws UserNotFoundException {
        return userDao.findByUsername(username, SecureUserView.class)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    public Page<SecureUserView> getAllUsers(final Pageable page) {
        return userDao.findAllProjectedBy(page, SecureUserView.class);
    }

    public void addUser(final User user) throws MessagingException {
        userDao.save(user);
        mailService.sendPasswordSetMail(user.getEmail());
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
        final var user = userDao.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        mailService.sendPasswordSetMail(user.getEmail());
    }

    private String generateRandomPassword() {
        final var string = RandomStringUtils.randomAlphanumeric(8);
        return passwordEncoder.encode(string);
    }
}
