package be.wouterversyck.shoppinglistapi.notes.controllers;

import be.wouterversyck.shoppinglistapi.notes.models.ShoppingList;
import be.wouterversyck.shoppinglistapi.security.models.JwtUserPrincipal;
import be.wouterversyck.shoppinglistapi.notes.exceptions.ShoppingListNotFoundException;
import be.wouterversyck.shoppinglistapi.notes.services.ShoppingListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("shoppinglist")
public class NotesController {
    private final ShoppingListService shoppingListService;

    public NotesController(final ShoppingListService shoppingListService) {
        this.shoppingListService = shoppingListService;
    }

    @GetMapping("all")
    public List<ShoppingList> getShoppingLists(final JwtUserPrincipal principal) {
        return shoppingListService.getShoppingListsForContributor(principal.getId());
    }

    @GetMapping("{id}")
    public ShoppingList getShoppingList(
            @PathVariable final String id,
            final JwtUserPrincipal principal) throws ShoppingListNotFoundException {

        return shoppingListService.getShoppingListById(id, principal.getId());
    }

    @PostMapping("save")
    public void saveShoppingList(
            @RequestBody final ShoppingList request,
            final JwtUserPrincipal principal) {
        this.shoppingListService.saveShoppingList(request, principal.getId());
    }

    @ExceptionHandler(ShoppingListNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public void handleShoppingListNotFound(final ShoppingListNotFoundException exception) {
        log.info("Exception occurred while fetching shopping list: {}", exception.getMessage());
    }

}
