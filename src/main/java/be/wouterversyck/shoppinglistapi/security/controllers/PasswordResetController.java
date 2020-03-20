package be.wouterversyck.shoppinglistapi.security.controllers;

import be.wouterversyck.shoppinglistapi.security.models.PasswordResetRequest;
import be.wouterversyck.shoppinglistapi.security.utils.JwtService;
import be.wouterversyck.shoppinglistapi.users.exceptions.UserNotFoundException;
import be.wouterversyck.shoppinglistapi.users.services.UserService;
import io.jsonwebtoken.security.SignatureException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("pwd")
@Slf4j
public class PasswordResetController {
    private UserService userService;
    private JwtService jwtService;
    private PasswordEncoder passwordEncoder;

    @PutMapping
    public void resetPassword(@RequestBody final PasswordResetRequest passwordResetRequest)
            throws UserNotFoundException {

        final String username = jwtService.getUsernameWithoutValidationSignature(passwordResetRequest.getToken());

        log.info("Resetting pwd for user {}", username);

        final var user = userService.getUserModelByUsername(username);
        jwtService.validatePasswordResetToken(passwordResetRequest.getToken(), user.getPassword());

        user.setPassword(passwordEncoder.encode(passwordResetRequest.getPassword()));
        userService.updateUser(user);
    }

    @ExceptionHandler(SignatureException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private void handleSignatureException() {
        log.info("Pwd reset token is already used");
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private void handleUserNotFoundException(final UserNotFoundException exception) {
        log.info("Pwd reset failed, user is not found in db. Message: {}", exception.getMessage());
    }
}
