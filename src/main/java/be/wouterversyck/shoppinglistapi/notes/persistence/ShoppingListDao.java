package be.wouterversyck.shoppinglistapi.notes.persistence;

import be.wouterversyck.shoppinglistapi.notes.models.ShoppingList;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ShoppingListDao extends MongoRepository<ShoppingList, Long> {
    List<ShoppingList> findAllByOwner(long userId);
    List<ShoppingList> findAllByContributors(long contributorId);
    void deleteAllByOwner(long userId);
    ShoppingList findByIdAndContributors(String shoppingListId, long contributorId);
    Optional<ShoppingList> findByIdAndOwner(String shoppingListId, long ownerId);
    void deleteById(String id);
}
