package be.wouterversyck.shoppinglistapi.notes.controllers;

import be.wouterversyck.shoppinglistapi.notes.models.Note;
import be.wouterversyck.shoppinglistapi.security.models.JwtUserPrincipal;
import be.wouterversyck.shoppinglistapi.notes.exceptions.ShoppingListNotFoundException;
import be.wouterversyck.shoppinglistapi.notes.services.NoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("shoppinglist")
public class NotesController {
    private final NoteService noteService;

    public NotesController(final NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping
    public List<Note> getShoppingLists(final JwtUserPrincipal principal) {
        return noteService.getAllForUser(principal.getId());
    }

    @GetMapping("{id}")
    public Note getShoppingList(
            @PathVariable final String id,
            final JwtUserPrincipal principal) throws ShoppingListNotFoundException {

        return noteService.getByIdForUser(id, principal.getId());
    }

    @PostMapping
    public Note saveShoppingList(
            @RequestBody final Note request,
            final JwtUserPrincipal principal) {
        return this.noteService.saveForUser(request, principal.getId());
    }

    @DeleteMapping("{id}")
    public void deleteNote(@PathVariable("id") final String id,
                           final JwtUserPrincipal principal) {
        this.noteService.deleteForUser(id, principal.getId());
    }

    @ExceptionHandler(ShoppingListNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public void handleShoppingListNotFound(final ShoppingListNotFoundException exception) {
        log.info("Exception occurred while fetching shopping list: {}", exception.getMessage());
    }

}
