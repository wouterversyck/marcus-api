package be.wouterversyck.shoppinglistapi.shoppinglist.controllers;

import be.wouterversyck.shoppinglistapi.shoppinglist.ShoppingListNotFoundException;
import be.wouterversyck.shoppinglistapi.shoppinglist.models.ShoppingListView;
import be.wouterversyck.shoppinglistapi.shoppinglist.services.ShoppingListService;
import be.wouterversyck.shoppinglistapi.users.exceptions.UserNotFoundException;
import be.wouterversyck.shoppinglistapi.users.models.SecureUserView;
import be.wouterversyck.shoppinglistapi.users.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("shoppinglist")
public class ShoppingListController {
    private final ShoppingListService shoppingListService;
    private final UserService userService;

    public ShoppingListController(final ShoppingListService shoppingListService, final UserService userService) {
        this.shoppingListService = shoppingListService;
        this.userService = userService;
    }

    @GetMapping("all")
    public List<ShoppingListView> getShoppingLists(final HttpServletRequest request) throws UserNotFoundException {
        final var user = getCurrentUser(request);

        log.info("Getting shopping list for user with username [{}]", user.getUsername());
        return shoppingListService.getShoppingListsForUser(user);
    }

    @GetMapping("{id}")
    public ShoppingListView getShoppingList(
            @PathVariable final long id,
            final HttpServletRequest request) throws ShoppingListNotFoundException, UserNotFoundException {
        final var user = getCurrentUser(request);

        return shoppingListService.getShoppingListById(id, user);
    }

    private SecureUserView getCurrentUser(final HttpServletRequest request) throws UserNotFoundException {
        final Principal principal = request.getUserPrincipal();
        return userService.getUserByUsername(principal.getName());
    }

    @ExceptionHandler(ShoppingListNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public void handleShoppingListNotFound(final ShoppingListNotFoundException exception) {
        log.info("Exception occurred while fetching shopping list: {}", exception.getMessage());
    }

}
