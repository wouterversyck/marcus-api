package be.wouterversyck.shoppinglistapi.notes.controllers;

import be.wouterversyck.shoppinglistapi.AbstractIT;
import org.junit.jupiter.api.Test;

import static java.lang.String.format;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class NotesControllerIT extends AbstractIT {

    private static final String SHOPPING_LIST_URL = "/shoppinglist/";

    @Test
    void shouldSee404_WhenITryToSeeAnotherUsersNotes() throws Exception {
        String token = login("user", "password");

        // 3 is owned by admin, see liquibase seed data
        getMvc()
                .perform(
                        getWithToken(format("%s%s", SHOPPING_LIST_URL, 3), token))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldSeeMyList_WhenImLoggedIn() throws Exception {
        String token = login("admin", "secure");

        // 3 is owned by admin, see liquibase seed data
        getMvc()
                .perform(
                        getWithToken(format("%s%s", SHOPPING_LIST_URL, 3), token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("List of other user")));
    }
}