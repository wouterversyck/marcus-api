package be.wouterversyck.shoppinglistapi.security.filters;

import be.wouterversyck.shoppinglistapi.security.config.SecurityProperties;
import be.wouterversyck.shoppinglistapi.security.utils.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtLoginFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private HttpServletResponse httpServletResponse;

    @Mock
    private AuthenticationFailureHandler authenticationFailureHandler;

    @Mock
    private FilterChain filterChain;

    private HttpServletRequest httpServletRequest;
    private SecurityProperties properties;

    private JwtLoginFilter jwtLoginFilter;

    @BeforeEach
    void setup() {
        properties = new SecurityProperties();
        properties.setResponseTokenHeader("x-token");
        properties.setAuthLoginUrl("login");
        jwtLoginFilter = new JwtLoginFilter(authenticationManager,jwtService,
                authenticationFailureHandler, properties);

        httpServletRequest = MockMvcRequestBuilders
                .get("login")
                .content("{\"username\": \"user\",\"password\": \"password\"}")
                .buildRequest(new MockServletContext());

        httpServletResponse = new MockHttpServletResponse();
    }

    @Test
    void shouldReturnCredentials_WhenLoginCredentialsAreProvided() {
        Authentication authLogin = new UsernamePasswordAuthenticationToken("username", "password", Collections.emptyList());
        when(authenticationManager.authenticate(any())).thenReturn(authLogin);

        Authentication authResult = jwtLoginFilter.attemptAuthentication(httpServletRequest, httpServletResponse);

        assertThat(authResult.isAuthenticated()).isTrue();
        assertThat(authResult.getPrincipal()).isEqualTo("username");
        assertThat(authResult.getCredentials()).isEqualTo("password");
    }

    @Test
    void shouldSetTokenInHeader_WhenAuthenticationWasSuccessful() {
        User user = new User("user", "password", Collections.emptyList());
        var userToken = new UsernamePasswordAuthenticationToken(user, "password", Collections.emptyList());
        when(jwtService.generateToken(user)).thenReturn("token");

        jwtLoginFilter.successfulAuthentication(httpServletRequest, httpServletResponse, filterChain, userToken);

        assertThat(httpServletResponse.getHeader(properties.getResponseTokenHeader())).isEqualTo("token");
        assertThat(httpServletResponse.getStatus()).isEqualTo(200);
    }

}
