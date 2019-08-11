package be.wouterversyck.shoppinglistapi.shoppinglist.services;

import be.wouterversyck.shoppinglistapi.shoppinglist.daos.ShoppingListDao;
import be.wouterversyck.shoppinglistapi.shoppinglist.models.ShoppingList;
import be.wouterversyck.shoppinglistapi.users.models.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShoppingListService {

    private ShoppingListDao shoppingListDao;

    public ShoppingListService(ShoppingListDao shoppingListDao) {
        this.shoppingListDao = shoppingListDao;
    }

    public List<ShoppingList> getShoppingListsForUser(User user) {
        return shoppingListDao.findAllByOwner(user);
    }
}
