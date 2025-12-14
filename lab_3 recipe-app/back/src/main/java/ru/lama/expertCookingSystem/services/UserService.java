package ru.lama.expertCookingSystem.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.lama.expertCookingSystem.dto.RecipeHistoryDto;
import ru.lama.expertCookingSystem.dto.UserPreferencesDto;
import ru.lama.expertCookingSystem.dto.request.UpdatePreferencesRequest;
import ru.lama.expertCookingSystem.dto.response.ApiResponse;
import ru.lama.expertCookingSystem.dto.response.UserProfileResponse;
import ru.lama.expertCookingSystem.entity.*;
import ru.lama.expertCookingSystem.mappers.RecipeMapper;
import ru.lama.expertCookingSystem.repositories.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

  private final UserRepository userRepository;
  private final UserPreferencesRepository userPreferencesRepository;
  private final UserRecipeHistoryRepository userRecipeHistoryRepository;
  private final RecipeRepository recipeRepository;
  private final DietTypeRepository dietTypeRepository;
  private final RecipeMapper recipeMapper;

  public User getByUserId(Long userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("User not found: " + userId));
  }

  public ResponseEntity<ApiResponse<UserProfileResponse>> getProfile(Long userId) {
    try {
      log.info("Getting profile for user: {}", userId);

      Optional<User> userOpt = userRepository.findByIdWithPreferences(userId);
      if (userOpt.isEmpty()) {
        log.warn("User not found: {}", userId);
        return ResponseEntity.ok(ApiResponse.error("Пользователь не найден"));
      }

      User user = userOpt.get();
      UserProfileResponse profileResponse = buildUserProfileResponse(user);

      log.info("Successfully retrieved profile for user: {}", user.getUsername());
      return ResponseEntity.ok(ApiResponse.success(profileResponse));

    } catch (Exception e) {
      log.error("Error getting profile for user: {}", userId, e);
      return ResponseEntity.ok(ApiResponse.error("Ошибка при получении профиля"));
    }
  }

  @Transactional
  public ResponseEntity<ApiResponse<UserPreferencesDto>> updatePreferences(Long userId,
      UpdatePreferencesRequest request) {
    try {
      log.info("Updating preferences for user: {}", userId);

      Optional<User> userOpt = userRepository.findById(userId);
      if (userOpt.isEmpty()) {
        log.warn("User not found: {}", userId);
        return ResponseEntity.ok(ApiResponse.error("Пользователь не найден"));
      }

      User user = userOpt.get();
      UserPreferences preferences = userPreferencesRepository.findByUserId(userId)
          .orElse(new UserPreferences());

      // Обновляем тип диеты
      if (request.dietTypeId() != null) {
        Optional<DietType> dietTypeOpt = dietTypeRepository.findById(request.dietTypeId());
        if (dietTypeOpt.isPresent()) {
          preferences.setDietType(dietTypeOpt.get());
        } else {
          log.warn("Diet type not found: {}", request.dietTypeId());
          return ResponseEntity.ok(ApiResponse.error("Тип диеты не найден"));
        }
      }

      // Обновляем аллергии и исключенные ингредиенты
      if (request.allergies() != null) {
        preferences.setAllergies(request.allergies());
      }
      if (request.excludedIngredients() != null) {
        preferences.setExcludedIngredients(request.excludedIngredients());
      }

      // Если это новые предпочтения, устанавливаем пользователя
      if (preferences.getId() == null) {
        preferences.setUser(user);
      }

      preferences.setUpdatedAt(LocalDateTime.now());
      UserPreferences savedPreferences = userPreferencesRepository.save(preferences);

      UserPreferencesDto preferencesDto = buildUserPreferencesDto(savedPreferences);

      log.info("Successfully updated preferences for user: {}", userId);
      return ResponseEntity.ok(ApiResponse.success(preferencesDto));

    } catch (Exception e) {
      log.error("Error updating preferences for user: {}", userId, e);
      return ResponseEntity.ok(ApiResponse.error("Ошибка при обновлении предпочтений"));
    }
  }

  public ResponseEntity<ApiResponse<List<RecipeHistoryDto>>> getRecipeHistory(Long userId) {
    try {
      log.info("Getting recipe history for user: {}", userId);

      if (!userRepository.existsById(userId)) {
        log.warn("User not found: {}", userId);
        return ResponseEntity.ok(ApiResponse.error("Пользователь не найден"));
      }

      List<UserRecipeHistory> history = userRecipeHistoryRepository.findByUserIdOrderByMatchedAtDesc(userId);
      List<RecipeHistoryDto> historyDtos = recipeMapper.toRecipeHistoryDtoList(history);

      log.info("Successfully retrieved {} history items for user: {}", historyDtos.size(), userId);
      return ResponseEntity.ok(ApiResponse.success(historyDtos));

    } catch (Exception e) {
      log.error("Error getting recipe history for user: {}", userId, e);
      return ResponseEntity.ok(ApiResponse.error("Ошибка при получении истории"));
    }
  }

  @Transactional
  public ResponseEntity<ApiResponse<String>> addToHistory(Long userId, Long recipeId) {
    try {
      log.info("Adding recipe {} to history for user: {}", recipeId, userId);

      Optional<User> userOpt = userRepository.findById(userId);
      if (userOpt.isEmpty()) {
        log.warn("User not found: {}", userId);
        return ResponseEntity.ok(ApiResponse.error("Пользователь не найден"));
      }

      Optional<Recipe> recipeOpt = recipeRepository.findById(recipeId);
      if (recipeOpt.isEmpty()) {
        log.warn("Recipe not found: {}", recipeId);
        return ResponseEntity.ok(ApiResponse.error("Рецепт не найден"));
      }

      User user = userOpt.get();
      Recipe recipe = recipeOpt.get();

      // Проверяем, нет ли уже этой записи в истории
      if (!userRecipeHistoryRepository.existsByUserIdAndRecipeId(userId, recipeId)) {
        UserRecipeHistory history = new UserRecipeHistory();
        history.setUser(user);
        history.setRecipe(recipe);
        history.setMatchedAt(LocalDateTime.now());
        userRecipeHistoryRepository.save(history);

        log.info("Added recipe {} to history for user: {}", recipeId, userId);
      } else {
        log.info("Recipe {} already in history for user: {}", recipeId, userId);
      }

      return ResponseEntity.ok(ApiResponse.success("Рецепт добавлен в историю"));

    } catch (Exception e) {
      log.error("Error adding recipe to history for user: {}, recipe: {}", userId, recipeId, e);
      return ResponseEntity.ok(ApiResponse.error("Ошибка при добавлении в историю"));
    }
  }

  private UserProfileResponse buildUserProfileResponse(User user) {
    UserPreferencesDto preferencesDto = null;
    if (user.getPreferences() != null) {
      preferencesDto = buildUserPreferencesDto(user.getPreferences());
    }

    return new UserProfileResponse(
        user.getUsername(),
        user.getEmail(),
        preferencesDto
    );
  }

  private UserPreferencesDto buildUserPreferencesDto(UserPreferences preferences) {
    String dietTypeName = null;
    if (preferences.getDietType() != null) {
      dietTypeName = preferences.getDietType().getName();
    }

    return new UserPreferencesDto(
        dietTypeName,
        preferences.getAllergies(),
        preferences.getExcludedIngredients()
    );
  }

  /**
   * Дополнительный метод: получить статистику пользователя
   */
  public ResponseEntity<ApiResponse<UserStats>> getUserStats(Long userId) {
    try {
      if (!userRepository.existsById(userId)) {
        return ResponseEntity.ok(ApiResponse.error("Пользователь не найден"));
      }

      Long historyCount = userRecipeHistoryRepository.countByUserId(userId);
      Long ratedRecipesCount = userRecipeHistoryRepository.countByUserIdAndRatingIsNotNull(userId);

      UserStats stats = new UserStats(historyCount, ratedRecipesCount);
      return ResponseEntity.ok(ApiResponse.success(stats));

    } catch (Exception e) {
      log.error("Error getting user stats for user: {}", userId, e);
      return ResponseEntity.ok(ApiResponse.error("Ошибка при получении статистики"));
    }
  }

  // DTO для статистики пользователя
  public record UserStats(Long totalRecipes, Long ratedRecipes) {}
}