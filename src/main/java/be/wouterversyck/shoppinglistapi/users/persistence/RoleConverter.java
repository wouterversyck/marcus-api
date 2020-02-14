package be.wouterversyck.shoppinglistapi.users.persistence;

import be.wouterversyck.shoppinglistapi.users.models.Role;

import javax.persistence.AttributeConverter;

public class RoleConverter implements AttributeConverter<Role, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Role role) {
        return role.getId();
    }

    @Override
    public Role convertToEntityAttribute(Integer id) {
        return Role.fromId(id);
    }
}
