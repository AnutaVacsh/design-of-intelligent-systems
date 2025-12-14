package ru.lama.expertCookingSystem.api;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.lama.expertCookingSystem.dto.RecipeHistoryDto;
import ru.lama.expertCookingSystem.dto.UserPreferencesDto;
import ru.lama.expertCookingSystem.dto.request.UpdatePreferencesRequest;
import ru.lama.expertCookingSystem.dto.response.ApiResponse;
import ru.lama.expertCookingSystem.dto.response.UserProfileResponse;
import ru.lama.expertCookingSystem.util.ApiPaths;

@RequestMapping(ApiPaths.USERS)
public interface UserApi {

  @GetMapping(ApiPaths.USERS_PROFILE)
  ResponseEntity<ApiResponse<UserProfileResponse>> getProfile(@RequestParam Long userId);

  @PutMapping(ApiPaths.USERS_PREFERENCES)
  ResponseEntity<ApiResponse<UserPreferencesDto>> updatePreferences(
      @RequestParam Long userId,
      @RequestBody UpdatePreferencesRequest request);

  @GetMapping(ApiPaths.USERS_HISTORY)
  ResponseEntity<ApiResponse<List<RecipeHistoryDto>>> getRecipeHistory(@RequestParam Long userId);

  @PostMapping(ApiPaths.USERS_HISTORY)
  ResponseEntity<ApiResponse<String>> addToHistory(
      @RequestParam Long userId,
      @RequestParam Long recipeId);
}
