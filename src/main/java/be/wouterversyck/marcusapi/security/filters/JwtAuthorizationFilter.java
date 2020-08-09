package be.wouterversyck.marcusapi.security.filters;

import be.wouterversyck.marcusapi.security.config.SecurityProperties;
import be.wouterversyck.marcusapi.security.utils.JwtService;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final SecurityProperties properties;

    public JwtAuthorizationFilter(final JwtService jwtService, final SecurityProperties properties) {
        this.jwtService = jwtService;
        this.properties = properties;
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
                                    final FilterChain filterChain) throws IOException, ServletException {
        final var token = request.getHeader(properties.getTokenHeader());

        // Filter will always be called (even on anonymous endpoints, so skip auth for anonymous endpoints
        if (token != null) {
            authenticateUser(token);
        }

        filterChain.doFilter(request, response);
    }

    private void authenticateUser(final String token) {
        try {
            final var authentication = jwtService.parseToken(token);

            log.info("Authenticated request for user with username: [{}]", authentication.getPrincipal());

            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch(final JwtException ex) {
            log.info(ex.getMessage());
            SecurityContextHolder.clearContext();
        }
    }
}
