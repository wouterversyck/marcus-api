package be.wouterversyck.shoppinglistapi.notes.testmodels;

import be.wouterversyck.shoppinglistapi.notes.models.ShoppingListItemView;
import be.wouterversyck.shoppinglistapi.notes.models.ShoppingListView;
import lombok.Builder;

import java.util.List;

@Builder
public class ShoppingListImpl implements ShoppingListView {

    private long id;
    private String name;
    private List<ShoppingListItemView> items;

    public ShoppingListImpl(long id, String name, List<ShoppingListItemView> items) {
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
    public List<ShoppingListItemView> getItems() {
        return items;
    }
}
