package be.wouterversyck.shoppinglistapi.shoppinglist.services;

import be.wouterversyck.shoppinglistapi.shoppinglist.ShoppingListNotFoundException;
import be.wouterversyck.shoppinglistapi.shoppinglist.daos.ShoppingListDao;
import be.wouterversyck.shoppinglistapi.shoppinglist.models.ShoppingListView;
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
    public List<ShoppingListView> getShoppingListsForUser(final long userId) {
        log.info("Fetching shopping lists form db (cache miss)");
        return shoppingListDao.findAllByOwner(userId);
    }

    @Cacheable(value = "be.wouterversyck.shoppinglistapi.shoppinglist.findbyid")
    public ShoppingListView getShoppingListById(final long id, final long userId) throws ShoppingListNotFoundException {
        log.info("Fetching shopping lists form db (cache miss)");
        return shoppingListDao.findByIdAndOwner(id, userId).orElseThrow(
                () -> new ShoppingListNotFoundException(id)
        );
    }
}
