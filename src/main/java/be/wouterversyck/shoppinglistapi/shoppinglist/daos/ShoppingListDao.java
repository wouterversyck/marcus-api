package be.wouterversyck.shoppinglistapi.shoppinglist.daos;

import be.wouterversyck.shoppinglistapi.shoppinglist.models.ShoppingList;
import be.wouterversyck.shoppinglistapi.shoppinglist.models.ShoppingListView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ShoppingListDao extends JpaRepository<ShoppingList, Long> {
    @Query("select s from ShoppingList s join s.owner o where o.id = :#{#userId}")
    List<ShoppingListView> findAllByOwner(long userId);

    @Query("select s from ShoppingList s join s.owner o where o.id = :#{#ownerId} and s.id = :#{#shoppingListId}")
    Optional<ShoppingListView> findByIdAndOwner(Long shoppingListId, long ownerId);
}
