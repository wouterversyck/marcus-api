package be.wouterversyck.marcusapi.notes.exceptions;

import static java.lang.String.format;

public class NoteNotFoundException extends Exception {
    public NoteNotFoundException(final String id) {
        super(format("Note with id %s was not found", id));
    }
}
