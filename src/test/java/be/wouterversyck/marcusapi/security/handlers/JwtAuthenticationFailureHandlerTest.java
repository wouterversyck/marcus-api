package be.wouterversyck.marcusapi.security.handlers;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;

import static org.assertj.core.api.Assertions.assertThat;

class JwtAuthenticationFailureHandlerTest {

    private JwtAuthenticationFailureHandler handler = new JwtAuthenticationFailureHandler();
    private MockHttpServletResponse response = new MockHttpServletResponse();
    private MockHttpServletRequest request = new MockHttpServletRequest();
    private AuthenticationException exception = new BadCredentialsException("no");

    @Test
    void test() {
        handler.onAuthenticationFailure(request, response, exception);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
