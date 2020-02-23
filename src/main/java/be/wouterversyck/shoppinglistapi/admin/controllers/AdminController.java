package be.wouterversyck.shoppinglistapi.admin.controllers;

import be.wouterversyck.shoppinglistapi.users.exceptions.UserNotFoundException;
import be.wouterversyck.shoppinglistapi.users.models.RoleEntity;
import be.wouterversyck.shoppinglistapi.users.models.SecureUserView;
import be.wouterversyck.shoppinglistapi.users.models.User;
import be.wouterversyck.shoppinglistapi.users.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
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

import javax.mail.MessagingException;
import java.util.Collection;

@Slf4j
@RestController
@PreAuthorize("hasAuthority('ADMIN')")
@RequestMapping("admin")
public class AdminController {

    private UserService userService;

    public AdminController(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping("users")
    public Page<SecureUserView> getUsers(
            @SortDefault(sort = "username", direction = Sort.Direction.ASC) final Pageable page) {
        return userService.getAllUsers(page);
    }

    @PostMapping("users")
    public void addUser(@RequestBody final User user) throws MessagingException {
        userService.addUser(user);
    }

    @GetMapping("users/passwordSet/{id}")
    public void sendPasswordSetMail(@PathVariable final long id)
            throws MessagingException, UserNotFoundException {
        userService.sendPasswordSetMailForUser(id);
    }

    @GetMapping("users/exists")
    public boolean doesUsernameExist(
            @RequestParam(name = "username", required = false) final String username,
            @RequestParam(name = "email", required = false) final String email) {
        if (username != null) {
            return userService.userExistsByUsername(username);
        }
        return userService.userExistsByEmail(email);
    }

    @GetMapping("roles")
    public Collection<RoleEntity> getRoles() {
        return userService.getRoles();
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public void handleUserNotFoundException(final UserNotFoundException exception) {
        log.info("Exception occurred while fetching user: {}", exception.getMessage());
    }

    @ExceptionHandler(MessagingException.class)
    @ResponseStatus(value = HttpStatus.MULTI_STATUS)
    public void handleEmailException(final MessagingException exception) {
        log.info("An error occurred while sending password set email: {}", exception.getMessage());
    }
}
