package be.wouterversyck.shoppinglistapi.security.controllers;

import be.wouterversyck.shoppinglistapi.security.models.PasswordResetRequest;
import be.wouterversyck.shoppinglistapi.security.utils.JwtService;
import be.wouterversyck.shoppinglistapi.users.exceptions.UserNotFoundException;
import be.wouterversyck.shoppinglistapi.users.models.User;
import be.wouterversyck.shoppinglistapi.users.services.UserService;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PasswordResetControllerTest {
    private final String USERNAME = "USERNAME";
    private final String OLD_PASSWORD = "OLD_PASSWORD";
    private final String NEW_PASSWORD = "NEW_PASSWORD";
    private final String ENCRYPTED_PASSWORD = "ENCRYPTED_PASSWORD";
    private final String TOKEN = "TOKEN";

    @Mock
    private UserService userService;
    @Mock
    private JwtService jwtService;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private PasswordResetController passwordResetController;

    @Test
    public void shouldResetPassword_WhenPasswordRequestIsMade() throws UserNotFoundException {
        var request = createPasswordResetRequest();
        var user = createUser();

        when(jwtService.getUsernameWithoutValidationSignature(TOKEN)).thenReturn(USERNAME);
        when(userService.getUserModelByUsername(USERNAME)).thenReturn(user);
        when(passwordEncoder.encode(NEW_PASSWORD)).thenReturn(ENCRYPTED_PASSWORD);

        passwordResetController.resetPassword(request);

        verify(jwtService).validatePasswordResetToken(TOKEN, OLD_PASSWORD);
        verify(userService).updateUser(user);
        assertThat(user.getPassword()).isEqualTo(ENCRYPTED_PASSWORD);
    }

    @Test
    public void shouldNotUpdatePassword_WhenOldPasswordDoesNotMathToken() throws UserNotFoundException {
        var request = createPasswordResetRequest();
        var user = createUser();

        when(jwtService.getUsernameWithoutValidationSignature(TOKEN)).thenReturn(USERNAME);
        when(userService.getUserModelByUsername(USERNAME)).thenReturn(user);
        doThrow(new SignatureException("")).when(jwtService).validatePasswordResetToken(any(), any());

        try {
            passwordResetController.resetPassword(request);
        } catch(Exception e) {}

        verify(jwtService).validatePasswordResetToken(TOKEN, OLD_PASSWORD);
        verifyNoMoreInteractions(userService);
        assertThat(user.getPassword()).isEqualTo(OLD_PASSWORD);
    }

    private PasswordResetRequest createPasswordResetRequest() {
        var request = new PasswordResetRequest();
        request.setPassword(NEW_PASSWORD);
        request.setToken(TOKEN);

        return request;
    }

    private User createUser() {
        var user = new User();
        user.setPassword(OLD_PASSWORD);

        return user;
    }
}