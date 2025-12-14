package ru.lama.expertCookingSystem.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import ru.lama.expertCookingSystem.api.UserApi;
import ru.lama.expertCookingSystem.dto.RecipeHistoryDto;
import ru.lama.expertCookingSystem.dto.UserPreferencesDto;
import ru.lama.expertCookingSystem.dto.request.UpdatePreferencesRequest;
import ru.lama.expertCookingSystem.dto.response.ApiResponse;
import ru.lama.expertCookingSystem.dto.response.UserProfileResponse;
import ru.lama.expertCookingSystem.services.UserService;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin(origins = "*", allowCredentials = "false")
@RequiredArgsConstructor
public class UserController implements UserApi {

  private final UserService userService;

  @Override
  public ResponseEntity<ApiResponse<UserProfileResponse>> getProfile(Long userId) {
    log.info("GET /api/users/profile - получение профиля пользователя: {}", userId);
    return userService.getProfile(userId);
  }

  @Override
  public ResponseEntity<ApiResponse<UserPreferencesDto>> updatePreferences(Long userId,
      UpdatePreferencesRequest request) {
    log.info("PUT /api/users/preferences - обновление предпочтений пользователя: {}", userId);
    return userService.updatePreferences(userId, request);
  }

  @Override
  public ResponseEntity<ApiResponse<List<RecipeHistoryDto>>> getRecipeHistory(Long userId) {
    log.info("GET /api/users/history - получение истории рецептов пользователя: {}", userId);
    return userService.getRecipeHistory(userId);
  }

  @Override
  public ResponseEntity<ApiResponse<String>> addToHistory(Long userId, Long recipeId) {
    log.info("POST /api/users/history - добавление рецепта {} в историю пользователя: {}", recipeId, userId);
    return userService.addToHistory(userId, recipeId);
  }
}