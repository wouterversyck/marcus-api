package be.wouterversyck.marcusapi;

import lombok.Getter;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static java.lang.String.format;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class AbstractIT {

    @Getter
    @Autowired
    private MockMvc mvc;

    protected String login(String username, String password) throws Exception {
        return mvc.perform(
                post("/login")
                        .content(format("{\"username\": \"%s\",\"password\": \"%s\"}", username, password))
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getHeader("x-token");
    }

    protected MockHttpServletRequestBuilder getWithToken(String url, String token) {
        return get(url)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", format("Bearer %s", token));
    }

    protected MockHttpServletRequestBuilder deleteWithToken(String url, String token) {
        return delete(url)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", format("Bearer %s", token));
    }
}
