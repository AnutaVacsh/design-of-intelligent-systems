package ru.lama.expertCookingSystem.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.lama.expertCookingSystem.entity.UserRecipeHistory;

@Repository
public interface UserRecipeHistoryRepository extends JpaRepository<UserRecipeHistory, Long> {

  Optional<UserRecipeHistory> findByUserIdAndRecipeId(Long userId, Long recipeId);

  List<UserRecipeHistory> findByUserIdOrderByMatchedAtDesc(Long userId);

  boolean existsByUserIdAndRecipeId(Long userId, Long recipeId);

  Long countByUserId(Long userId);

  Long countByUserIdAndRatingIsNotNull(Long userId);
}
