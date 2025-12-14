package ru.lama.expertCookingSystem.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.lama.expertCookingSystem.dto.request.AnswerRequest;
import ru.lama.expertCookingSystem.dto.request.StartSessionRequest;
import ru.lama.expertCookingSystem.dto.response.ApiResponse;
import ru.lama.expertCookingSystem.dto.response.SessionResponse;
import ru.lama.expertCookingSystem.util.ApiPaths;

@RequestMapping(ApiPaths.SESSIONS)
public interface SessionApi {

  @PostMapping(ApiPaths.SESSIONS_START)
  ResponseEntity<ApiResponse<SessionResponse>> startSession(@RequestBody StartSessionRequest request);

  @PostMapping(ApiPaths.SESSIONS_ANSWER)
  ResponseEntity<ApiResponse<Object>> answerQuestion(@RequestParam Long userId, @RequestBody AnswerRequest request);

  @GetMapping(ApiPaths.SESSIONS_CURRENT)
  ResponseEntity<ApiResponse<SessionResponse>> getCurrentSession(@RequestParam(required = false) String sessionId);

  @DeleteMapping(ApiPaths.SESSIONS_CANCEL)
  ResponseEntity<ApiResponse<String>> cancelSession(@PathVariable String sessionId);
}
