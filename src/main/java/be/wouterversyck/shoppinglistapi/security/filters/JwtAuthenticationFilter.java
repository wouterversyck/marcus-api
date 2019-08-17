package be.wouterversyck.shoppinglistapi.security.filters;

import be.wouterversyck.shoppinglistapi.security.models.LoginRequest;
import be.wouterversyck.shoppinglistapi.security.utils.SecurityConstants;
import be.wouterversyck.shoppinglistapi.security.services.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private JwtService jwtService;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtService jwtService) {
        setFilterProcessesUrl(SecurityConstants.AUTH_LOGIN_URL);
        setAuthenticationManager(authenticationManager);
        setPostOnly(true);

        this.jwtService = jwtService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        LoginRequest loginRequest = getLoginRequestFromHttpRequest(request);

        return getAuthenticationManager().authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword(),
                        Collections.emptyList())
        );
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain filterChain, Authentication authentication) {
        var user = ((User) authentication.getPrincipal());
        String token = jwtService.generateToken(user);
        response.addHeader(SecurityConstants.RESPONSE_TOKEN_HEADER, token);
    }

    private LoginRequest getLoginRequestFromHttpRequest(HttpServletRequest request) {
        LoginRequest loginRequest = null;
        try {
            loginRequest = new ObjectMapper()
                    .readValue(request.getInputStream(), LoginRequest.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return loginRequest;
    }
}
