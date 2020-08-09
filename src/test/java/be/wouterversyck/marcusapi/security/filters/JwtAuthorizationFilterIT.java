package be.wouterversyck.marcusapi.security.filters;

import be.wouterversyck.marcusapi.AbstractIT;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

import java.time.Instant;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;

import static java.lang.String.format;
import static java.time.temporal.ChronoUnit.MILLIS;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

class JwtAuthorizationFilterIT extends AbstractIT {

    private String signingKey;

    public JwtAuthorizationFilterIT(@Value("${jwt.secret}") final String signingKey) {
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
    void shouldDenyAccess_WhenNoTokenIsProvided() throws Exception {
        getMvc()
                .perform(
                        get("/admin/users?page=0&size=1"))
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
    void shouldDenyAccess_WhenValidTokenIsProvidedButTokenHasBeenTamperedWith() throws Exception {
        String token = buildTamperedToken();
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

    private String buildTamperedToken() {
        String token = buildValidToken();
        String[] jwtArray = token.split("\\.");
        String middle = jwtArray[1];

        String middleDecoded = new String(Base64.getDecoder().decode(middle));
        middleDecoded = middleDecoded.replace("admin", "user");

        String result = Base64.getEncoder().withoutPadding().encodeToString(middleDecoded.getBytes());
        return format("%s.%s.%s", jwtArray[0], result, jwtArray[2]);
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
