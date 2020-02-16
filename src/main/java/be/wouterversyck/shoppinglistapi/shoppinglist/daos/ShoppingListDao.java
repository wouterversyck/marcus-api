package be.wouterversyck.shoppinglistapi.shoppinglist.daos;

import be.wouterversyck.shoppinglistapi.shoppinglist.models.ShoppingList;
import be.wouterversyck.shoppinglistapi.shoppinglist.models.ShoppingListView;
import be.wouterversyck.shoppinglistapi.users.models.SecureUserView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ShoppingListDao extends JpaRepository<ShoppingList, Long> {
    @Query("select s from ShoppingList s join s.owner o where o.id = :#{#user.id}")
    List<ShoppingListView> findAllByOwner(SecureUserView user);

    @Query("select s from ShoppingList s join s.owner o where o.id = :#{#owner.id} and s.id = :#{#shoppingListId}")
    Optional<ShoppingListView> findByIdAndOwner(Long shoppingListId, SecureUserView owner);
}
