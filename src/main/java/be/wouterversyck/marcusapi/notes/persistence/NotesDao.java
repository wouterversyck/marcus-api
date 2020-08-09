package be.wouterversyck.marcusapi.notes.persistence;

import be.wouterversyck.marcusapi.notes.models.Note;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface NotesDao extends MongoRepository<Note, Long> {
    List<Note> findAllByOwner(long userId);
    List<Note> findAllByContributors(long contributorId);
    void deleteAllByOwner(long userId);
    Optional<Note> findByIdAndContributors(String noteId, long contributorId);
    void deleteByIdAndContributors(String id, long contributorId);
}
