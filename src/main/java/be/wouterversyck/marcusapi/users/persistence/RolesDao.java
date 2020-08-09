package be.wouterversyck.marcusapi.users.persistence;

import be.wouterversyck.marcusapi.users.models.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolesDao extends JpaRepository<RoleEntity, Long> {
}
