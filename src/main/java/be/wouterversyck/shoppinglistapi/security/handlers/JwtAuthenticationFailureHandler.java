package be.wouterversyck.shoppinglistapi.security.handlers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static java.lang.String.format;

@Slf4j
public class JwtAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(final HttpServletRequest httpServletRequest,
                                        final HttpServletResponse httpServletResponse,
                                        final AuthenticationException e) {
        log.info(format("Authentication request failed: %s", e.toString()), e);
        httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
    }
};