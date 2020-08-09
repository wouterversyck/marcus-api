package be.wouterversyck.marcusapi.users.services;

import be.wouterversyck.marcusapi.users.models.RoleEntity;
import be.wouterversyck.marcusapi.users.persistence.RolesDao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RolesServiceTest {
    private static final String ROLE_NAME = "USER";

    @Mock
    private RolesDao rolesDao;
    @InjectMocks
    private RolesService service;

    @Test
    void shouldReturnRoles_WhenRolesAreRequested() {
        var role = new RoleEntity();
        role.setName(ROLE_NAME);

        when(rolesDao.findAll()).thenReturn(Collections.singletonList(role));

        var roles = service.getRoles();

        assertThat(roles.size()).isEqualTo(1);
        assertThat(roles).extracting("name")
                .contains(ROLE_NAME);
    }
}