package be.wouterversyck.shoppinglistapi.shoppinglist.controllers;

import be.wouterversyck.shoppinglistapi.security.models.JwtUserPrincipal;
import be.wouterversyck.shoppinglistapi.shoppinglist.ShoppingListNotFoundException;
import be.wouterversyck.shoppinglistapi.shoppinglist.models.ShoppingListView;
import be.wouterversyck.shoppinglistapi.shoppinglist.services.ShoppingListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("shoppinglist")
public class ShoppingListController {
    private final ShoppingListService shoppingListService;

    public ShoppingListController(final ShoppingListService shoppingListService) {
        this.shoppingListService = shoppingListService;
    }

    @GetMapping("all")
    public List<ShoppingListView> getShoppingLists(final JwtUserPrincipal principal) {
        return shoppingListService.getShoppingListsForUser(principal.getId());
    }

    @GetMapping("{id}")
    public ShoppingListView getShoppingList(
            @PathVariable final long id,
            final JwtUserPrincipal principal) throws ShoppingListNotFoundException {

        return shoppingListService.getShoppingListById(id, principal.getId());
    }

    @ExceptionHandler(ShoppingListNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public void handleShoppingListNotFound(final ShoppingListNotFoundException exception) {
        log.info("Exception occurred while fetching shopping list: {}", exception.getMessage());
    }

}
