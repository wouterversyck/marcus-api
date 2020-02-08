package be.wouterversyck.shoppinglistapi.security.utils;

import be.wouterversyck.shoppinglistapi.security.config.SecurityProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.time.temporal.ChronoUnit.MILLIS;

@Slf4j
public class JwtService {

    // provide key through jvm property (-DJWT_SECRET=<key>)
    private final String jwtSecretKey;
    private final SecurityProperties properties;

    public JwtService(final String jwtSecretKey, final SecurityProperties properties) {
        this.jwtSecretKey = jwtSecretKey;
        this.properties = properties;
    }

    public String generateToken(final User user) {
        final var signingKey = jwtSecretKey.getBytes();

        final var roles = user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(signingKey), SignatureAlgorithm.HS512)
                .setHeaderParam("typ", properties.getTokenType())
                .setIssuer(properties.getTokenIssuer())
                .setAudience(properties.getTokenAudience())
                .setSubject(user.getUsername())
                .setExpiration(Date.from(
                        Instant.now()
                        .plus(properties.getExpiration(), MILLIS)))
                .claim("roles", roles)
                .compact();
    }

    public UsernamePasswordAuthenticationToken parseToken(final String token) {
        if (StringUtils.isEmpty(token)) {
            throw new MalformedJwtException("Token was empty");
        }

        if (!token.startsWith(format("%s ", properties.getTokenPrefix()))) {
            throw new MalformedJwtException(format("Token must start with %s", properties.getTokenPrefix()));
        }

        final var signingKey = jwtSecretKey.getBytes();

        final var parsedToken = Jwts.parser()
                .setSigningKey(signingKey)
                .parseClaimsJws(token.replace(format("%s ", properties.getTokenPrefix()), ""));

        final var username = parsedToken
                .getBody()
                .getSubject();

        if (StringUtils.isEmpty(username)) {
            throw new MalformedJwtException("No username found");
        }
        final var authorities = ((List<?>) parsedToken.getBody()
                .get("roles")).stream()
                .map(authority -> new SimpleGrantedAuthority((String) authority))
                .collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken(username, null, authorities);
    }
}
