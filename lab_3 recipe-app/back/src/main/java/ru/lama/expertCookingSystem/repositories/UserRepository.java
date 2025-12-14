package ru.lama.expertCookingSystem.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.lama.expertCookingSystem.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  boolean existsByUsername(String username);

  boolean existsByEmail(String email);

  Optional<User> findByUsername(String username);

  Optional<User> findByEmail(String email);

  @Query("SELECT u FROM User u LEFT JOIN FETCH u.preferences WHERE u.id = :id")
  Optional<User> findByIdWithPreferences(@Param("id") Long id);
}
