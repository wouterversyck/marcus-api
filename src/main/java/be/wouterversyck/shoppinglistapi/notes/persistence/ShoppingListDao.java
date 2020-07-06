package be.wouterversyck.shoppinglistapi.notes.persistence;

import be.wouterversyck.shoppinglistapi.notes.models.ShoppingList;
import be.wouterversyck.shoppinglistapi.notes.models.ShoppingListView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ShoppingListDao extends JpaRepository<ShoppingList, Long> {
    @Query("select s from ShoppingList s join s.owner o where o.id = :#{#userId}")
    List<ShoppingListView> findAllByOwner(long userId);

    @Query("select s from ShoppingList s join s.owner o where o.id = :#{#userId}")
    List<ShoppingList> findAllEntitiesByOwner(long userId);

    @Query("select s from ShoppingList s join s.contributors c where c.id = :#{#contributorId}")
    List<ShoppingListView> findAllByContributor(long contributorId);

    @Query("select s from ShoppingList s join s.owner o where o.id = :#{#ownerId} and s.id = :#{#shoppingListId}")
    Optional<ShoppingListView> findByIdAndOwner(long shoppingListId, long ownerId);
}
