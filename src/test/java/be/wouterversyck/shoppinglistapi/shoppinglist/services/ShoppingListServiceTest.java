package be.wouterversyck.shoppinglistapi.shoppinglist.services;

import be.wouterversyck.shoppinglistapi.shoppinglist.exceptions.ShoppingListNotFoundException;
import be.wouterversyck.shoppinglistapi.shoppinglist.daos.ShoppingListDao;
import be.wouterversyck.shoppinglistapi.shoppinglist.models.ShoppingListView;
import be.wouterversyck.shoppinglistapi.shoppinglist.testmodels.ShoppingListImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShoppingListServiceTest {

    private static final long USERID = 1;
    private static final String SHOPPING_LIST_NAME_A = "SHOPPING_LIST_NAME_A";
    private static final String SHOPPING_LIST_NAME_B = "SHOPPING_LIST_NAME_B";

    @Mock
    private ShoppingListDao shoppingListDao;

    @InjectMocks
    private ShoppingListService shoppingListService;

    @Test
    void shouldReturnShoppingList_WhenUserIsPassed() {
        when(shoppingListDao.findAllByOwner(USERID)).thenReturn(getShoppingLists());

        List<ShoppingListView> result = shoppingListService.getShoppingListsForUser(USERID);

        assertThat(result.size()).isEqualTo(2);
        assertThat(result).extracting("name")
                .contains(SHOPPING_LIST_NAME_A, SHOPPING_LIST_NAME_B);
    }

    @Test
    void shouldReturnShoppingList_WhenIdIsPassed() throws ShoppingListNotFoundException {
        when(shoppingListDao.findByIdAndOwner(1L, USERID)).thenReturn(Optional.of(getShoppingList()));

        ShoppingListView result = shoppingListService.getShoppingListById(1L, USERID);

        assertThat(result).extracting("name")
                .isEqualTo(SHOPPING_LIST_NAME_A);
    }

    @Test
    void shouldThrowException_WhenShoppingListIsNotFound() {
        when(shoppingListDao.findByIdAndOwner(1L, USERID)).thenReturn(Optional.empty());

        assertThrows(ShoppingListNotFoundException.class, () -> shoppingListService.getShoppingListById(1L, USERID));
    }

    private ShoppingListView getShoppingList() {
        return ShoppingListImpl.builder()
                .id(1)
                .name(SHOPPING_LIST_NAME_A)
                .build();
    }

    private List<ShoppingListView> getShoppingLists() {
        return Arrays.asList(
                ShoppingListImpl.builder()
                        .id(1)
                        .name(SHOPPING_LIST_NAME_A)
                        .build(),
                ShoppingListImpl.builder()
                        .id(2)
                        .name(SHOPPING_LIST_NAME_B)
                        .build()
        );
    }
}
