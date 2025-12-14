package ru.lama.expertCookingSystem.api;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.lama.expertCookingSystem.dto.RecipeDto;
import ru.lama.expertCookingSystem.dto.request.RateRecipeRequest;
import ru.lama.expertCookingSystem.dto.request.RecipeBatchRequest;
import ru.lama.expertCookingSystem.dto.response.ApiResponse;
import ru.lama.expertCookingSystem.util.ApiPaths;

@RequestMapping(ApiPaths.RECIPES)
public interface RecipeApi {

  @GetMapping(ApiPaths.RECIPES_ID)
  ResponseEntity<ApiResponse<RecipeDto>> getRecipe(@PathVariable Long recipeId);

  @PostMapping(ApiPaths.RECIPES_RATING)
  ResponseEntity<ApiResponse<String>> rateRecipe(
      @PathVariable Long recipeId,
      @RequestParam Long userId,
      @RequestBody RateRecipeRequest request);

  @PostMapping(ApiPaths.RECIPES_BATCH)
  ResponseEntity<ApiResponse<List<RecipeDto>>> getRecipesBatch(@RequestBody RecipeBatchRequest request);
}
