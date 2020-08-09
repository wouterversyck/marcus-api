package be.wouterversyck.marcusapi.users.services;

import be.wouterversyck.marcusapi.users.models.RoleEntity;
import be.wouterversyck.marcusapi.users.persistence.RolesDao;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RolesService {
    private RolesDao rolesDao;

    @Cacheable(value = "be.wouterversyck.marcusapi.users.role")
    public List<RoleEntity> getRoles() {
        return rolesDao.findAll();
    }
}
