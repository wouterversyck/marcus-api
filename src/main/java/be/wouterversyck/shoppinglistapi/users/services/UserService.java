package be.wouterversyck.shoppinglistapi.users.services;

import be.wouterversyck.shoppinglistapi.users.exceptions.UserNotFoundException;
import be.wouterversyck.shoppinglistapi.users.models.SecureUserView;
import be.wouterversyck.shoppinglistapi.users.models.DangerUserView;
import be.wouterversyck.shoppinglistapi.users.models.SecureUserViewImpl;
import be.wouterversyck.shoppinglistapi.users.models.User;
import be.wouterversyck.shoppinglistapi.users.persistence.UserDao;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.ignoreCase;

@Service
@AllArgsConstructor
@Slf4j
class UserService {
    private UserDao userDao;

    // only for internal use
    DangerUserView getSecurityUserByUsername(final String username) throws UserNotFoundException {
        log.info("retrieving user model with password, use only internally");
        return userDao.findByUsername(username, DangerUserView.class)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    DangerUserView getSecurityUserByEmail(final String email) throws UserNotFoundException {
        log.info("retrieving user model with password, use only internally");
        return userDao.findByEmail(email, DangerUserView.class)
                .orElseThrow(() -> new UserNotFoundException(email));
    }

    User getUserById(final long id) throws UserNotFoundException {
        return userDao.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    User getUserModelByUsername(final String username) throws UserNotFoundException {
        return userDao.findByUsername(username, User.class)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    SecureUserView getUserByUsername(final String username) throws UserNotFoundException {
        return userDao.findByUsername(username, SecureUserView.class)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    SecureUserView getUserByEmail(final String email) throws UserNotFoundException {
        return userDao.findByEmail(email, SecureUserView.class)
                .orElseThrow(() -> new UserNotFoundException(email));
    }

    Page<SecureUserView> getAllUsers(final Pageable page) {
        return userDao.findAllProjectedBy(page, SecureUserView.class);
    }

    User updateUser(final User user) {
       return userDao.save(user);
    }

    SecureUserView addUser(final User user) {
        final var userResult = userDao.save(user);
        log.info("user {} added, sending mail", user.getUsername());
        return SecureUserViewImpl.builder()
                .email(userResult.getEmail())
                .id(userResult.getId())
                .role(userResult.getRole())
                .username(userResult.getUsername())
                .build();
    }

    void deleteUser(final long id) {
        log.info("Deleting user with user id: {}", id);
        userDao.deleteById(id);
    }

    boolean userExistsByUsername(final String username) {
        final var user = new User();
        user.setUsername(username);
        return userDao.exists(
                Example.of(
                        user,
                        ExampleMatcher.matching()
                                .withIgnorePaths("id")
                                .withMatcher("username", ignoreCase())));
    }

    boolean userExistsByEmail(final String email) {
        final var user = new User();
        user.setEmail(email);
        return userDao.exists(
                Example.of(
                        user,
                        ExampleMatcher.matching()
                                .withIgnorePaths("id")
                                .withMatcher("email", ignoreCase())));
    }

}
