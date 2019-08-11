package be.wouterversyck.shoppinglistapi.users.daos;

import be.wouterversyck.shoppinglistapi.users.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
