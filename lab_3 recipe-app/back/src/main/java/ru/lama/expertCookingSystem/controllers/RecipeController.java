package ru.lama.expertCookingSystem.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import ru.lama.expertCookingSystem.api.RecipeApi;
import ru.lama.expertCookingSystem.dto.RecipeDto;
import ru.lama.expertCookingSystem.dto.request.RateRecipeRequest;
import ru.lama.expertCookingSystem.dto.request.RecipeBatchRequest;
import ru.lama.expertCookingSystem.dto.response.ApiResponse;
import ru.lama.expertCookingSystem.services.RecipeService;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin(origins = "*", allowCredentials = "false")
@RequiredArgsConstructor
public class RecipeController implements RecipeApi {

  private final RecipeService recipeService;

  @Override
  public ResponseEntity<ApiResponse<RecipeDto>> getRecipe(Long recipeId) {
    log.info("GET /api/recipes/{} - получение рецепта", recipeId);
    return recipeService.getRecipe(recipeId);
  }

  @Override
  public ResponseEntity<ApiResponse<String>> rateRecipe(Long recipeId, Long userId, RateRecipeRequest request) {
    log.info("POST /api/recipes/{}/rating - оценка рецепта {} пользователем {}", recipeId, recipeId, userId);
    return recipeService.rateRecipe(recipeId, userId, request);
  }

  @Override
  public ResponseEntity<ApiResponse<List<RecipeDto>>> getRecipesBatch(RecipeBatchRequest request) {
    log.info("POST /api/recipes/batch - получение рецептов по списку ID: {}", request.recipeIds());
    return recipeService.getRecipesBatch(request);
  }
}