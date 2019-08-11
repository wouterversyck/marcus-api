package be.wouterversyck.shoppinglistapi.security.filters;

import be.wouterversyck.shoppinglistapi.security.utils.SecurityConstants;
import be.wouterversyck.shoppinglistapi.security.services.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private JwtService jwtService;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtService jwtService) {
        setFilterProcessesUrl(SecurityConstants.AUTH_LOGIN_URL);
        setAuthenticationManager(authenticationManager);
        setPostOnly(true);

        this.jwtService = jwtService;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain filterChain, Authentication authentication) {
        var user = ((User) authentication.getPrincipal());
        String token = jwtService.generateToken(user);
        response.addHeader(SecurityConstants.RESPONSE_TOKEN_HEADER, token);
    }
}
