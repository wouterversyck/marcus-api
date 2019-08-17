package be.wouterversyck.shoppinglistapi.shoppinglist;

import static java.lang.String.format;

public class ShoppingListNotFoundException extends Exception {
    public ShoppingListNotFoundException(long id) {
        super(format("Shopping list with id %s was not found", id));
    }
}
