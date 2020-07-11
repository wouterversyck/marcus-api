package be.wouterversyck.shoppinglistapi.notes.models;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode
public class ShoppingListItem {
    private String contents;

    private String entryType;

    private boolean checked;
    private List<ShoppingListItem> children;
}
