package be.wouterversyck.shoppinglistapi.admin.controllers;

import be.wouterversyck.shoppinglistapi.users.exceptions.UserNotFoundException;
import be.wouterversyck.shoppinglistapi.users.models.RoleEntity;
import be.wouterversyck.shoppinglistapi.users.models.SecureUserView;
import be.wouterversyck.shoppinglistapi.users.models.User;
import be.wouterversyck.shoppinglistapi.users.services.UserService;
import be.wouterversyck.shoppinglistapi.users.testmodels.SecureUserImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.mail.MessagingException;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminControllerTest {

    private static final String USERNAME_1 = "USERNAME_1";
    private static final String USERNAME_2 = "USERNAME_2";
    private static final String ROLE_NAME = "USER";

    @Mock
    private UserService userService;

    @InjectMocks
    private AdminController adminController;

    @Test
    void shouldReturnUsersPage_WhenControllerMethodIsCalled() {
        var page = PageRequest.of(0, 20);
        when(userService.getAllUsers(page))
                .thenReturn(createUserPage());

        Page<SecureUserView> result = adminController.getUsers(page);

        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent())
                .extracting("username")
                .contains(USERNAME_1, USERNAME_2);
    }

    @Test
    void shouldDelegateToUserService_WhenUserIsAdded() throws MessagingException {
        var user = new User();
        user.setUsername(USERNAME_1);

        adminController.addUser(user);

        verify(userService).addUser(user);
    }

    @Test
    void shouldReturnRoles_WhenGetRolesRequestIsMade() {
        var role = new RoleEntity();
        role.setName(ROLE_NAME);

        when(userService.getRoles()).thenReturn(Collections.singletonList(role));

        var roles = adminController.getRoles();

        assertThat(roles.size()).isEqualTo(1);
        assertThat(roles).extracting("name")
                .contains(ROLE_NAME);
    }

    @Test
    void shouldDelegateToService_WhenPasswordSetMailRequestIsMade() throws MessagingException, UserNotFoundException {
        adminController.sendPasswordSetMail(1);

        verify(userService).sendPasswordSetMailForUser(1);
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
