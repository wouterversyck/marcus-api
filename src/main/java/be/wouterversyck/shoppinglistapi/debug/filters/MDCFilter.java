package be.wouterversyck.shoppinglistapi.debug.filters;

import be.wouterversyck.shoppinglistapi.debug.config.MDCProperties;
import be.wouterversyck.shoppinglistapi.security.config.SecurityProperties;
import be.wouterversyck.shoppinglistapi.security.utils.JwtService;
import org.slf4j.MDC;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@Component
public class MDCFilter extends OncePerRequestFilter {

    private JwtService jwtService;
    private SecurityProperties securityProperties;
    private MDCProperties mdcProperties;

    public MDCFilter(final JwtService jwtService,
                     final SecurityProperties securityProperties,
                     final MDCProperties mdcProperties) {
        this.jwtService = jwtService;
        this.securityProperties = securityProperties;
        this.mdcProperties = mdcProperties;
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {
        final var authToken = getAuthentication(request);
        final var correlationID = newCorrelationId();

        MDC.put(mdcProperties.getUserMdcKey(), authToken.getName());
        MDC.put(mdcProperties.getCorrelationIdMdcKey(), correlationID);

        response.addHeader(mdcProperties.getCorrelationIdHeaderKey(), correlationID);
        filterChain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(final HttpServletRequest request) {
        final var token = request.getHeader(securityProperties.getTokenHeader());

        return jwtService.parseToken(token);
    }

    private String newCorrelationId(){
        return UUID.randomUUID().toString().toUpperCase();
    }
}
