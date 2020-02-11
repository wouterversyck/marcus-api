package be.wouterversyck.shoppinglistapi.debug.filters;

import be.wouterversyck.shoppinglistapi.debug.config.MDCProperties;
import be.wouterversyck.shoppinglistapi.security.config.SecurityProperties;
import be.wouterversyck.shoppinglistapi.security.utils.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import javax.servlet.ServletException;
import java.io.IOException;

import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class MDCFilterTest {

    private static final String CORRELATION_ID_HEADER_KEY = "CORRELATION_ID_HEADER_KEY";
    private static final String CORRELATION_ID_MDC_KEY = "CORRELATION_ID_MDC_KEY";
    private static final String USER_MDC_KEY = "USER_MDC_KEY";
    private static final String TOKEN_HEADER_KEY = "TOKEN_HEADER_KEY";
    private static final String TOKEN = "TOKEN";
    private static final String USERNAME = "USERNAME";

    @Mock
    private JwtService jwtService;

    private MDCFilter mdcFilter;

    @BeforeEach
    void setup() {
        when(jwtService.parseToken(TOKEN)).thenReturn(new UsernamePasswordAuthenticationToken(USERNAME, null, null));

        var securityProperties = new SecurityProperties();
        securityProperties.setTokenHeader(TOKEN_HEADER_KEY);

        var mdcProperties = new MDCProperties();
        mdcProperties.setCorrelationIdHeaderKey(CORRELATION_ID_HEADER_KEY);
        mdcProperties.setUserMdcKey(USER_MDC_KEY);
        mdcProperties.setCorrelationIdMdcKey(CORRELATION_ID_MDC_KEY);

        mdcFilter = new MDCFilter(jwtService, securityProperties, mdcProperties);
    }


    @Test
    void shouldAddCorrelationIdToHeader() throws ServletException, IOException {
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(TOKEN_HEADER_KEY, TOKEN);

        mdcFilter.doFilterInternal(request, response, new MockFilterChain());

        assertThat(response.getHeader(CORRELATION_ID_HEADER_KEY))
                .isNotBlank()
                .hasSize(36);
    }
}
