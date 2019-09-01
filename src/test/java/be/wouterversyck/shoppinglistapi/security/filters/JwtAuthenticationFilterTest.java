package be.wouterversyck.shoppinglistapi.security.filters;

import be.wouterversyck.shoppinglistapi.security.services.JwtService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JwtAuthenticationFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private HttpServletResponse httpServletResponse;

    @Mock
    private FilterChain filterChain;

    HttpServletRequest httpServletRequest;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Before
    public void setup() {
        httpServletRequest = MockMvcRequestBuilders
                .get("login")
                .content("{\"username\": \"user\",\"password\": \"password\"}")
                .buildRequest(new MockServletContext());

        httpServletResponse = new MockHttpServletResponse();
    }

    @Test
    public void shouldReturnCredentials_WhenLoginCredentialsAreProvided() {
        Authentication authLogin = new UsernamePasswordAuthenticationToken("username", "password", Collections.emptyList());
        when(authenticationManager.authenticate(any())).thenReturn(authLogin);

        Authentication authResult = jwtAuthenticationFilter.attemptAuthentication(httpServletRequest, httpServletResponse);

        assertThat(authResult.isAuthenticated()).isTrue();
        assertThat(authResult.getPrincipal()).isEqualTo("username");
        assertThat(authResult.getCredentials()).isEqualTo("password");
    }

    @Test
    public void shouldSetTokenInHeader_WhenAuthenticationWasSuccessful() {
        User user = new User("user", "password", Collections.emptyList());
        var userToken = new UsernamePasswordAuthenticationToken(user, "password", Collections.emptyList());
        when(jwtService.generateToken(user)).thenReturn("token");

        jwtAuthenticationFilter.successfulAuthentication(httpServletRequest, httpServletResponse, filterChain, userToken);

        assertThat(httpServletResponse.getHeader("X-Token")).isEqualTo("token");
        assertThat(httpServletResponse.getStatus()).isEqualTo(200);
    }

}
