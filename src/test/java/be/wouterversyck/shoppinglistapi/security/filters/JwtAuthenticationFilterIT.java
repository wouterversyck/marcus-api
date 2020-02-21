package be.wouterversyck.shoppinglistapi.security.filters;

import be.wouterversyck.shoppinglistapi.AbstractIT;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

import java.time.Instant;
import java.util.Collections;
import java.util.Date;

import static java.time.temporal.ChronoUnit.MILLIS;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class JwtAuthenticationFilterIT extends AbstractIT {

    private String signingKey;

    public JwtAuthenticationFilterIT(@Value("${JWT_SECRET}") final String signingKey) {
        this.signingKey = signingKey;
    }

    @Test
    void shouldDenyAccess_WhenUnsignedNonAlgTokenIsProvided() throws Exception {
        String token = buildNonAlgJwtToken();
        getMvc()
                .perform(
                        getWithToken("/admin/users?page=0&size=1", token))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldDenyAccess_WhenExpiredTokenIsProvided() throws Exception {
        String token = buildExpiredToken();
        getMvc()
                .perform(
                        getWithToken("/admin/users?page=0&size=1", token))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldAllowAccess_WhenValidTokenIsProvided() throws Exception {
        String token = buildValidToken();
        getMvc()
                .perform(
                        getWithToken("/admin/users?page=0&size=1", token))
                .andExpect(status().isOk());
    }

    private String buildValidToken() {
        return buildBaseToken()
                .signWith(Keys.hmacShaKeyFor(signingKey.getBytes()), SignatureAlgorithm.HS512)
                .setExpiration(Date.from(
                        Instant.now()
                                .plus(43200000, MILLIS)))
                .compact();
    }

    private String buildExpiredToken() {
        return buildBaseToken()
                .signWith(Keys.hmacShaKeyFor(signingKey.getBytes()), SignatureAlgorithm.HS512)
                .setExpiration(Date.from(
                        Instant.now()
                                .plus(-1, MILLIS)))
                .compact();
    }

    private String buildNonAlgJwtToken() {
        return buildBaseToken()
                .setExpiration(Date.from(
                    Instant.now()
                        .plus(43200000, MILLIS)))
                .compact();
    }

    private JwtBuilder buildBaseToken() {
        return Jwts.builder()
                .setSubject("2")
                .claim("roles", Collections.singletonList("ADMIN"))
                .claim("username", "admin");
    }
}
