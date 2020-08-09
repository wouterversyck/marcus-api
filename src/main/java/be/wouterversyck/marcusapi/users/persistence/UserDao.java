package be.wouterversyck.marcusapi.users.persistence;

import be.wouterversyck.marcusapi.users.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserDao extends JpaRepository<User, Long> {
    <T> Optional<T> findByUsername(String username, Class<T> type);
    <T> Optional<T> findByEmail(String email, Class<T> type);
    <T> Page<T> findAllProjectedBy(Pageable pageable, Class<T> type);
}
