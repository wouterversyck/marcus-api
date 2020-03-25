package be.wouterversyck.shoppinglistapi.security.utils;

import be.wouterversyck.shoppinglistapi.security.models.JwtUserDetails;
import be.wouterversyck.shoppinglistapi.security.models.JwtUserPrincipal;
import be.wouterversyck.shoppinglistapi.security.config.SecurityProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import static java.lang.String.format;
import static java.time.Instant.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JwtServiceTest {

    private static final String JWT_SECRET_KEY = "dddddddddddddddddddfffffffffffffffffffffffcccccccccccccccccccccceeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee";
    private JwtService jwtService;
    private SecurityProperties properties;

    @BeforeEach
    public void setup() {
        properties = new SecurityProperties();
        properties.setTokenHeader("Authorization");
        properties.setResponseTokenHeader("x-token");
        properties.setTokenPrefix("Bearer");
        properties.setTokenType("JWT");
        properties.setTokenIssuer("");
        properties.setTokenAudience("");
        properties.setExpiration(43200000);
        properties.setSecretKey(JWT_SECRET_KEY);
        jwtService = new JwtService(properties);
    }

    private static final String USERNAME = "USERNAME";
    private static final String USER_ID = "1";
    private static final String USER_ROLE = "USER";

    @Test
    void shouldThrowMalformedJwtException_WhenInvalidTokenIsProvided() {
        assertThrows(MalformedJwtException.class, () -> jwtService.parseToken("Bearer token"));

    }

    @Test
    void shouldThrowMalformedJwtException_WhenNoTokenIsProvided() {
        assertThrows(MalformedJwtException.class, () -> jwtService.parseToken(""));
    }

    @Test
    void shouldThrowMalformedJwtException_WhenTokenDoesNotStartWithPrefix() {
        assertThrows(MalformedJwtException.class, () -> jwtService.parseToken(generateRawToken()));
    }

    @Test
    void shouldThrowExpiredJwtException_WhenTokenIsExpired() {
        String token = generateExpiredToken();
        assertThrows(ExpiredJwtException.class, () -> jwtService.parseToken(token));
    }

    @Test
    void shouldThrowUnsupportedJwtException_WhenTokenIsHasNoSignature() {
        String token = generateValidToken();
        final String[] splitToken = token.split("\\.");
        assertThrows(UnsupportedJwtException.class, () -> jwtService.parseToken(splitToken[0] + "." + splitToken[1] + "."));
    }

    @Test
    void shouldReturnUsername_WhenGetUsernameMethodIsCalled() {
        var token = generateRawToken();
        var result = jwtService.getUsernameWithoutValidationSignature(token);

        assertThat(result).isEqualTo("1");
    }

    @Test
    void shouldCreateCorrectPrincipal_WhenTokenStringIsPassed() {
        String token = generateValidToken();

        var principal = (JwtUserPrincipal) jwtService.parseToken(token);

        assertThat(principal.getName()).isEqualTo(USERNAME);
        assertThat(principal.getId()).isEqualTo(Long.parseLong(USER_ID));
    }

    @Test
    void shouldGenerateCorrectTokenString_WhenUserIsPassed() {
        var roles = Collections.singletonList(new SimpleGrantedAuthority(USER_ROLE));
        var authentication = new UsernamePasswordAuthenticationToken(
                new JwtUserDetails(
                        Long.parseLong(USER_ID),
                        USERNAME,
                        "pass",
                        roles), null, roles);

        var rawToken = jwtService.generateToken(authentication);
        var parsedToken = parseToken(rawToken);

        assertThat(parsedToken.getBody().getExpiration())
                .isInSameMinuteAs(Date.from(now().plus(properties.getExpiration(), ChronoUnit.MILLIS)));
        assertThat(parsedToken.getBody().get("username")).isEqualTo(USERNAME);
        assertThat(parsedToken.getBody().getSubject()).isEqualTo(USER_ID);
        assertThat((Collection<String>) parsedToken.getBody().get("roles")).contains(USER_ROLE);
        assertThat(parsedToken.getBody().getId()).isNotBlank().hasSize(36);
        assertThat(parsedToken.getSignature()).isNotBlank().hasSize(86);
    }

    private String generateRawToken() {
        return generateToken(new Date(System.currentTimeMillis() + 864000000));
    }

    private String generateValidToken() {
        String token = generateToken(new Date(System.currentTimeMillis() + 864000000));

        return format("Bearer %s", token);
    }

    private String generateExpiredToken() {
        Calendar date = Calendar.getInstance();
        date.add(Calendar.MONTH, -1);
        String token = generateToken(date.getTime());

        return format("Bearer %s", token);
    }

    private String generateToken(Date expirationDate) {
        return Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(JWT_SECRET_KEY.getBytes()), SignatureAlgorithm.HS512)
                .setHeaderParam("typ", properties.getTokenType())
                .setIssuer(properties.getTokenIssuer())
                .setAudience(properties.getTokenAudience())
                .setSubject(USER_ID)
                .claim("username", USERNAME)
                .setExpiration(expirationDate)
                .claim("roles", Collections.emptyList())
                .compact();
    }

    private Jws<Claims> parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(JWT_SECRET_KEY.getBytes())
                .build()
                .parseClaimsJws(token);
    }
}
