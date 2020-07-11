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

    public List<ShoppingList> getShoppingListsForContributor(final long userId) {
        log.info("Fetching shopping lists by user");
        return shoppingListDao.findAllByContributors(userId);
    }

    public ShoppingList getShoppingListById(final String id, final long userId) throws ShoppingListNotFoundException {
        log.info("Fetching shopping lists with id: {} for user", id);
        return shoppingListDao.findByIdAndOwner(id, userId).orElseThrow(
                () -> new ShoppingListNotFoundException(id)
        );
    }

    public void saveShoppingList(
            final ShoppingList request, final long ownerId) {
        log.info("Saving shopping list with id {}", request.getId());

        request.setOwner(ownerId);
        List<Long> contributors = request.getContributors();
        if (contributors == null) {
            contributors = new ArrayList<>();
        }
        if (!contributors.contains(ownerId)) {
            contributors.add(ownerId);
        }

        request.setContributors(contributors);
        shoppingListDao.save(request);
    }
}
