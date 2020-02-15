package be.wouterversyck.shoppinglistapi.shoppinglist.models;

import java.util.List;

public interface ShoppingListView {

    long getId();
    String getName();
    List<ShoppingListItemView> getItems();
}
