package ru.lama.expertCookingSystem.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.lama.expertCookingSystem.entity.UserPreferences;

@Repository
public interface UserPreferencesRepository extends JpaRepository<UserPreferences, Long> {

  Optional<UserPreferences> findByUserId(Long userId);
}

