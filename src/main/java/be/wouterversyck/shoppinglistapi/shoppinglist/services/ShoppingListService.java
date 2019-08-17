package be.wouterversyck.shoppinglistapi.shoppinglist.services;

import be.wouterversyck.shoppinglistapi.shoppinglist.ShoppingListNotFoundException;
import be.wouterversyck.shoppinglistapi.shoppinglist.daos.ShoppingListDao;
import be.wouterversyck.shoppinglistapi.shoppinglist.models.ShoppingListDto;
import be.wouterversyck.shoppinglistapi.users.models.User;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShoppingListService {

    private ShoppingListDao shoppingListDao;

    public ShoppingListService(ShoppingListDao shoppingListDao) {
        this.shoppingListDao = shoppingListDao;
    }

    @Cacheable("be.wouterversyck.shoppinglistapi.shoppinglist.findforuser")
    public List<ShoppingListDto> getShoppingListsForUser(User user) {
        return shoppingListDao.findAllByOwner(user);
    }

    @Cacheable("be.wouterversyck.shoppinglistapi.shoppinglist.findfbyid")
    public ShoppingListDto getShoppingListById(long id, User user) throws ShoppingListNotFoundException {
        return shoppingListDao.findByIdAndOwner(id, user).orElseThrow(
                () -> new ShoppingListNotFoundException(id)
        );
    }
}
