package be.wouterversyck.shoppinglistapi.admin.controllers;

import be.wouterversyck.shoppinglistapi.users.exceptions.UserNotFoundException;
import be.wouterversyck.shoppinglistapi.users.models.RoleEntity;
import be.wouterversyck.shoppinglistapi.users.models.SecureUserView;
import be.wouterversyck.shoppinglistapi.users.models.User;
import be.wouterversyck.shoppinglistapi.users.services.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import javax.mail.MessagingException;
import java.util.Collection;

@Slf4j
@RestController
@PreAuthorize("hasAuthority('ADMIN')")
@RequestMapping("admin")
@AllArgsConstructor
public class AdminController {

    private UserService userService;

    @GetMapping("users")
    public Page<SecureUserView> getUsers(
            @SortDefault(sort = "username", direction = Sort.Direction.ASC) final Pageable page) {
        log.info("requesting user page");
        return userService.getAllUsers(page);
    }

    @PostMapping("users")
    public ResponseEntity<SecureUserView> addUser(@RequestBody final User user) throws UserNotFoundException {
        log.info("add user request for username {}", user.getUsername());
        final var userResult = userService.addUser(user);

        try {
            userService.sendPasswordSetMailForUser(userResult.getId());
            return ResponseEntity.ok(userResult);
        } catch(final MessagingException e) {
            log.info("An error occurred while sending password set email: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.MULTI_STATUS)
                    .body(userResult);
        }
    }

    @GetMapping("users/passwordSet/{id}")
    public void sendPasswordSetMail(@PathVariable final long id)
            throws MessagingException, UserNotFoundException {
        log.info("add user request for userId {}", id);
        userService.sendPasswordSetMailForUser(id);
    }

    @GetMapping("users/exists")
    public boolean doesUserExist(
            @RequestParam(name = "username", required = false) final String username,
            @RequestParam(name = "email", required = false) final String email) {
        log.info("user exists request with params [username: {}, email: {}]", username, email);
        if (username == null && email == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "either email or username need to be provided");
        }
        if (username != null) {
            return userService.userExistsByUsername(username);
        }
        return userService.userExistsByEmail(email);
    }

    @GetMapping("roles")
    public Collection<RoleEntity> getRoles() {
        log.info("get roles request");
        return userService.getRoles();
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public void handleUserNotFoundException(final UserNotFoundException exception) {
        log.info("Exception occurred while fetching user: {}", exception.getMessage());
    }
}
