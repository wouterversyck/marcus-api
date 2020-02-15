package be.wouterversyck.shoppinglistapi.users.testmodels;

import be.wouterversyck.shoppinglistapi.users.models.Role;
import be.wouterversyck.shoppinglistapi.users.models.SecureUserView;
import lombok.Builder;

@Builder
public class SecureUserImpl implements SecureUserView {

    private long id;
    private String username;
    private Role role;

    public SecureUserImpl(long id, String username, Role role) {
        this.role = role;
        this.id = id;
        this.username = username;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public Role getRole() {
        return role;
    }
}
