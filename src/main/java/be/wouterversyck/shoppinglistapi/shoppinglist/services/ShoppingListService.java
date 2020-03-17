package be.wouterversyck.shoppinglistapi.shoppinglist.services;

import be.wouterversyck.shoppinglistapi.shoppinglist.exceptions.ShoppingListNotFoundException;
import be.wouterversyck.shoppinglistapi.shoppinglist.daos.ShoppingListDao;
import be.wouterversyck.shoppinglistapi.shoppinglist.models.ShoppingListView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ShoppingListService {

    private final ShoppingListDao shoppingListDao;

    public ShoppingListService(final ShoppingListDao shoppingListDao) {
        this.shoppingListDao = shoppingListDao;
    }

    public List<ShoppingListView> getShoppingListsForOwner(final long userId) {
        log.info("Fetching shopping lists by owner");
        return shoppingListDao.findAllByOwner(userId);
    }

    public List<ShoppingListView> getShoppingListsForContributor(final long userId) {
        log.info("Fetching shopping lists by user");
        return shoppingListDao.findAllByContributor(userId);
    }

    public ShoppingListView getShoppingListById(final long id, final long userId) throws ShoppingListNotFoundException {
        log.info("Fetching shopping lists with id: {} for user", id);
        return shoppingListDao.findByIdAndOwner(id, userId).orElseThrow(
                () -> new ShoppingListNotFoundException(id)
        );
    }
}
