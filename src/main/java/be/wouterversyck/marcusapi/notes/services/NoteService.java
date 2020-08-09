package be.wouterversyck.marcusapi.notes.services;

import be.wouterversyck.marcusapi.notes.exceptions.NoteNotFoundException;
import be.wouterversyck.marcusapi.notes.models.Note;
import be.wouterversyck.marcusapi.notes.persistence.NotesDao;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Slf4j
@Service
public class NoteService {

    private final NotesDao notesDao;

    public List<Note> getAllForUser(final long userId) {
        log.info("Fetching note lists of user {}", userId);
        return notesDao.findAllByContributors(userId);
    }

    public Note getByIdForUser(final String id, final long userId) throws NoteNotFoundException {
        log.info("Fetching note lists with id {} for user {}", id, userId);
        return notesDao.findByIdAndContributors(id, userId).orElseThrow(
                () -> new NoteNotFoundException(id)
        );
    }

    public void deleteForUser(final String id, final long userId) {
        log.info("Deleting note with id {} for user {}", id, userId);
        notesDao.deleteByIdAndContributors(id, userId);
    }

    public void deleteAllByOwner(final long userId) {
        log.info("Deleting all notes for user {}", userId);
        notesDao.deleteAllByOwner(userId);
    }

    public Note saveForUser(
            final Note note, final long userId) {
        log.info("Saving note with id {}", note.getId());

        note.setOwner(userId);
        List<Long> contributors = note.getContributors();
        if (contributors == null) {
            contributors = new ArrayList<>();
        }
        if (!contributors.contains(userId)) {
            contributors.add(userId);
        }

        note.setContributors(contributors);
        return notesDao.save(note);
    }
}
