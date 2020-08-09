package be.wouterversyck.marcusapi.security.models;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import java.util.Collection;

public class JwtUserDetails extends User {

    @Getter
    private long id;

    public JwtUserDetails(
            final long id,
            final String name,
            final String password,
            final Collection<? extends GrantedAuthority> authorities) {
        super(name, password, authorities);
        this.id = id;
    }
}
