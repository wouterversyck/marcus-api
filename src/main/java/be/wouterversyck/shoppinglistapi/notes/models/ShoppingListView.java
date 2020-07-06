package be.wouterversyck.shoppinglistapi.notes.models;

import java.util.List;

public interface ShoppingListView {

    long getId();
    String getName();
    List<ShoppingListItemView> getItems();
}
