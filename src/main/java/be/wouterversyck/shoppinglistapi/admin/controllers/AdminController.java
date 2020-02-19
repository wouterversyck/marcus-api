package be.wouterversyck.shoppinglistapi.admin.controllers;

import be.wouterversyck.shoppinglistapi.users.models.SecureUserView;
import be.wouterversyck.shoppinglistapi.users.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@PreAuthorize("hasAuthority('ADMIN')")
@RequestMapping("admin")
public class AdminController {

    private UserService userService;

    public AdminController(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping("users/{page}/{size}")
    public Page<SecureUserView> getUsers(@PathVariable final int page, @PathVariable final int size) {
        return userService.getAllUsers(PageRequest.of(page, size));
    }
}
