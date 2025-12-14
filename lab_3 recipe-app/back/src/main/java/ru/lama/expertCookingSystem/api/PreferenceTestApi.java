package ru.lama.expertCookingSystem.api;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.lama.expertCookingSystem.dto.QuestionPrefDto;
import ru.lama.expertCookingSystem.dto.SubmitPreferenceTestRequest;
import ru.lama.expertCookingSystem.dto.UserPreferencesDto;
import ru.lama.expertCookingSystem.dto.response.ApiResponse;
import ru.lama.expertCookingSystem.util.ApiPaths;

@RequestMapping(ApiPaths.PREFERENCE_TEST)
public interface PreferenceTestApi {

  @GetMapping(ApiPaths.PREFERENCE_TEST_QUESTIONS)
  ResponseEntity<ApiResponse<List<QuestionPrefDto>>> getPreferenceQuestions(
      @RequestParam(required = false) Long userId
  );

  @PostMapping(ApiPaths.PREFERENCE_TEST_SUBMIT)
  ResponseEntity<ApiResponse<UserPreferencesDto>> submitPreferenceTest(
      @RequestBody SubmitPreferenceTestRequest request
  );
}
