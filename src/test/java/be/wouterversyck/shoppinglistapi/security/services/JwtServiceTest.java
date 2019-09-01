package be.wouterversyck.shoppinglistapi.security.services;

import io.jsonwebtoken.Jwts;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.userdetails.User;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class JwtServiceTest {

    private JwtService jwtService = new JwtService();

    private static final String USERNAME = "USERNAME";

    @Before
    public void setup() {
        jwtService.jwtSecretKey = "dddddddddddddddddddfffffffffffffffffffffffcccccccccccccccccccccceeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee";
    }

    @Test
    public void shouldReturnEmpty_WhenInvalidTokenIsProvided() {
        var token = jwtService.parseToken("Bearer token");

        assertThat(token.isEmpty()).isTrue();
    }

    @Test
    public void shouldReturnEmpty_WhenNoTokenIsProvided() {
        var token = jwtService.parseToken("");

        assertThat(token.isEmpty()).isTrue();
    }

    @Test
    public void test() {
        User user = new User(USERNAME, "password", Collections.emptyList());
        String result = jwtService.generateToken(user);

        var parsedToken = Jwts.parser()
                .setSigningKey(jwtService.jwtSecretKey.getBytes())
                .parseClaimsJws(result);

        assertThat(parsedToken.getBody().getSubject()).isEqualTo(USERNAME);
    }
}
