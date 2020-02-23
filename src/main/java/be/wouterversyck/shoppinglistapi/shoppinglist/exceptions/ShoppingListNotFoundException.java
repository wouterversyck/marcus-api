package be.wouterversyck.shoppinglistapi.shoppinglist.exceptions;

import static java.lang.String.format;

public class ShoppingListNotFoundException extends Exception {
    public ShoppingListNotFoundException(final long id) {
        super(format("Shopping list with id %s was not found", id));
    }
}
