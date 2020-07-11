package be.wouterversyck.shoppinglistapi.notes.services;

import be.wouterversyck.shoppinglistapi.notes.exceptions.ShoppingListNotFoundException;
import be.wouterversyck.shoppinglistapi.notes.models.ShoppingList;
import be.wouterversyck.shoppinglistapi.notes.persistence.ShoppingListDao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
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
        when(shoppingListDao.findAllByContributors(USERID)).thenReturn(getShoppingLists());

        List<ShoppingList> result = shoppingListService.getShoppingListsForContributor(USERID);

        assertThat(result.size()).isEqualTo(2);
        assertThat(result).extracting("name")
                .contains(SHOPPING_LIST_NAME_A, SHOPPING_LIST_NAME_B);
    }

    @Test
    void shouldReturnShoppingList_WhenIdIsPassed() throws ShoppingListNotFoundException {
        when(shoppingListDao.findByIdAndOwner("1", USERID)).thenReturn(Optional.of(getShoppingList()));

        ShoppingList result = shoppingListService.getShoppingListById("1", USERID);

        assertThat(result).extracting("name")
                .isEqualTo(SHOPPING_LIST_NAME_A);
    }

    @Test
    void shouldThrowException_WhenShoppingListIsNotFound() {
        when(shoppingListDao.findByIdAndOwner("1", USERID)).thenReturn(Optional.empty());

        assertThrows(ShoppingListNotFoundException.class, () -> shoppingListService.getShoppingListById("1", USERID));
    }

    @Test
    void shouldSetOwnerAndContributor_WhenShoppingListIsSaved() {
        ShoppingList shoppingList = getShoppingList();
        shoppingListService.saveShoppingList(shoppingList, USERID);

        assertThat(shoppingList.getContributors()).contains(USERID);
        assertThat(shoppingList.getOwner()).isEqualTo(USERID);
    }

    @Test
    void shouldNotAddContributorAgain_WhenShoppingListIsSaved() {
        ShoppingList shoppingList = getShoppingList();
        shoppingList.setContributors(Collections.singletonList(USERID));

        shoppingListService.saveShoppingList(shoppingList, USERID);

        assertThat(shoppingList.getContributors()).contains(USERID);
        assertThat(shoppingList.getContributors()).hasSize(1);
        assertThat(shoppingList.getOwner()).isEqualTo(USERID);
    }

    private ShoppingList getShoppingList() {
        return ShoppingList.builder()
                .id("1")
                .name(SHOPPING_LIST_NAME_A)
                .build();
    }

    private List<ShoppingList> getShoppingLists() {
        return Arrays.asList(
                ShoppingList.builder()
                        .id("1")
                        .name(SHOPPING_LIST_NAME_A)
                        .build(),
                ShoppingList.builder()
                        .id("2")
                        .name(SHOPPING_LIST_NAME_B)
                        .build()
        );
    }
}
