package be.wouterversyck.marcusapi.users.controllers;

import be.wouterversyck.marcusapi.security.models.PasswordResetRequest;
import be.wouterversyck.marcusapi.users.exceptions.UserNotFoundException;
import be.wouterversyck.marcusapi.users.services.UserFacade;
import io.jsonwebtoken.security.SignatureException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
    private UserFacade userFacade;


    @PutMapping
    public void resetPassword(@RequestBody final PasswordResetRequest passwordResetRequest)
            throws UserNotFoundException {
        userFacade.resetPassword(passwordResetRequest.getPassword(), passwordResetRequest.getToken());
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
