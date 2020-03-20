package be.wouterversyck.shoppinglistapi.security.utils;

import be.wouterversyck.shoppinglistapi.security.config.SecurityProperties;
import be.wouterversyck.shoppinglistapi.security.models.JwtUserDetails;
import be.wouterversyck.shoppinglistapi.security.models.JwtUserPrincipal;
import be.wouterversyck.shoppinglistapi.users.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.time.temporal.ChronoUnit.MILLIS;

@Slf4j
@AllArgsConstructor
public class JwtService {

    private final SecurityProperties properties;

    public String generateToken(final Authentication authentication) {
        final var signingKey = properties.getSecretKey().getBytes();

        final var user = (JwtUserDetails) authentication.getPrincipal();

        final var roles = user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(signingKey), SignatureAlgorithm.HS512)
                .setHeaderParam("typ", properties.getTokenType())
                .setSubject(String.valueOf(user.getId()))
                .setIssuer(properties.getTokenIssuer())
                .setAudience(properties.getTokenAudience())
                .setId(UUID.randomUUID().toString())
                .setExpiration(Date.from(
                        Instant.now()
                        .plus(properties.getExpiration(), MILLIS)))
                .claim("roles", roles)
                .claim("username", user.getUsername())
                .compact();
    }

    public Authentication parseToken(final String token) {
        validateTokenString(token);

        final var signingKey = properties.getSecretKey().getBytes();

        final var parsedToken = Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token.replace(format("%s ", properties.getTokenPrefix()), ""));

        final var authorities = getAuthorities(parsedToken);
        return new JwtUserPrincipal(getId(parsedToken), getUsername(parsedToken), authorities, true);
    }

    public String generatePasswordResetToken(final User user) {
        final var key = createPasswordKey(user.getPassword());
        return Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(key), SignatureAlgorithm.HS512)
                .setSubject(String.valueOf(user.getUsername()))
                .setIssuer(properties.getTokenIssuer())
                .setAudience(properties.getTokenAudience())
                .setId(UUID.randomUUID().toString())
                .setExpiration(Date.from(
                        Instant.now()
                                .plus(properties.getExpiration(), MILLIS)))
                .compact();
    }

    public void validatePasswordResetToken(final String token, final String previousPassword) {
        final var key = createPasswordKey(previousPassword);

        Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token.replace(format("%s ", properties.getTokenPrefix()), ""));
    }

    public String getUsernameWithoutValidationSignature(final String token) {
        final String[] splitToken = token.split("\\.");
        return Jwts.parserBuilder()
                .build()
                .parseClaimsJwt(splitToken[0] + "." + splitToken[1] + ".")
                .getBody().getSubject();
    }

    private long getId(final Jws<Claims> parsedToken) {
        return Long.parseLong(parsedToken
                .getBody()
                .getSubject());
    }

    private String getUsername(final Jws<Claims> parsedToken) {
        return (String) parsedToken
                .getBody()
                .get("username");
    }

    private List<GrantedAuthority> getAuthorities(final Jws<Claims> parsedToken) {
        return ((List<?>) parsedToken.getBody()
                .get("roles")).stream()
                .map(authority -> new SimpleGrantedAuthority((String) authority))
                .collect(Collectors.toList());
    }

    private void validateTokenString(final String token) {
        if (StringUtils.isEmpty(token)) {
            throw new MalformedJwtException("Token was empty");
        }

        if (!token.startsWith(format("%s ", properties.getTokenPrefix()))) {
            throw new MalformedJwtException(format("Token must start with %s", properties.getTokenPrefix()));
        }
    }

    private byte[] createPasswordKey(final String password) {
        return ((password == null ? "" : password) + properties.getSecretKey()).getBytes();
    }
}
