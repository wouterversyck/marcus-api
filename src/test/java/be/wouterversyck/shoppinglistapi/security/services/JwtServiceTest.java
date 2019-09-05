package be.wouterversyck.shoppinglistapi.security.services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.userdetails.User;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class JwtServiceTest {

    private String jwtSecretKey = "dddddddddddddddddddfffffffffffffffffffffffcccccccccccccccccccccceeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee";
    private JwtService jwtService = new JwtService(jwtSecretKey);

    private static final String USERNAME = "USERNAME";
    private static final String TOKEN = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJzZWN1cmUtYXBpIiwiYXVkIjoic2VjdXJlLWFwcCIsInN1YiI6IlVTRVJOQU1FIiwiZXhwIjoxNTY4MzE3NDgxLCJyb2xlcyI6W119.U-iG5jWQJ6I7Qf7b02bS6Q7uz6KUNUA6423pq2ceHQ7QOEZSqpz-pSxkxa3cteDQDiTBpQbXFDxI1zePTmJpXQ";

    @Test(expected = MalformedJwtException.class)
    public void shouldReturnEmpty_WhenInvalidTokenIsProvided() {
        jwtService.parseToken("Bearer token");
    }

    @Test(expected = MalformedJwtException.class)
    public void shouldReturnEmpty_WhenNoTokenIsProvided() {
        jwtService.parseToken("");
    }

    @Test
    public void shouldCreateCorrectUsernamePasswordAuthenticationToken_WhenTokenStringIsPassed() {
        var authenticationToken = jwtService.parseToken(TOKEN);

        assertThat(authenticationToken.getPrincipal()).isEqualTo(USERNAME);
    }

    @Test
    public void shouldGenerateCorrectTokenString_WhenUserIsPassed() {
        User user = new User(USERNAME, "password", Collections.emptyList());
        String result = jwtService.generateToken(user);

        var parsedToken = Jwts.parser()
                .setSigningKey(jwtSecretKey.getBytes())
                .parseClaimsJws(result);

        assertThat(parsedToken.getBody().getSubject()).isEqualTo(USERNAME);
    }
}
