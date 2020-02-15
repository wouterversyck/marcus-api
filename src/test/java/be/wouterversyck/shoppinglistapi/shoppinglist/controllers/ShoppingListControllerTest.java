package be.wouterversyck.shoppinglistapi.shoppinglist.controllers;

import be.wouterversyck.shoppinglistapi.shoppinglist.models.ShoppingListView;
import be.wouterversyck.shoppinglistapi.shoppinglist.services.ShoppingListService;
import be.wouterversyck.shoppinglistapi.shoppinglist.testmodels.ShoppingListImpl;
import be.wouterversyck.shoppinglistapi.users.exceptions.UserNotFoundException;
import be.wouterversyck.shoppinglistapi.users.services.UserService;
import be.wouterversyck.shoppinglistapi.users.testmodels.SecureUserImpl;
import com.sun.net.httpserver.HttpPrincipal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ShoppingListControllerTest {

    @Mock
    private ShoppingListService shoppingListService;

    @Mock
    private UserService userService;

    @Mock
    private HttpServletRequest httpServletRequest;

    @InjectMocks
    private ShoppingListController shoppingListController;

    private static final String USERNAME = "USERNAME";

    @Test
    void shouldReturnLists_WhenUserIsSetInContext() throws UserNotFoundException {
        var user = SecureUserImpl.builder()
                .username(USERNAME).build();

        when(httpServletRequest.getUserPrincipal()).thenReturn(new HttpPrincipal(USERNAME, "REALM"));
        when(userService.getUserByUsername(USERNAME)).thenReturn(user);
        when(shoppingListService.getShoppingListsForUser(user)).thenReturn(getShoppingLists());

        List<ShoppingListView> items = shoppingListController.getShoppingLists(httpServletRequest);

        assertThat(items).hasSize(2);
        assertThat(items).extracting("name")
                .contains("test", "test2");
        assertThat(items).extracting("id")
                .contains(1L, 2L);
    }

    private List<ShoppingListView> getShoppingLists() {
        return Arrays.asList(
                ShoppingListImpl.builder()
                        .id(1)
                        .name("test")
                        .build(),
                ShoppingListImpl.builder()
                        .id(2)
                        .name("test2")
                        .build()
        );
    }

}
