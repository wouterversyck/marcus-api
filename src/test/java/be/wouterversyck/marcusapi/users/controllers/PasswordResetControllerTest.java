package be.wouterversyck.marcusapi.users.controllers;

import be.wouterversyck.marcusapi.security.models.PasswordResetRequest;
import be.wouterversyck.marcusapi.users.exceptions.UserNotFoundException;
import be.wouterversyck.marcusapi.users.services.UserFacade;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PasswordResetControllerTest {

    private static final String TOKEN = "TOKEN";
    private static final String PASSWORD = "PASSWORD";

    @Mock
    private UserFacade userFacade;

    @InjectMocks
    private PasswordResetController passwordResetController;

    @Test
    void shouldDelegateToService_WhenPasswordResetRequestIsMade() throws UserNotFoundException {

        passwordResetController.resetPassword(createPasswordResetRequest());

        verify(userFacade).resetPassword(PASSWORD, TOKEN);
    }

    public PasswordResetRequest createPasswordResetRequest() {
        var passwordResetRequest = new PasswordResetRequest();
        passwordResetRequest.setToken(TOKEN);
        passwordResetRequest.setPassword(PASSWORD);

        return passwordResetRequest;
    }
}