package be.wouterversyck.shoppinglistapi.security.filters;

import be.wouterversyck.shoppinglistapi.security.config.SecurityProperties;
import be.wouterversyck.shoppinglistapi.security.models.JwtUserDetails;
import be.wouterversyck.shoppinglistapi.security.utils.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
class JwtAuthenticationFilterTest {

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

    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    void setup() {
        properties = new SecurityProperties();
        properties.setResponseTokenHeader("x-token");
        properties.setAuthLoginUrl("http://login.be");
        jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager,jwtService,
                authenticationFailureHandler, properties);

        httpServletRequest = MockMvcRequestBuilders
                .get("http://login.be")
                .content("{\"username\": \"user\",\"password\": \"password\"}")
                .buildRequest(new MockServletContext());

        httpServletResponse = new MockHttpServletResponse();
    }

    @Test
    void shouldReturnCredentials_WhenLoginCredentialsAreProvided() {
        JwtUserDetails user = new JwtUserDetails(1, "user", "password", Collections.emptyList());
        Authentication authLogin = new UsernamePasswordAuthenticationToken(user, "password", Collections.emptyList());
        when(authenticationManager.authenticate(any())).thenReturn(authLogin);

        Authentication authResult = jwtAuthenticationFilter.attemptAuthentication(httpServletRequest, httpServletResponse);

        assertThat(authResult.isAuthenticated()).isTrue();
        assertThat(authResult.getPrincipal()).isEqualTo(user);
        assertThat(authResult.getCredentials()).isEqualTo("password");
    }

    @Test
    void shouldSetTokenInHeader_WhenAuthenticationWasSuccessful() {
        JwtUserDetails user = new JwtUserDetails(1, "user", "password", Collections.emptyList());
        var userToken = new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
        when(jwtService.generateToken(userToken)).thenReturn("token");

        jwtAuthenticationFilter.successfulAuthentication(httpServletRequest, httpServletResponse, filterChain, userToken);

        assertThat(httpServletResponse.getHeader(properties.getResponseTokenHeader())).isEqualTo("token");
        assertThat(httpServletResponse.getStatus()).isEqualTo(200);
    }

}
