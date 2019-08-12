package be.wouterversyck.shoppinglistapi.shoppinglist.models;

import java.util.List;

public interface ShoppingListDto {

    long getId();
    String getName();
    List<ShoppingListItemDto> getItems();
}
