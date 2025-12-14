package ru.lama.expertCookingSystem.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import ru.lama.expertCookingSystem.api.SessionApi;
import ru.lama.expertCookingSystem.dto.request.AnswerRequest;
import ru.lama.expertCookingSystem.dto.request.StartSessionRequest;
import ru.lama.expertCookingSystem.dto.response.ApiResponse;
import ru.lama.expertCookingSystem.dto.response.SessionResponse;
import ru.lama.expertCookingSystem.services.SessionService;

@Slf4j
@RestController
@CrossOrigin(origins = "*", allowCredentials = "false")
@RequiredArgsConstructor
public class SessionController implements SessionApi {

  private final SessionService sessionService;

  @Override
  public ResponseEntity<ApiResponse<SessionResponse>> startSession(StartSessionRequest request) {
    log.info("POST /api/sessions/start - начало новой сессии для пользователя: {}", request.userId());
    return sessionService.startSession(request);
  }

  @Override
  public ResponseEntity<ApiResponse<Object>> answerQuestion(Long userId, AnswerRequest request) {
    log.info("POST /api/sessions/answer - ответ на вопрос в сессии: {}", request.sessionId());
    return sessionService.answerQuestion(userId, request);
  }

  @Override
  public ResponseEntity<ApiResponse<SessionResponse>> getCurrentSession(String sessionId) {
    log.info("GET /api/sessions/current - получение текущей сессии: {}", sessionId);
    return sessionService.getCurrentSession(sessionId);
  }

  @Override
  public ResponseEntity<ApiResponse<String>> cancelSession(String sessionId) {
    log.info("DELETE /api/sessions/{} - отмена сессии", sessionId);
    return sessionService.cancelSession(sessionId);
  }
}