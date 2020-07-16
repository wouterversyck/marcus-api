package be.wouterversyck.shoppinglistapi.notes.services;

import be.wouterversyck.shoppinglistapi.notes.exceptions.ShoppingListNotFoundException;
import be.wouterversyck.shoppinglistapi.notes.models.ShoppingList;
import be.wouterversyck.shoppinglistapi.notes.persistence.ShoppingListDao;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Slf4j
@Service
public class ShoppingListService {

    private final ShoppingListDao shoppingListDao;

    public List<ShoppingList> getShoppingListsForUser(final long userId) {
        log.info("Fetching shopping lists by user");
        return shoppingListDao.findAllByContributors(userId);
    }

    public ShoppingList getShoppingListByIdForUser(final String id, final long userId) throws ShoppingListNotFoundException {
        log.info("Fetching shopping lists with id: {} for user", id);
        return shoppingListDao.findByIdAndContributors(id, userId).orElseThrow(
                () -> new ShoppingListNotFoundException(id)
        );
    }

    public void deleteForUser(final String id, final long userId) {
        shoppingListDao.deleteByIdAndContributors(id, userId);
    }

    public void deleteAllByOwner(final long userId) {
        shoppingListDao.deleteAllByOwner(userId);
    }

    public ShoppingList saveShoppingList(
            final ShoppingList request, final long userId) {
        log.info("Saving shopping list with id {}", request.getId());

        request.setOwner(userId);
        List<Long> contributors = request.getContributors();
        if (contributors == null) {
            contributors = new ArrayList<>();
        }
        if (!contributors.contains(userId)) {
            contributors.add(userId);
        }

        request.setContributors(contributors);
        return shoppingListDao.save(request);
    }
}
