package be.wouterversyck.shoppinglistapi.shoppinglist.services;

import be.wouterversyck.shoppinglistapi.shoppinglist.ShoppingListNotFoundException;
import be.wouterversyck.shoppinglistapi.shoppinglist.daos.ShoppingListDao;
import be.wouterversyck.shoppinglistapi.shoppinglist.models.ShoppingListDto;
import be.wouterversyck.shoppinglistapi.users.models.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ShoppingListService {

    private final ShoppingListDao shoppingListDao;

    public ShoppingListService(final ShoppingListDao shoppingListDao) {
        this.shoppingListDao = shoppingListDao;
    }

    @Cacheable("be.wouterversyck.shoppinglistapi.shoppinglist.findforuser")
    public List<ShoppingListDto> getShoppingListsForUser(final User user) {
        log.info("Fetching shopping lists form db (cache miss)");
        return shoppingListDao.findAllByOwner(user);
    }

    @Cacheable(value = "be.wouterversyck.shoppinglistapi.shoppinglist.findbyid", key = "#id")
    public ShoppingListDto getShoppingListById(final long id, final User user) throws ShoppingListNotFoundException {
        log.info("Fetching shopping lists form db (cache miss)");
        return shoppingListDao.findByIdAndOwner(id, user).orElseThrow(
                () -> new ShoppingListNotFoundException(id)
        );
    }
}
