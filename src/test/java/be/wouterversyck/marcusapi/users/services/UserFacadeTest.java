package be.wouterversyck.marcusapi.users.services;

import be.wouterversyck.marcusapi.mail.services.MailService;
import be.wouterversyck.marcusapi.notes.services.NoteService;
import be.wouterversyck.marcusapi.security.utils.JwtService;
import be.wouterversyck.marcusapi.users.exceptions.UserNotFoundException;
import be.wouterversyck.marcusapi.users.models.User;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.mail.MessagingException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserFacadeTest {
    private static final String USERNAME = "USERNAME";
    private static final long USER_ID = 1L;
    private static final String OLD_PASSWORD = "OLD_PASSWORD";
    private static final String NEW_PASSWORD = "NEW_PASSWORD";
    private static final String ENCRYPTED_PASSWORD = "ENCRYPTED_PASSWORD";
    private static final String EMAIL = "EMAIL";
    private static final String TOKEN = "TOKEN";
    private static final String PASSWORD = "PASSWORD";

    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private MailService mailService;
    @Mock
    private JwtService jwtService;
    @Mock
    private UserService userService;
    @Mock
    private NoteService noteService;
    @InjectMocks
    private UserFacade userFacade;

    @Test
    public void shouldResetPassword_WhenPasswordRequestIsMade() throws UserNotFoundException {
        var user = createUser();

        when(jwtService.getUsernameWithoutValidationSignature(TOKEN)).thenReturn(USERNAME);
        when(userService.getUserModelByUsername(USERNAME)).thenReturn(user);
        when(passwordEncoder.encode(NEW_PASSWORD)).thenReturn(ENCRYPTED_PASSWORD);

        userFacade.resetPassword(NEW_PASSWORD, TOKEN);

        verify(jwtService).validatePasswordResetToken(TOKEN, OLD_PASSWORD);
        verify(userService).updateUser(user);
        assertThat(user.getPassword()).isEqualTo(ENCRYPTED_PASSWORD);
    }

    @Test
    public void shouldNotUpdatePassword_WhenOldPasswordDoesNotMathToken() throws UserNotFoundException {
        var user = createUser();

        when(jwtService.getUsernameWithoutValidationSignature(TOKEN)).thenReturn(USERNAME);
        when(userService.getUserModelByUsername(USERNAME)).thenReturn(user);
        doThrow(new SignatureException("")).when(jwtService).validatePasswordResetToken(any(), any());

        try {
            userFacade.resetPassword(NEW_PASSWORD, TOKEN);
        } catch(Exception e) {}

        verify(jwtService).validatePasswordResetToken(TOKEN, OLD_PASSWORD);
        verifyNoMoreInteractions(userService);
        assertThat(user.getPassword()).isEqualTo(OLD_PASSWORD);
    }

    @Test
    void shouldSendPasswordSetMail_WhenMethodIsCalled() throws MessagingException, UserNotFoundException {
        var user = new User();
        user.setId(1);
        user.setEmail(EMAIL);
        user.setUsername(USERNAME);
        user.setPassword(PASSWORD);
        when(userService.getUserById(1L)).thenReturn(user);
        when(jwtService.generatePasswordResetToken(user)).thenReturn(TOKEN);

        userFacade.sendPasswordSetMailForUser(1L);

        verify(mailService).sendPasswordSetMail(USERNAME, EMAIL, TOKEN);
    }

    @Test
    public void shouldDeleteNotes_WhenUserIsDeleted() {
        userFacade.deleteUser(USER_ID);

        verify(noteService).deleteAllByOwner(USER_ID);
        verify(userService).deleteUser(USER_ID);
    }

    private User createUser() {
        var user = new User();
        user.setPassword(OLD_PASSWORD);

        return user;
    }
}