package be.wouterversyck.marcusapi.users.models;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SecureUserViewImpl implements SecureUserView {
    private long id;
    private String username;
    private String email;
    private Role role;
}
