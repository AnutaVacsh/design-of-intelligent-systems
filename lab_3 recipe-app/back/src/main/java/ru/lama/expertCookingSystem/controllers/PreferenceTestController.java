package ru.lama.expertCookingSystem.controllers;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import ru.lama.expertCookingSystem.api.PreferenceTestApi;
import ru.lama.expertCookingSystem.dto.QuestionPrefDto;
import ru.lama.expertCookingSystem.dto.SubmitPreferenceTestRequest;
import ru.lama.expertCookingSystem.dto.UserPreferencesDto;
import ru.lama.expertCookingSystem.dto.response.ApiResponse;
import ru.lama.expertCookingSystem.services.PreferenceTestService;
import ru.lama.expertCookingSystem.services.UserService;

@Slf4j
@RestController
@CrossOrigin(origins = "*", allowCredentials = "false")
@RequiredArgsConstructor
public class PreferenceTestController implements PreferenceTestApi {

  private final UserService userService;
  private final PreferenceTestService preferenceTestService;

  @Override
  public ResponseEntity<ApiResponse<List<QuestionPrefDto>>> getPreferenceQuestions(Long userId) {
    try {
      log.info("GET /api/preference-test/questions - userId: {}", userId);

      List<QuestionPrefDto> questions = preferenceTestService.getAllPreferenceQuestions();

      log.info("Returning {} preference questions", questions.size());
      return ResponseEntity.ok(ApiResponse.success(questions));

    } catch (Exception e) {
      log.error("Error getting preference questions for user: {}", userId, e);
      return ResponseEntity.ok(ApiResponse.error("Ошибка при получении вопросов теста"));
    }
  }

  @Override
  public ResponseEntity<ApiResponse<UserPreferencesDto>> submitPreferenceTest(SubmitPreferenceTestRequest request) {
    return userService.updatePreferences(request.userId(), request.preferences());
  }
}
