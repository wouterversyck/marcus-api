package be.wouterversyck.shoppinglistapi.notes.controllers;

import be.wouterversyck.shoppinglistapi.AbstractIT;
import be.wouterversyck.shoppinglistapi.notes.models.ShoppingList;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class NotesControllerIT extends AbstractIT {

    private static final String SHOPPING_LIST_URL = "/shoppinglist/";

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    void shouldSee404_WhenITryToSeeAnotherUsersNotes() throws Exception {
        String token = login("user", "password");
        var list = ShoppingList.builder()
                .id("3")
                .owner(2L)
                .name("NOTE_NAME").build();
        mongoTemplate.save(list);

        getMvc()
                .perform(
                        getWithToken(SHOPPING_LIST_URL + 3, token))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldSeeMyList_WhenImLoggedIn() throws Exception {
        String token = login("admin", "secure");

        var list = ShoppingList.builder()
                .id("3")
                .owner(2L)
                .name("NOTE_NAME").build();
        mongoTemplate.save(list);

        getMvc()
                .perform(
                        getWithToken(SHOPPING_LIST_URL + 3, token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("NOTE_NAME")));
    }
}