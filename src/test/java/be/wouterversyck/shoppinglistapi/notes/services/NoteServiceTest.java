package be.wouterversyck.shoppinglistapi.notes.services;

import be.wouterversyck.shoppinglistapi.notes.exceptions.ShoppingListNotFoundException;
import be.wouterversyck.shoppinglistapi.notes.models.Note;
import be.wouterversyck.shoppinglistapi.notes.persistence.NotesDao;
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
    private static final String SHOPPING_LIST_NAME_A = "SHOPPING_LIST_NAME_A";
    private static final String SHOPPING_LIST_NAME_B = "SHOPPING_LIST_NAME_B";

    @Mock
    private NotesDao notesDao;

    @InjectMocks
    private NoteService noteService;

    @Test
    void shouldReturnShoppingList_WhenUserIsPassed() {
        when(notesDao.findAllByContributors(USERID)).thenReturn(getChecklists());

        List<Note> result = noteService.getAllForUser(USERID);

        assertThat(result.size()).isEqualTo(2);
        assertThat(result).extracting("name")
                .contains(SHOPPING_LIST_NAME_A, SHOPPING_LIST_NAME_B);
    }

    @Test
    void shouldReturnShoppingList_WhenIdIsPassed() throws ShoppingListNotFoundException {
        when(notesDao.findByIdAndContributors("1", USERID))
                .thenReturn(Optional.of(getChecklist()));

        Note result = noteService.getByIdForUser("1", USERID);

        assertThat(result).extracting("name")
                .isEqualTo(SHOPPING_LIST_NAME_A);
    }

    @Test
    void shouldThrowException_WhenShoppingListIsNotFound() {
        when(notesDao.findByIdAndContributors("1", USERID)).thenReturn(Optional.empty());

        assertThrows(ShoppingListNotFoundException.class, () -> noteService.getByIdForUser("1", USERID));
    }

    @Test
    void shouldSetOwnerAndContributor_WhenShoppingListIsSaved() {
        Note note = getChecklist();
        noteService.saveForUser(note, USERID);

        assertThat(note.getContributors()).contains(USERID);
        assertThat(note.getOwner()).isEqualTo(USERID);
    }

    @Test
    void shouldNotAddContributorAgain_WhenShoppingListIsSaved() {
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
                .name(SHOPPING_LIST_NAME_A)
                .build();
    }

    private List<Note> getChecklists() {
        return Arrays.asList(
                Note.builder()
                        .id("1")
                        .name(SHOPPING_LIST_NAME_A)
                        .build(),
                Note.builder()
                        .id("2")
                        .name(SHOPPING_LIST_NAME_B)
                        .build()
        );
    }
}
