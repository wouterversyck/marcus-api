package be.wouterversyck.shoppinglistapi.notes.controllers;

import be.wouterversyck.shoppinglistapi.security.models.JwtUserPrincipal;
import be.wouterversyck.shoppinglistapi.notes.models.ShoppingListView;
import be.wouterversyck.shoppinglistapi.notes.services.ShoppingListService;
import be.wouterversyck.shoppinglistapi.notes.testmodels.ShoppingListImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotesControllerTest {

    @Mock
    private ShoppingListService shoppingListService;

    @InjectMocks
    private NotesController shoppingListController;

    private static final String USERNAME = "USERNAME";
    private static final long USER_ID = 1;

    @Test
    void shouldReturnLists_WhenUserIsSetInContext() {

        when(shoppingListService.getShoppingListsForContributor(USER_ID)).thenReturn(getShoppingLists());

        List<ShoppingListView> items = shoppingListController.getShoppingLists(new JwtUserPrincipal(USER_ID, USERNAME, null, true));

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
