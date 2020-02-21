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
                    getWithToken("/admin/users", token))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldAllowAccess_WhenLoggedInAsAdmin() throws Exception {
        String token = login("admin", "secure");

        getMvc()
                .perform(
                        getWithToken("/admin/users", token))
                .andExpect(status().isOk());
    }

    @Test
    void shouldNotShowUserPasswordAndIsSortedAccordingToUsername_WhenUsersEndpointIsQueried() throws Exception {
        String token = login("admin", "secure");

        getMvc()
                .perform(
                        getWithToken("/admin/users", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()", is(2)))
                .andExpect(jsonPath("$.content[0].username", is("admin")))
                .andExpect(jsonPath("$.content[0].role", is("ADMIN")))
                .andExpect(jsonPath("$.content[0].password").doesNotExist())
                .andExpect(jsonPath("$.content[1].username", is("user")))
                .andExpect(jsonPath("$.content[1].role", is("USER")))
                .andExpect(jsonPath("$.content[1].password").doesNotExist());
    }

    @Test
    void shouldReturnSingleUser_WhenOnePageWithSizeOneIsRequested() throws Exception {
        String token = login("admin", "secure");

        getMvc()
                .perform(
                        getWithToken("/admin/users?page=0&size=1", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()", is(1)))
                .andExpect(jsonPath("$.content[0].username", is("admin")))
                .andExpect(jsonPath("$.content[0].role", is("ADMIN")));
    }

    @Test
    void shouldReturnUsersSortedById_WhenRequestIsMadeWithSortParam() throws Exception {
        String token = login("admin", "secure");

        getMvc()
                .perform(
                        getWithToken("/admin/users?sort=id,asc", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()", is(2)))
                .andExpect(jsonPath("$.content[0].username", is("user")))
                .andExpect(jsonPath("$.content[1].username", is("admin")));
    }

    @Test
    void shouldReturnUsersSortedByNameDesc_WhenRequestIsMadeWithSortParam() throws Exception {
        String token = login("admin", "secure");

        getMvc()
                .perform(
                        getWithToken("/admin/users?sort=username,desc", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()", is(2)))
                .andExpect(jsonPath("$.content[0].username", is("user")))
                .andExpect(jsonPath("$.content[1].username", is("admin")));
    }
}
