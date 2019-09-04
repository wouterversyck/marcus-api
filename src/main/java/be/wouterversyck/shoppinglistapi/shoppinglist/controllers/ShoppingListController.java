package be.wouterversyck.shoppinglistapi.shoppinglist.controllers;

import be.wouterversyck.shoppinglistapi.shoppinglist.ShoppingListNotFoundException;
import be.wouterversyck.shoppinglistapi.shoppinglist.models.ShoppingListDto;
import be.wouterversyck.shoppinglistapi.shoppinglist.services.ShoppingListService;
import be.wouterversyck.shoppinglistapi.users.models.User;
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
    private ShoppingListService shoppingListService;
    private UserService userService;

    public ShoppingListController(ShoppingListService shoppingListService, UserService userService) {
        this.shoppingListService = shoppingListService;
        this.userService = userService;
    }

    @GetMapping("all")
    public List<ShoppingListDto> getShoppingLists(HttpServletRequest request) {
        User user = getCurrentUser(request);

        log.info("Getting shopping list for user with username [{}]", user.getUsername());
        return shoppingListService.getShoppingListsForUser(user);
    }

    @GetMapping("{id}")
    public ShoppingListDto getShoppingList(@PathVariable long id, HttpServletRequest request) throws ShoppingListNotFoundException {
        User user = getCurrentUser(request);

        return shoppingListService.getShoppingListById(id, user);
    }

    private User getCurrentUser(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        return userService.getUserByUsername(principal.getName());
    }

    @ExceptionHandler(ShoppingListNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public void handleShoppingListNotFound(ShoppingListNotFoundException exception) {
        log.info("Exception occurred while fetching shopping list: {}", exception.getMessage());
    }

}
