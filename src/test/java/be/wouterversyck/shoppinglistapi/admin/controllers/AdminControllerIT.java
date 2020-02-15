package be.wouterversyck.shoppinglistapi.admin.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static java.lang.String.format;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class AdminControllerIT {

    @Autowired
    private MockMvc mvc;

    @Test
    public void shouldDenyAccess_WhenNotLoggedInAsAdmin() throws Exception {
        String token = login("user", "password");

        mvc.perform(
                get("/admin/users/0/1")
                .header("Authorization", format("Bearer %s", token)))
                .andExpect(status().isForbidden());
    }

    @Test
    public void shouldAllowAccess_WhenLoggedInAsAdmin() throws Exception {
        String token = login("admin", "secure");

        mvc.perform(
                get("/admin/users/0/4")
                        .header("Authorization", format("Bearer %s", token)))
                .andExpect(status().isOk());
    }

    private String login(String username, String password) throws Exception {
        return mvc.perform(
                post("/login")
                        .content(format("{\"username\": \"%s\",\"password\": \"%s\"}", username, password))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getHeader("x-token");
    }
}