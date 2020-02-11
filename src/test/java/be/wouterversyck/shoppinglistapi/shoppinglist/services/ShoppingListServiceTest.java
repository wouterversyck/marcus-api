package be.wouterversyck.shoppinglistapi.shoppinglist.services;

import be.wouterversyck.shoppinglistapi.shoppinglist.ShoppingListNotFoundException;
import be.wouterversyck.shoppinglistapi.shoppinglist.daos.ShoppingListDao;
import be.wouterversyck.shoppinglistapi.shoppinglist.models.ShoppingListDto;
import be.wouterversyck.shoppinglistapi.shoppinglist.models.ShoppingListItemDto;
import be.wouterversyck.shoppinglistapi.users.models.User;
import lombok.Builder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ShoppingListServiceTest {

    private static final String PASSWORD = "PASSWORD";
    private static final String USERNAME = "USERNAME";
    private static final String SHOPPING_LIST_NAME_A = "SHOPPING_LIST_NAME_A";
    private static final String SHOPPING_LIST_NAME_B = "SHOPPING_LIST_NAME_B";

    @Mock
    private ShoppingListDao shoppingListDao;

    @InjectMocks
    private ShoppingListService shoppingListService;

    @Test
    void shouldReturnShoppingList_WhenUserIsPassed() {
        User user = createUser();
        when(shoppingListDao.findAllByOwner(user)).thenReturn(getShoppingLists());

        List<ShoppingListDto> result = shoppingListService.getShoppingListsForUser(user);

        assertThat(result.size()).isEqualTo(2);
        assertThat(result).extracting("name")
                .contains(SHOPPING_LIST_NAME_A, SHOPPING_LIST_NAME_B);
    }

    @Test
    void shouldReturnShoppingList_WhenIdIsPassed() throws ShoppingListNotFoundException {
        User user = createUser();
        when(shoppingListDao.findByIdAndOwner(1L, user)).thenReturn(Optional.of(getShoppingList()));

        ShoppingListDto result = shoppingListService.getShoppingListById(1L, user);

        assertThat(result).extracting("name")
                .isEqualTo(SHOPPING_LIST_NAME_A);
    }

    private User createUser() {
        User user = new User();
        user.setPassword(PASSWORD);
        user.setUsername(USERNAME);

        return user;
    }

    private ShoppingListDto getShoppingList() {
        return ShoppingList.builder()
                .id(1)
                .name(SHOPPING_LIST_NAME_A)
                .build();
    }

    private List<ShoppingListDto> getShoppingLists() {
        return Arrays.asList(
                ShoppingList.builder()
                        .id(1)
                        .name(SHOPPING_LIST_NAME_A)
                        .build(),
                ShoppingList.builder()
                        .id(2)
                        .name(SHOPPING_LIST_NAME_B)
                        .build()
        );
    }

    @Builder
    public static class ShoppingList implements ShoppingListDto {

        private long id;
        private String name;
        private List<ShoppingListItemDto> items;

        public ShoppingList(long id, String name, List<ShoppingListItemDto> items) {
            this.id = id;
            this.name = name;
            this.items = items;
        }

        @Override
        public long getId() {
            return id;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public List<ShoppingListItemDto> getItems() {
            return items;
        }
    }
}
