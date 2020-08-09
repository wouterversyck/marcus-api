package be.wouterversyck.marcusapi.users.testmodels;

import be.wouterversyck.marcusapi.users.models.DangerUserView;
import be.wouterversyck.marcusapi.users.models.Role;
import lombok.Builder;

@Builder
public class DangerUserImpl implements DangerUserView {

    private long id;
    private String username;
    private Role role;
    private String password;

    public DangerUserImpl(long id, String username, Role role, String password) {
        this.role = role;
        this.id = id;
        this.username = username;
        this.password = password;
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
    public String getPassword() {
        return password;
    }

    @Override
    public Role getRole() {
        return role;
    }
}
