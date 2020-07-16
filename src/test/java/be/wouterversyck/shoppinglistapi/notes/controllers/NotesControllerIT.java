package be.wouterversyck.shoppinglistapi.notes.controllers;

import be.wouterversyck.shoppinglistapi.AbstractIT;
import be.wouterversyck.shoppinglistapi.notes.models.ShoppingList;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.assertThat;

class NotesControllerIT extends AbstractIT {

    private static final String SHOPPING_LIST_URL = "/shoppinglist/";

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    void shouldSee404_WhenITryToSeeAnotherUsersNotes() throws Exception {
        String token = login("user", "password");
        var list = ShoppingList.builder()
                .id("3")
                .contributors(Collections.singletonList(2L))
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
                .contributors(Collections.singletonList(2L))
                .name("NOTE_NAME").build();

        mongoTemplate.save(list);

        getMvc()
                .perform(
                        getWithToken(SHOPPING_LIST_URL + 3, token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("NOTE_NAME")));
    }

    @Test
    void shouldNotBeAbleToRemoveAnotherUsersLists() throws Exception {
        String token = login("user", "password");
        var noteBeforeRequest = ShoppingList.builder()
                .id("3")
                .contributors(Collections.singletonList(2L))
                .name("NOTE_NAME").build();

        mongoTemplate.save(noteBeforeRequest);

        getMvc()
                .perform(
                        deleteWithToken(SHOPPING_LIST_URL + 3, token))
                .andExpect(status().isOk());

        var noteAfterRequest = mongoTemplate.findById("3", ShoppingList.class);
        assertThat(noteAfterRequest.getName()).isEqualTo("NOTE_NAME");
    }

    @Test
    void shouldBeAbleToRemoveMyOwnLists() throws Exception {
        String token = login("admin", "secure");
        var noteBeforeRequest = ShoppingList.builder()
                .id("3")
                .contributors(Collections.singletonList(2L))
                .name("NOTE_NAME").build();

        mongoTemplate.save(noteBeforeRequest);

        getMvc()
                .perform(
                        deleteWithToken(SHOPPING_LIST_URL + 3, token))
                .andExpect(status().isOk());

        var noteAfterRequest = mongoTemplate.findById("3", ShoppingList.class);

        assertThat(noteAfterRequest).isNull();
    }
}