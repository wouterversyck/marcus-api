package be.wouterversyck.shoppinglistapi.security.models;

import lombok.Value;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Value
public class JwtUserPrincipal extends AbstractAuthenticationToken {

    private String name;
    private long id;
    private Object credentials;
    private Object principal;

    public JwtUserPrincipal(
            final long id,
            final String name,
            final Collection<? extends GrantedAuthority> authorities,
            final boolean isAuthenticated) {
        super(authorities);
        this.name = name;
        this.id = id;
        this.credentials = null;
        this.principal = name;
        this.setAuthenticated(isAuthenticated);
    }
}
