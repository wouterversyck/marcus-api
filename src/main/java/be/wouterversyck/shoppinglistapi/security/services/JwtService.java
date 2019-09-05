package be.wouterversyck.shoppinglistapi.security.services;

import be.wouterversyck.shoppinglistapi.security.utils.SecurityConstants;
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

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class JwtService {

    // provide key through jvm property (-DJWT_SECRET=<key>)
    private final String jwtSecretKey;

    public JwtService(final String jwtSecretKey) {
        this.jwtSecretKey = jwtSecretKey;
    }

    public String generateToken(final User user) {
        final var signingKey = jwtSecretKey.getBytes();

        final var roles = user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(signingKey), SignatureAlgorithm.HS512)
                .setHeaderParam("typ", SecurityConstants.TOKEN_TYPE)
                .setIssuer(SecurityConstants.TOKEN_ISSUER)
                .setAudience(SecurityConstants.TOKEN_AUDIENCE)
                .setSubject(user.getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + 864000000))
                .claim("roles", roles)
                .compact();
    }

    public UsernamePasswordAuthenticationToken parseToken(final String token) {
        if (StringUtils.isEmpty(token) || !token.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            throw new MalformedJwtException("Token was empty");
        }

        final var signingKey = jwtSecretKey.getBytes();

        final var parsedToken = Jwts.parser()
                .setSigningKey(signingKey)
                .parseClaimsJws(token.replace("Bearer ", ""));

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
