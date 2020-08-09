package be.wouterversyck.marcusapi.users.services;

import be.wouterversyck.marcusapi.mail.services.MailService;
import be.wouterversyck.marcusapi.notes.services.NoteService;
import be.wouterversyck.marcusapi.security.utils.JwtService;
import be.wouterversyck.marcusapi.users.exceptions.UserNotFoundException;
import be.wouterversyck.marcusapi.users.models.SecureUserView;
import be.wouterversyck.marcusapi.users.models.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

@Service
@AllArgsConstructor
@Slf4j
public class UserFacade {
    private PasswordEncoder passwordEncoder;
    private MailService mailService;
    private JwtService jwtService;
    private NoteService notesService;
    private UserService userService;

    public SecureUserView addUser(final User user) {
        return userService.addUser(user);
    }

    public void deleteUser(final long id) {
        notesService.deleteAllByOwner(id);
        userService.deleteUser(id);
    }

    public Page<SecureUserView> getAllUsers(final Pageable page) {
        return userService.getAllUsers(page);
    }

    public void sendPasswordSetMailForUser(final long id) throws MessagingException, UserNotFoundException {
        log.info("sending email for userId {}, verifying user exists", id);
        final var user = userService.getUserById(id);

        log.info("sending email for userId {}, user exists [username: {}] -> sending email", id, user.getUsername());
        mailService.sendPasswordSetMail(user.getUsername(), user.getEmail(), jwtService.generatePasswordResetToken(user));

        log.info("mail sent for [userId: {}, username: {}]", user.getId(), user.getUsername());
    }

    public boolean userExistsByUsername(final String username) {
        return userService.userExistsByUsername(username);
    }

    public boolean userExistsByEmail(final String email) {
        return userService.userExistsByEmail(email);
    }

    public void resetPassword(final String password, final String token) throws UserNotFoundException {
        final String username = jwtService.getUsernameWithoutValidationSignature(token);

        log.info("Resetting pwd for user {}", username);

        final var user = userService.getUserModelByUsername(username);
        jwtService.validatePasswordResetToken(token, user.getPassword());

        user.setPassword(passwordEncoder.encode(password));
        userService.updateUser(user);
    }
}
