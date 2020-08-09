package be.wouterversyck.marcusapi.notes.services;

import be.wouterversyck.marcusapi.notes.exceptions.NoteNotFoundException;
import be.wouterversyck.marcusapi.notes.models.Note;
import be.wouterversyck.marcusapi.notes.persistence.NotesDao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NoteServiceTest {

    private static final long USERID = 1;
    private static final String NOTE_NAME_A = "NOTE_NAME_A";
    private static final String NOTE_NAME_B = "NOTE_NAME_B";

    @Mock
    private NotesDao notesDao;

    @InjectMocks
    private NoteService noteService;

    @Test
    void shouldReturnNote_WhenUserIsPassed() {
        when(notesDao.findAllByContributors(USERID)).thenReturn(getChecklists());

        List<Note> result = noteService.getAllForUser(USERID);

        assertThat(result.size()).isEqualTo(2);
        assertThat(result).extracting("name")
                .contains(NOTE_NAME_A, NOTE_NAME_B);
    }

    @Test
    void shouldReturnNote_WhenIdIsPassed() throws NoteNotFoundException {
        when(notesDao.findByIdAndContributors("1", USERID))
                .thenReturn(Optional.of(getChecklist()));

        Note result = noteService.getByIdForUser("1", USERID);

        assertThat(result).extracting("name")
                .isEqualTo(NOTE_NAME_A);
    }

    @Test
    void shouldThrowException_WhenNoteIsNotFound() {
        when(notesDao.findByIdAndContributors("1", USERID)).thenReturn(Optional.empty());

        assertThrows(NoteNotFoundException.class, () -> noteService.getByIdForUser("1", USERID));
    }

    @Test
    void shouldSetOwnerAndContributor_WhenNoteIsSaved() {
        Note note = getChecklist();
        noteService.saveForUser(note, USERID);

        assertThat(note.getContributors()).contains(USERID);
        assertThat(note.getOwner()).isEqualTo(USERID);
    }

    @Test
    void shouldNotAddContributorAgain_WhenNoteIsSaved() {
        Note note = getChecklist();
        note.setContributors(Collections.singletonList(USERID));

        noteService.saveForUser(note, USERID);

        assertThat(note.getContributors()).contains(USERID);
        assertThat(note.getContributors()).hasSize(1);
        assertThat(note.getOwner()).isEqualTo(USERID);
    }

    private Note getChecklist() {
        return Note.builder()
                .id("1")
                .name(NOTE_NAME_A)
                .build();
    }

    private List<Note> getChecklists() {
        return Arrays.asList(
                Note.builder()
                        .id("1")
                        .name(NOTE_NAME_A)
                        .build(),
                Note.builder()
                        .id("2")
                        .name(NOTE_NAME_B)
                        .build()
        );
    }
}
