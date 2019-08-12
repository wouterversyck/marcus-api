package be.wouterversyck.shoppinglistapi.shoppinglist.controllers;

import be.wouterversyck.shoppinglistapi.shoppinglist.models.ShoppingListDto;
import be.wouterversyck.shoppinglistapi.shoppinglist.services.ShoppingListService;
import be.wouterversyck.shoppinglistapi.users.models.User;
import be.wouterversyck.shoppinglistapi.users.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @GetMapping
    public List<ShoppingListDto> getShoppingList(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        User user = userService.getUserByUsername(principal.getName());

        log.info("Getting shopping list for user with username [{}]", user.getUsername());
        return shoppingListService.getShoppingListsForUser(user);
    }
}
