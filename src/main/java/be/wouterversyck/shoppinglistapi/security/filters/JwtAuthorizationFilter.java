package be.wouterversyck.shoppinglistapi.security.filters;

import be.wouterversyck.shoppinglistapi.security.utils.SecurityConstants;
import be.wouterversyck.shoppinglistapi.security.services.JwtService;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final JwtService jwtService;

    public JwtAuthorizationFilter(final AuthenticationManager authenticationManager, final JwtService jwtService) {
        super(authenticationManager);
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
                                    final FilterChain filterChain) throws IOException, ServletException {
        try {
            final var authenticationToken = getAuthentication(request);
            log.info("Authenticated request for user with username: [{}]", authenticationToken.getPrincipal());

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            MDC.put("user", authenticationToken.getName());

            filterChain.doFilter(request, response);
        } catch(final JwtException ex) {
            log.warn(ex.getMessage());
            filterChain.doFilter(request, response);
        }
    }

    private UsernamePasswordAuthenticationToken getAuthentication(final HttpServletRequest request) {
        final var token = request.getHeader(SecurityConstants.TOKEN_HEADER);

        return jwtService.parseToken(token);
    }
}
