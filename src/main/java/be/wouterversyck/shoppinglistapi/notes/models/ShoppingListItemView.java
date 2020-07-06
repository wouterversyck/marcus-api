package be.wouterversyck.shoppinglistapi.notes.models;

import java.util.List;

public interface ShoppingListItemView {
    long getId();
    String getContents();
    boolean isChecked();
    List<ShoppingListItemView> getChildren();
    EntryType getEntryType();
}
