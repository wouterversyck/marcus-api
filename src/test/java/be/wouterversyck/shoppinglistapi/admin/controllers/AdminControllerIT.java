package be.wouterversyck.shoppinglistapi.admin.controllers;

import be.wouterversyck.shoppinglistapi.AbstractIT;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminControllerIT extends AbstractIT {

    @Test
    void shouldDenyAccess_WhenNotLoggedInAsAdmin() throws Exception {
        String token = login("user", "password");

        getMvc()
                .perform(
                    getWithToken("/admin/users/0/1", token))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldAllowAccess_WhenLoggedInAsAdmin() throws Exception {
        String token = login("admin", "secure");

        getMvc()
                .perform(
                        getWithToken("/admin/users/0/4", token))
                .andExpect(status().isOk());
    }

    @Test
    void shouldNotShowUserPassword_WhenUsersEndpointIsQueried() throws Exception {
        String token = login("admin", "secure");

        getMvc()
                .perform(
                        getWithToken("/admin/users/0/4", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].username", is("user")))
                .andExpect(jsonPath("$.content[0].role", is("USER")))
                .andExpect(jsonPath("$.content[0].password").doesNotExist());
    }
}
