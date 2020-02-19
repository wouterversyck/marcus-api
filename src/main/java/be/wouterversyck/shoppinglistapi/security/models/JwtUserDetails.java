package be.wouterversyck.shoppinglistapi.security.models;

import lombok.Getter;
import lombok.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import java.util.Collection;

public class JwtUserDetails extends User {

    @Getter
    private long id;

    public JwtUserDetails(long id, String name, String password, Collection<? extends GrantedAuthority> authorities) {
        super(name, password, authorities);
        this.id = id;
    }
}
