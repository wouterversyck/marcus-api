package be.wouterversyck.shoppinglistapi.notes.services;

import be.wouterversyck.shoppinglistapi.notes.exceptions.ShoppingListNotFoundException;
import be.wouterversyck.shoppinglistapi.notes.models.Note;
import be.wouterversyck.shoppinglistapi.notes.persistence.NotesDao;
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
        log.info("Fetching shopping lists by user");
        return notesDao.findAllByContributors(userId);
    }

    public Note getByIdForUser(final String id, final long userId) throws ShoppingListNotFoundException {
        log.info("Fetching shopping lists with id: {} for user", id);
        return notesDao.findByIdAndContributors(id, userId).orElseThrow(
                () -> new ShoppingListNotFoundException(id)
        );
    }

    public void deleteForUser(final String id, final long userId) {
        notesDao.deleteByIdAndContributors(id, userId);
    }

    public void deleteAllByOwner(final long userId) {
        notesDao.deleteAllByOwner(userId);
    }

    public Note saveForUser(
            final Note request, final long userId) {
        log.info("Saving shopping list with id {}", request.getId());

        request.setOwner(userId);
        List<Long> contributors = request.getContributors();
        if (contributors == null) {
            contributors = new ArrayList<>();
        }
        if (!contributors.contains(userId)) {
            contributors.add(userId);
        }

        request.setContributors(contributors);
        return notesDao.save(request);
    }
}
