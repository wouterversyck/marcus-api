package be.wouterversyck.shoppinglistapi.users.services;

import be.wouterversyck.shoppinglistapi.users.models.RoleEntity;
import be.wouterversyck.shoppinglistapi.users.persistence.RolesDao;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RolesService {
    private RolesDao rolesDao;

    @Cacheable(value = "be.wouterversyck.shoppinglistapi.users.role")
    public List<RoleEntity> getRoles() {
        return rolesDao.findAll();
    }
}
