package ru.lama.expertCookingSystem.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.lama.expertCookingSystem.entity.UserSession;

@Repository
public interface UserSessionRepository extends JpaRepository<UserSession, Long> {

  Optional<UserSession> findBySessionToken(String s);
}
