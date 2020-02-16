package be.wouterversyck.shoppinglistapi.security.filters;

import be.wouterversyck.shoppinglistapi.AbstractIT;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class JwtLoginFilterIT extends AbstractIT {

    @Test
    void shouldGrantToken_WhenCorrectCredentialsAreGiven() throws Exception {
        getMvc()
                .perform(
                        post("/login")
                                .content("{\"username\": \"user\",\"password\": \"password\"}")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(header().exists("X-Token"));
    }

    @Test
    void shouldDenyAccess_WhenInvalidCredentialsAreGiven() throws Exception {
        getMvc()
                .perform(
                        post("/login")
                                .content("{\"username\": \"wrong\",\"password\": \"wrong\"}")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(header().doesNotExist("X-Token"));
    }

    @Test
    void shouldAllowAccess_WhenNotLoggedIn_ToPublicEndPoint() throws Exception {
        getMvc()
                .perform(get("/public/version"))
                .andExpect(status().isOk())
                .andExpect(header().doesNotExist("X-Token"));
    }

}
