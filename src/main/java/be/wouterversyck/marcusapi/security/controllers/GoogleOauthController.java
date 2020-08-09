package be.wouterversyck.marcusapi.security.controllers;

import be.wouterversyck.marcusapi.security.config.SecurityProperties;
import be.wouterversyck.marcusapi.security.models.GoogleOauthRequest;
import be.wouterversyck.marcusapi.security.utils.JwtService;
import be.wouterversyck.marcusapi.users.exceptions.UserNotFoundException;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.security.GeneralSecurityException;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("oauth")
public class GoogleOauthController {
    private GoogleIdTokenVerifier googleIdTokenVerifier;
    private UserDetailsService userService;
    private JwtService jwtService;
    private SecurityProperties securityProperties;

    @PostMapping("google")
    public void signInWithGoogle(
            @RequestBody @Valid final GoogleOauthRequest request,
            final HttpServletResponse response) throws GeneralSecurityException, IOException, UserNotFoundException {
        final var payload = googleIdTokenVerifier.verify(request.getIdToken()).getPayload();
        final var user = userService.loadUserByUsername(payload.getEmail());
        final var authentication = new UsernamePasswordAuthenticationToken(user, null);

        log.info("Authenticated user {} with google oauth, creating token", user.getUsername());
        response.setHeader(securityProperties.getResponseTokenHeader(), jwtService.generateToken(authentication));
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public void handleUserNotFoundException(final UserNotFoundException exception) {
        log.info("User that does not exist tried to login with google oauth: {}", exception.getMessage());
    }

    @ExceptionHandler(GeneralSecurityException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public void handleGeneralSecurityException(final GeneralSecurityException exception) {
        log.info("A security exception was thrown while verifying token with google: {}", exception.getMessage());
    }
}
