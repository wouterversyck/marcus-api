package be.wouterversyck.shoppinglistapi.admin.controllers;

import be.wouterversyck.shoppinglistapi.users.models.RoleEntity;
import be.wouterversyck.shoppinglistapi.users.models.SecureUserView;
import be.wouterversyck.shoppinglistapi.users.models.User;
import be.wouterversyck.shoppinglistapi.users.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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
    public void addUser(@RequestBody final User user) {
        userService.addUser(user);
    }

    @GetMapping("roles")
    public Collection<RoleEntity> getRoles() {
        return userService.getRoles();
    }
}
