package be.wouterversyck.shoppinglistapi.security.filters;

import be.wouterversyck.shoppinglistapi.security.models.LoginRequest;
import be.wouterversyck.shoppinglistapi.security.config.SecurityProperties;
import be.wouterversyck.shoppinglistapi.security.utils.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

import static java.lang.String.format;

@Slf4j
public class JwtLoginFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtService jwtService;
    private final SecurityProperties properties;

    public JwtLoginFilter(final AuthenticationManager authenticationManager, final JwtService jwtService,
                          final AuthenticationFailureHandler failureHandler,
                          final SecurityProperties properties) {
        setFilterProcessesUrl(properties.getAuthLoginUrl());
        setAuthenticationManager(authenticationManager);
        setAuthenticationFailureHandler(failureHandler);
        setPostOnly(true);

        this.jwtService = jwtService;
        this.properties = properties;
    }

    @Override
    public Authentication attemptAuthentication(final HttpServletRequest request, final HttpServletResponse response) throws AuthenticationException {
        final LoginRequest loginRequest = getLoginRequestFromHttpRequest(request);

        log.info(format("Attempting login for user: %s", loginRequest.getUsername()));

        return getAuthenticationManager().authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword(),
                        Collections.emptyList())
        );
    }

    @Override
    protected void successfulAuthentication(final HttpServletRequest request, final HttpServletResponse response,
                                            final FilterChain filterChain, final Authentication authentication) {
        final var user = ((User) authentication.getPrincipal());

        log.info(format("User %s logged in", user.getUsername()));

        final String token = jwtService.generateToken(user);
        response.addHeader(properties.getResponseTokenHeader(), token);
    }

    private LoginRequest getLoginRequestFromHttpRequest(final HttpServletRequest request) {
        try {
            return new ObjectMapper()
                    .readValue(request.getInputStream(), LoginRequest.class);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
}
