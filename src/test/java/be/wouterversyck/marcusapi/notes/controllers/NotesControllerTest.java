package be.wouterversyck.marcusapi.notes.controllers;

import be.wouterversyck.marcusapi.notes.exceptions.NoteNotFoundException;
import be.wouterversyck.marcusapi.notes.models.Note;
import be.wouterversyck.marcusapi.security.models.JwtUserPrincipal;
import be.wouterversyck.marcusapi.notes.services.NoteService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotesControllerTest {

    @Mock
    private NoteService noteService;

    @InjectMocks
    private NotesController notesController;

    private static final String USERNAME = "USERNAME";
    private static final long USER_ID = 1;
    private static final String NOTE_ID = "1";

    @Test
    void shouldReturnNote_WhenProperPrincipalAndNoteIdIsProvided() {

        when(noteService.getAllForUser(USER_ID)).thenReturn(getNotes());

        List<Note> items = notesController.getNotes(createPrincipal());

        assertThat(items).hasSize(2);
        assertThat(items).extracting("name")
                .contains("test", "test2");
        assertThat(items).extracting("id")
                .contains("1", "2");
    }

    @Test
    void shouldReturnSingleNote_WhenProperPrincipalAndNoteIdIsProvided() throws NoteNotFoundException {
        when(noteService.getByIdForUser(NOTE_ID, USER_ID)).thenReturn(
                Note.builder()
                        .id("1")
                        .name("test")
                        .build());

        notesController.getNote(NOTE_ID, createPrincipal());
    }

    @Test
    void shouldSaveNote_WhenProperPrincipalAndNoteIdIsProvided() throws NoteNotFoundException {
        var note = Note.builder()
                .id("1")
                .name("test")
                .build();

        notesController.saveNote(note, createPrincipal());

        verify(noteService).saveForUser(note, USER_ID);
    }

    @Test
    void shouldDeleteLists_WhenProperPrincipalAndNoteIdIsProvided() {
        notesController.deleteNote(NOTE_ID, createPrincipal());

        verify(noteService).deleteForUser(NOTE_ID, USER_ID);
    }

    private List<Note> getNotes() {
        return Arrays.asList(
                Note.builder()
                        .id("1")
                        .name("test")
                        .build(),
                Note.builder()
                        .id("2")
                        .name("test2")
                        .build()
        );
    }

    private JwtUserPrincipal createPrincipal() {
        return new JwtUserPrincipal(USER_ID, USERNAME, null, true);
    }
}
