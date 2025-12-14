package ru.lama.expertCookingSystem.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.lama.expertCookingSystem.dto.RecipeDto;
import ru.lama.expertCookingSystem.dto.request.RateRecipeRequest;
import ru.lama.expertCookingSystem.dto.request.RecipeBatchRequest;
import ru.lama.expertCookingSystem.dto.response.ApiResponse;
import ru.lama.expertCookingSystem.entity.Recipe;
import ru.lama.expertCookingSystem.entity.User;
import ru.lama.expertCookingSystem.entity.UserRecipeHistory;
import ru.lama.expertCookingSystem.mappers.RecipeMapper;
import ru.lama.expertCookingSystem.repositories.RecipeRepository;
import ru.lama.expertCookingSystem.repositories.UserRecipeHistoryRepository;
import ru.lama.expertCookingSystem.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecipeService {

  private final RecipeRepository recipeRepository;
  private final UserRepository userRepository;
  private final UserRecipeHistoryRepository userRecipeHistoryRepository;
  private final RecipeMapper recipeMapper;

  /**
   * Получить детали рецепта по ID
   */
  public ResponseEntity<ApiResponse<RecipeDto>> getRecipe(Long recipeId) {
    try {
      log.info("Getting recipe with id: {}", recipeId);

      Optional<Recipe> recipeOpt = recipeRepository.findById(recipeId);
      if (recipeOpt.isEmpty()) {
        log.warn("Recipe not found with id: {}", recipeId);
        return ResponseEntity.ok(ApiResponse.error("Рецепт не найден"));
      }

      RecipeDto recipeDto = recipeMapper.toRecipeDto(recipeOpt.get());
      log.info("Successfully retrieved recipe: {}", recipeDto.name());

      return ResponseEntity.ok(ApiResponse.success(recipeDto));

    } catch (Exception e) {
      log.error("Error getting recipe with id: {}", recipeId, e);
      return ResponseEntity.ok(ApiResponse.error("Ошибка при получении рецепта"));
    }
  }

  /**
   * Оценить рецепт
   */
  @Transactional
  public ResponseEntity<ApiResponse<String>> rateRecipe(Long recipeId, Long userId, RateRecipeRequest request) {
    try {
      log.info("Rating recipe {} by user {} with rating: {}", recipeId, userId, request.rating());

      // Получаем рецепт и пользователя (нужны объекты, а не только проверка существования)
      Optional<Recipe> recipeOpt = recipeRepository.findById(recipeId);
      if (recipeOpt.isEmpty()) {
        log.warn("Recipe not found for rating: {}", recipeId);
        return ResponseEntity.ok(ApiResponse.error("Рецепт не найден"));
      }

      Optional<User> userOpt = userRepository.findById(userId);
      if (userOpt.isEmpty()) {
        log.warn("User not found for rating: {}", userId);
        return ResponseEntity.ok(ApiResponse.error("Пользователь не найден"));
      }

      // Валидация рейтинга
      if (request.rating() == null || request.rating() < 1 || request.rating() > 5) {
        return ResponseEntity.ok(ApiResponse.error("Рейтинг должен быть от 1 до 5"));
      }

      Recipe recipe = recipeOpt.get();
      User user = userOpt.get();

      // Ищем существующую запись в истории
      Optional<UserRecipeHistory> historyOpt = userRecipeHistoryRepository
          .findByUserIdAndRecipeId(userId, recipeId);

      if (historyOpt.isPresent()) {
        // Обновляем оценку в существующей записи
        UserRecipeHistory history = historyOpt.get();
        history.setRating(request.rating());
        history.setMatchedAt(LocalDateTime.now()); // обновляем время
        userRecipeHistoryRepository.save(history);
        log.info("Updated rating for recipe {} by user {}: {}", recipeId, userId, request.rating());
      } else {
        // Создаем новую запись в истории с оценкой
        UserRecipeHistory history = new UserRecipeHistory();
        history.setUser(user);        // Устанавливаем объект User
        history.setRecipe(recipe);    // Устанавливаем объект Recipe
        history.setRating(request.rating());
        history.setMatchedAt(LocalDateTime.now());
        userRecipeHistoryRepository.save(history);
        log.info("Created new rating for recipe {} by user {}: {}", recipeId, userId, request.rating());
      }

      return ResponseEntity.ok(ApiResponse.success("Оценка сохранена"));

    } catch (Exception e) {
      log.error("Error rating recipe {} by user {}", recipeId, userId, e);
      return ResponseEntity.ok(ApiResponse.error("Ошибка при оценке рецепта"));
    }
  }

  /**
   * Получить рецепты по списку ID (для истории)
   */
  public ResponseEntity<ApiResponse<List<RecipeDto>>> getRecipesBatch(RecipeBatchRequest request) {
    try {
      log.info("Getting batch of recipes with ids: {}", request.recipeIds());

      if (request.recipeIds() == null || request.recipeIds().isEmpty()) {
        log.info("Empty recipe IDs list provided");
        return ResponseEntity.ok(ApiResponse.success(List.of()));
      }

      List<Recipe> recipes = recipeRepository.findAllById(request.recipeIds());

      if (recipes.isEmpty()) {
        log.info("No recipes found for provided IDs");
        return ResponseEntity.ok(ApiResponse.success(List.of()));
      }

      List<RecipeDto> recipeDtos = recipes.stream()
          .map(recipeMapper::toRecipeDto)
          .collect(Collectors.toList());

      log.info("Successfully retrieved {} recipes", recipeDtos.size());

      return ResponseEntity.ok(ApiResponse.success(recipeDtos));

    } catch (Exception e) {
      log.error("Error getting recipes batch: {}", request.recipeIds(), e);
      return ResponseEntity.ok(ApiResponse.error("Ошибка при получении рецептов"));
    }
  }

  /**
   * Дополнительный метод: получить популярные рецепты
   */
//  public ResponseEntity<ApiResponse<List<RecipeDto>>> getPopularRecipes(int limit) {
//    try {
//      log.info("Getting {} popular recipes", limit);
//
//      // Здесь можно реализовать логику получения популярных рецептов
//      // Например, по количеству оценок или среднему рейтингу
//      List<Recipe> recipes = recipeRepository.findTopRatedRecipes(limit);
//
//      List<RecipeDto> recipeDtos = recipes.stream()
//          .map(recipeMapper::toRecipeDto)
//          .collect(Collectors.toList());
//
//      return ResponseEntity.ok(ApiResponse.success(recipeDtos));
//
//    } catch (Exception e) {
//      log.error("Error getting popular recipes", e);
//      return ResponseEntity.ok(ApiResponse.error("Ошибка при получении популярных рецептов"));
//    }
//  }
}