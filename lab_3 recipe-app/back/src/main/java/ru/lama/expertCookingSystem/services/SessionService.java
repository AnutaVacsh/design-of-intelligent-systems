package ru.lama.expertCookingSystem.services;

import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.lama.expertCookingSystem.drools.service.DroolsEngineService;
import ru.lama.expertCookingSystem.dto.RecipeDto;
import ru.lama.expertCookingSystem.dto.request.AnswerRequest;
import ru.lama.expertCookingSystem.dto.request.StartSessionRequest;
import ru.lama.expertCookingSystem.dto.response.ApiResponse;
import ru.lama.expertCookingSystem.dto.response.RecommendationResponse;
import ru.lama.expertCookingSystem.dto.response.SessionResponse;
import ru.lama.expertCookingSystem.entity.Recipe;
import ru.lama.expertCookingSystem.entity.User;
import ru.lama.expertCookingSystem.entity.UserSession;
import ru.lama.expertCookingSystem.mappers.RecipeMapper;
import ru.lama.expertCookingSystem.repositories.RecipeRepository;
import ru.lama.expertCookingSystem.repositories.UserRepository;
import ru.lama.expertCookingSystem.repositories.UserSessionRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SessionService {

  private final UserSessionRepository userSessionRepository;
  private final UserRepository userRepository;
  private final RecipeRepository recipeRepository;
  private final RecipeMapper recipeMapper;
  private final DroolsEngineService droolsService;

  @Transactional
  public ResponseEntity<ApiResponse<SessionResponse>> startSession(StartSessionRequest request) {
    try {
      log.info("Starting new session for user: {}", request.userId());

      User user = null;
      if (request.userId() != null) {
        Optional<User> userOpt = userRepository.findById(request.userId());
        if (userOpt.isEmpty()) {
          log.warn("User not found: {}", request.userId());
          return ResponseEntity.ok(ApiResponse.error("Пользователь не найден"));
        }
        user = userOpt.get();
      }

      var firstQuestion = droolsService.getFirstQuestion(user.getId());

      // Создаем сессию с текущим вопросом в currentState
      UserSession session = new UserSession();
      session.setUser(user);
      session.setSessionToken(generateSessionToken());
      session.setCurrentState(firstQuestion.questionId());
      session.setAnswersData("{}");
      session.setCreatedAt(LocalDateTime.now());
      session.setUpdatedAt(LocalDateTime.now());
      session.setExpiresAt(LocalDateTime.now().plusHours(1));

      UserSession savedSession = userSessionRepository.save(session);
      log.info("Created new session: {} with first question: {}",
          savedSession.getSessionToken(), firstQuestion.questionId());

      SessionResponse response = new SessionResponse(
          savedSession.getSessionToken(),
          firstQuestion
      );
      return ResponseEntity.ok(ApiResponse.success(response));

    } catch (Exception e) {
      log.error("Error starting session for user: {}", request.userId(), e);
      return ResponseEntity.ok(ApiResponse.error("Ошибка при создании сессии"));
    }
  }

  @Transactional
  public ResponseEntity<ApiResponse<Object>> answerQuestion(Long userId, AnswerRequest request) {
    try {
      log.info("Processing answer for session: {}, question: {}",
          request.sessionId(), request.questionId());

      // Находим сессию
      Optional<UserSession> sessionOpt = userSessionRepository.findBySessionToken(request.sessionId());
      if (sessionOpt.isEmpty()) {
        log.warn("Session not found: {}", request.sessionId());
        return ResponseEntity.ok(ApiResponse.error("Сессия не найдена"));
      }

      UserSession session = sessionOpt.get();

      // Проверяем, не истекла ли сессия
      if (session.getExpiresAt().isBefore(LocalDateTime.now())) {
        log.warn("Session expired: {}", request.sessionId());
        userSessionRepository.delete(session);
        return ResponseEntity.ok(ApiResponse.error("Сессия истекла"));
      }

      // Проверяем, что отвечаем на текущий вопрос
      if (!request.questionId().equals(session.getCurrentState())) {
        log.warn("Wrong question order. Expected: {}, got: {}",
            session.getCurrentState(), request.questionId());
        return ResponseEntity.ok(ApiResponse.error("Неверный порядок вопросов"));
      }

      // Получаем следующий вопрос или рецепт от Drools
      Object result = droolsService.getNextQuestion(userId, request.questionId(), request.answers());

      // Обновляем сессию

      updateSessionAnswers(session, request);
      session.setUpdatedAt(LocalDateTime.now());
      session.setExpiresAt(LocalDateTime.now().plusHours(1));

      if (result instanceof Long recipeId) {
        // Сессия завершена - возвращаем рецепт
        session.setCurrentState("COMPLETED");
        userSessionRepository.save(session);

        log.info("Session completed with recipe: {}", recipeId);
        RecipeDto recipeDto = getRecipeById(recipeId);
        RecommendationResponse response = new RecommendationResponse(recipeDto, true);
        return ResponseEntity.ok(ApiResponse.success(response));

      } else if (result instanceof ru.lama.expertCookingSystem.dto.QuestionDto nextQuestion) {
        // Продолжаем опрос - обновляем текущий вопрос
        session.setCurrentState(nextQuestion.questionId());
        userSessionRepository.save(session);

        log.info("Next question: {}", nextQuestion.questionId());
        SessionResponse response = new SessionResponse(request.sessionId(), nextQuestion);
        return ResponseEntity.ok(ApiResponse.success(response));

      } else {
        throw new RuntimeException("Неизвестный результат от Drools: " + result.getClass());
      }

    } catch (Exception e) {
      log.error("Error processing answer for session: {}", request.sessionId(), e);
      return ResponseEntity.ok(ApiResponse.error("Ошибка при обработке ответа"));
    }
  }

  public ResponseEntity<ApiResponse<SessionResponse>> getCurrentSession(String sessionId) {
    try {
      log.info("Getting current session: {}", sessionId);

      Optional<UserSession> sessionOpt = userSessionRepository.findBySessionToken(sessionId);
      if (sessionOpt.isEmpty()) {
        log.warn("Session not found: {}", sessionId);
        return ResponseEntity.ok(ApiResponse.error("Сессия не найдена"));
      }

      UserSession session = sessionOpt.get();

      if (session.getExpiresAt().isBefore(LocalDateTime.now())) {
        log.warn("Session expired: {}", sessionId);
        userSessionRepository.delete(session);
        return ResponseEntity.ok(ApiResponse.error("Сессия истекла"));
      }

      if ("COMPLETED".equals(session.getCurrentState())) {
        return ResponseEntity.ok(ApiResponse.error("Сессия уже завершена"));
      }

      // TODO: Реализовать получение текущего вопроса по currentState
      // Пока возвращаем, что сессия активна
      return ResponseEntity.ok(ApiResponse.error("Сессия активна, используйте answerQuestion"));

    } catch (Exception e) {
      log.error("Error getting session: {}", sessionId, e);
      return ResponseEntity.ok(ApiResponse.error("Ошибка при получении сессии"));
    }
  }

  @Transactional
  public ResponseEntity<ApiResponse<String>> cancelSession(String sessionId) {
    try {
      log.info("Cancelling session: {}", sessionId);

      Optional<UserSession> sessionOpt = userSessionRepository.findBySessionToken(sessionId);
      if (sessionOpt.isPresent()) {
        userSessionRepository.delete(sessionOpt.get());
        log.info("Session cancelled and removed from database: {}", sessionId);
      } else {
        log.warn("Session not found for cancellation: {}", sessionId);
      }

      return ResponseEntity.ok(ApiResponse.success("Сессия отменена"));

    } catch (Exception e) {
      log.error("Error cancelling session: {}", sessionId, e);
      return ResponseEntity.ok(ApiResponse.error("Ошибка при отмене сессии"));
    }
  }

  // ========== ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ==========

  private String generateSessionToken() {
    return UUID.randomUUID().toString();
  }

  private void updateSessionAnswers(UserSession session, AnswerRequest request) {
    String currentAnswers = session.getAnswersData();

    String answersJson = request.answers().stream()
        .map(answer -> "\"" + answer + "\"")
        .collect(Collectors.joining(", ", "[", "]"));

    String newAnswer = String.format("\"%s\": %s", request.questionId(), answersJson);

    if ("{}".equals(currentAnswers)) {
      session.setAnswersData("{" + newAnswer + "}");
    } else {
      String withoutBrace = currentAnswers.substring(0, currentAnswers.length() - 1);
      session.setAnswersData(withoutBrace + ", " + newAnswer + "}");
    }

    log.info("Updated answers: {}", session.getAnswersData());
  }

  private RecipeDto getRecipeById(Long recipeId) {
    try {
      log.info("Getting recipe by id: {}", recipeId);

      Recipe recipe = recipeRepository.findById(recipeId)
          .orElseThrow(() -> {
            log.warn("Recipe not found: {}", recipeId);
            return new RuntimeException("Рецепт не найден");
          });


      return recipeMapper.toRecipeDto(recipe);

    } catch (Exception e) {
      log.error("Error getting recipe by id: {}", recipeId, e);
      throw new RuntimeException("Ошибка при получении рецепта: " + e.getMessage());
    }
  }
}