package be.wouterversyck.shoppinglistapi.security.filters;

import be.wouterversyck.shoppinglistapi.security.config.SecurityProperties;
import be.wouterversyck.shoppinglistapi.security.utils.JwtService;
import io.jsonwebtoken.JwtException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JwtAuthenticationFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private FilterChain filterChain;

    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private HttpServletResponse httpServletResponse;
    private HttpServletRequest httpServletRequest;

    @Before
    public void setup() {
        SecurityProperties properties;
        properties = new SecurityProperties();
        properties.setTokenHeader("Authorization");

        jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtService, properties);

        httpServletRequest = MockMvcRequestBuilders
                .get("test")
                .header("Authorization", "token")
                .buildRequest(new MockServletContext());

        httpServletResponse = new MockHttpServletResponse();

        SecurityContextHolder.clearContext();
    }

    @Test
    public void shouldAuthorize_WhenValidTokenIsGiven() throws IOException, ServletException {
        when(jwtService.parseToken("token")).thenReturn(
                new UsernamePasswordAuthenticationToken("test", null, Collections.emptyList())
        );

        jwtAuthenticationFilter.doFilterInternal(httpServletRequest, httpServletResponse, filterChain);

        verify(filterChain).doFilter(httpServletRequest, httpServletResponse);
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        assertThat(authentication.getPrincipal()).isEqualTo("test");
        assertThat(authentication.isAuthenticated()).isTrue();
    }

    @Test
    public void shouldNotAuthorize_WhenInvalidTokenIsGiven() throws IOException, ServletException {
        when(jwtService.parseToken("token")).thenThrow(new JwtException("test"));

        jwtAuthenticationFilter.doFilterInternal(httpServletRequest, httpServletResponse, filterChain);

        verify(filterChain).doFilter(httpServletRequest, httpServletResponse);
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        assertThat(authentication).isNull();
    }
}
