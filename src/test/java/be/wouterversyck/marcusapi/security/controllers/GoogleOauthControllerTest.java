package be.wouterversyck.marcusapi.security.controllers;

import be.wouterversyck.marcusapi.security.config.SecurityProperties;
import be.wouterversyck.marcusapi.security.models.GoogleOauthRequest;
import be.wouterversyck.marcusapi.security.models.JwtUserDetails;
import be.wouterversyck.marcusapi.users.services.SecurityUserService;
import be.wouterversyck.marcusapi.security.utils.JwtService;
import be.wouterversyck.marcusapi.users.exceptions.UserNotFoundException;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class GoogleOauthControllerTest {

    private static final String ID_TOKEN = "ID_TOKEN";
    private static final String JWT_TOKEN = "JWT_TOKEN";
    private static final String EMAIL = "test@test.be";
    private static final long ID = 1L;
    private static final String USERNAME = "USERNAME";
    private static final String ROLE = "ROLE";
    private static final String RESPONSE_HEADER = "RESPONSE_HEADER";

    @Mock
    private GoogleIdTokenVerifier googleIdTokenVerifier;
    @Mock
    private SecurityUserService userService;
    @Mock
    private JwtService jwtService;
    @Mock
    GoogleIdToken googleIdToken;
    private SecurityProperties securityProperties;
    private HttpServletResponse response = new MockHttpServletResponse();

    private GoogleOauthController googleOauthController;

    @BeforeEach
    public void setup() {
        securityProperties = new SecurityProperties();
        securityProperties.setResponseTokenHeader(RESPONSE_HEADER);

        googleOauthController = new GoogleOauthController(googleIdTokenVerifier, userService, jwtService, securityProperties);
    }

    @Test
    public void shouldSetHeader_WhenTokenIsVerifiedByGoogle() throws UserNotFoundException, GeneralSecurityException, IOException {
        var request = new GoogleOauthRequest();
        request.setIdToken(ID_TOKEN);
        var userDetails = createUserDetails();

        when(googleIdToken.getPayload()).thenReturn(createGoogleIdToken(EMAIL));
        when(googleIdTokenVerifier.verify(ID_TOKEN)).thenReturn(googleIdToken);
        when(userService.loadUserByUsername(EMAIL)).thenReturn(userDetails);
        when(jwtService.generateToken(createAuthentication(userDetails))).thenReturn(JWT_TOKEN);

        googleOauthController.signInWithGoogle(request, response);

        assertThat(response.getHeader(RESPONSE_HEADER)).isEqualTo(JWT_TOKEN);
    }

    private GoogleIdToken.Payload createGoogleIdToken(final String email) {
        GoogleIdToken.Payload payload = new GoogleIdToken.Payload();
        payload.setEmail(email);

        return payload;
    }

    private UserDetails createUserDetails() {
        return new JwtUserDetails(
                ID,
                USERNAME,
                "",
                Collections.singletonList(new SimpleGrantedAuthority(ROLE)));
    }

    private Authentication createAuthentication(UserDetails userDetails) {
        return new UsernamePasswordAuthenticationToken(userDetails, null);
    }
}