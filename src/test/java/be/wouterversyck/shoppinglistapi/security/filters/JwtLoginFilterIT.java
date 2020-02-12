package be.wouterversyck.shoppinglistapi.security.filters;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class JwtLoginFilterIT {

    @Autowired
    private MockMvc mvc;

    @Test
    void shouldGrantToken_WhenCorrectCredentialsAreGiven() throws Exception {
        mvc.perform(
                post("/login")
                        .content("{\"username\": \"user\",\"password\": \"password\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(header().exists("X-Token"));
    }

    @Test
    void shouldDenyAccess_WhenInvalidCredentialsAreGiven() throws Exception {
        mvc.perform(
                post("/login")
                        .content("{\"username\": \"wrong\",\"password\": \"wrong\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(header().doesNotExist("X-Token"));
    }

    @Test
    void shouldAllowAccess_WhenNotLoggedIn_ToPublicEndPoint() throws Exception {
        mvc.perform(get("/public/version"))
                .andExpect(status().isOk())
                .andExpect(header().doesNotExist("X-Token"));
    }

}
