package be.wouterversyck.shoppinglistapi.users.persistence;

import be.wouterversyck.shoppinglistapi.users.models.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolesDao extends JpaRepository<RoleEntity, Long> {
}
