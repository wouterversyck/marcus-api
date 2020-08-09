package be.wouterversyck.marcusapi.users.controllers;

import be.wouterversyck.marcusapi.users.exceptions.UserNotFoundException;
import be.wouterversyck.marcusapi.users.models.RoleEntity;
import be.wouterversyck.marcusapi.users.models.SecureUserView;
import be.wouterversyck.marcusapi.users.models.User;
import be.wouterversyck.marcusapi.users.services.RolesService;
import be.wouterversyck.marcusapi.users.services.UserFacade;
import be.wouterversyck.marcusapi.users.testmodels.SecureUserImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.mail.MessagingException;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminUsersControllerTest {

    private static final String USERNAME_1 = "USERNAME_1";
    private static final String USERNAME_2 = "USERNAME_2";
    private static final String EMAIL = "EMAIL";
    private static final String ROLE_NAME = "USER";

    @Mock
    private UserFacade userFacade;
    @Mock
    private RolesService rolesService;

    @InjectMocks
    private AdminUsersController adminController;

    @Test
    void shouldReturnUsersPage_WhenControllerMethodIsCalled() {
        var page = PageRequest.of(0, 20);
        when(userFacade.getAllUsers(page))
                .thenReturn(createUserPage());

        Page<SecureUserView> result = adminController.getUsers(page);

        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent())
                .extracting("username")
                .contains(USERNAME_1, USERNAME_2);
    }

    @Test
    void shouldDelegateToUserFacadeAndReturnProperResponse_WhenUserIsAdded() throws UserNotFoundException, MessagingException {
        var user = new User();
        user.setUsername(USERNAME_1);
        when(userFacade.addUser(user)).thenReturn(
                SecureUserImpl.builder()
                        .id(1)
                        .username(USERNAME_1).build());

        var result = adminController.addUser(user);

        verify(userFacade).sendPasswordSetMailForUser(1);
        verify(userFacade).addUser(user);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().getUsername()).isEqualTo(USERNAME_1);
    }

    @Test
    void shouldDelegateToUserFacadeAndReturnProperResponse_WhenUserIsAddedButEmailSendingFailed() throws UserNotFoundException, MessagingException {
        var user = new User();
        user.setUsername(USERNAME_1);
        when(userFacade.addUser(user)).thenReturn(
                SecureUserImpl.builder()
                        .id(1)
                        .username(USERNAME_1).build());
        doThrow(new MessagingException("test")).when(userFacade).sendPasswordSetMailForUser(1);

        var result = adminController.addUser(user);

        verify(userFacade).sendPasswordSetMailForUser(1);
        verify(userFacade).addUser(user);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.MULTI_STATUS);
        assertThat(result.getBody().getUsername()).isEqualTo(USERNAME_1);
    }

    @Test
    void shouldReturnRoles_WhenGetRolesRequestIsMade() {
        var role = new RoleEntity();
        role.setName(ROLE_NAME);

        when(rolesService.getRoles()).thenReturn(Collections.singletonList(role));

        var roles = adminController.getRoles();

        assertThat(roles.size()).isEqualTo(1);
        assertThat(roles).extracting("name")
                .contains(ROLE_NAME);
    }

    @Test
    void shouldDelegateToService_WhenPasswordSetMailRequestIsMade() throws MessagingException, UserNotFoundException {
        adminController.sendPasswordSetMail(1);

        verify(userFacade).sendPasswordSetMailForUser(1);
    }

    @Test
    void shouldThrowException_WhenBothParamsAreNullOnExistsMethod() {
        assertThrows(ResponseStatusException.class, () -> adminController.doesUserExist(null, null));
    }

    @Test
    void shouldDelegateToService_WhenUsernameIsProvidedToExistsMethod() {
        adminController.doesUserExist(USERNAME_1, null);
        verify(userFacade).userExistsByUsername(USERNAME_1);
    }

    @Test
    void shouldDelegateToService_WhenEmailIsProvidedToExistsMethod() {
        adminController.doesUserExist(null, EMAIL);
        verify(userFacade).userExistsByEmail(EMAIL);
    }

    private Page<SecureUserView> createUserPage() {
        return new PageImpl<>(List.of(
                SecureUserImpl.builder()
                        .username(USERNAME_1).build(),
                SecureUserImpl.builder()
                        .username(USERNAME_2).build()
        ));
    }
}