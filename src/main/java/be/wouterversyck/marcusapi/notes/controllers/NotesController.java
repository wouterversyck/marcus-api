package be.wouterversyck.marcusapi.notes.controllers;

import be.wouterversyck.marcusapi.notes.models.Note;
import be.wouterversyck.marcusapi.security.models.JwtUserPrincipal;
import be.wouterversyck.marcusapi.notes.exceptions.NoteNotFoundException;
import be.wouterversyck.marcusapi.notes.services.NoteService;
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
@RequestMapping("notes")
public class NotesController {
    private final NoteService noteService;

    public NotesController(final NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping
    public List<Note> getNotes(final JwtUserPrincipal principal) {
        return noteService.getAllForUser(principal.getId());
    }

    @GetMapping("{id}")
    public Note getNote(
            @PathVariable final String id,
            final JwtUserPrincipal principal) throws NoteNotFoundException {
        return noteService.getByIdForUser(id, principal.getId());
    }

    @PostMapping
    public Note saveNote(
            @RequestBody final Note note,
            final JwtUserPrincipal principal) {
        return this.noteService.saveForUser(note, principal.getId());
    }

    @DeleteMapping("{id}")
    public void deleteNote(@PathVariable("id") final String id,
                           final JwtUserPrincipal principal) {
        this.noteService.deleteForUser(id, principal.getId());
    }

    @ExceptionHandler(NoteNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public void handleNoteNotFound(final NoteNotFoundException exception) {
        log.info("Exception occurred while fetching note: {}", exception.getMessage());
    }

}
