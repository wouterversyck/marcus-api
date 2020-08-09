package be.wouterversyck.marcusapi.notes.controllers;

import be.wouterversyck.marcusapi.AbstractIT;
import be.wouterversyck.marcusapi.notes.models.Note;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.assertThat;

class NotesControllerIT extends AbstractIT {

    private static final String NOTES_URL = "/notes/";

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    void shouldSee404_WhenITryToSeeAnotherUsersNotes() throws Exception {
        String token = login("user", "password");
        var list = Note.builder()
                .id("3")
                .contributors(Collections.singletonList(2L))
                .name("NOTE_NAME").build();

        mongoTemplate.save(list);

        getMvc()
                .perform(
                        getWithToken(NOTES_URL + 3, token))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldSeeMyList_WhenImLoggedIn() throws Exception {
        String token = login("admin", "secure");

        var list = Note.builder()
                .id("3")
                .contributors(Collections.singletonList(2L))
                .name("NOTE_NAME").build();

        mongoTemplate.save(list);

        getMvc()
                .perform(
                        getWithToken(NOTES_URL + 3, token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("NOTE_NAME")));
    }

    @Test
    void shouldNotBeAbleToRemoveAnotherUsersLists() throws Exception {
        String token = login("user", "password");
        var noteBeforeRequest = Note.builder()
                .id("3")
                .contributors(Collections.singletonList(2L))
                .name("NOTE_NAME").build();

        mongoTemplate.save(noteBeforeRequest);

        getMvc()
                .perform(
                        deleteWithToken(NOTES_URL + 3, token))
                .andExpect(status().isOk());

        var noteAfterRequest = mongoTemplate.findById("3", Note.class);
        assertThat(noteAfterRequest.getName()).isEqualTo("NOTE_NAME");
    }

    @Test
    void shouldBeAbleToRemoveMyOwnLists() throws Exception {
        String token = login("admin", "secure");
        var noteBeforeRequest = Note.builder()
                .id("3")
                .contributors(Collections.singletonList(2L))
                .name("NOTE_NAME").build();

        mongoTemplate.save(noteBeforeRequest);

        getMvc()
                .perform(
                        deleteWithToken(NOTES_URL + 3, token))
                .andExpect(status().isOk());

        var noteAfterRequest = mongoTemplate.findById("3", Note.class);

        assertThat(noteAfterRequest).isNull();
    }
}