package ru.lama.expertCookingSystem.drools.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.ObjectFilter;
import org.springframework.stereotype.Service;
import ru.lama.expertCookingSystem.drools.facts.*;
import ru.lama.expertCookingSystem.dto.QuestionDto;

import java.util.Collection;
import java.util.Optional;
import ru.lama.expertCookingSystem.entity.User;
import ru.lama.expertCookingSystem.services.UserService;

@Slf4j
@Service
@RequiredArgsConstructor
public class DroolsEngineService {

  private final KieContainer kieContainer;
  private final UserService userService;

  /**
   * Получить первый вопрос
   */
  public QuestionDto getFirstQuestion(Long userId) {
    KieSession kieSession = kieContainer.newKieSession();
    try {
      SessionFact sessionFact = new SessionFact();
      sessionFact.setCurrentState("INITIAL");

      UserPreferencesFact prefFact = createUserPreferencesFact(userId);

      kieSession.insert(sessionFact);
      kieSession.insert(prefFact);

      kieSession.fireAllRules();

      Optional<QuestionFact> question = findQuestionInSession(kieSession);
      return question.map(this::convertToQuestionDto)
          .orElseThrow(() -> new RuntimeException("Не удалось создать первый вопрос"));

    } finally {
      kieSession.dispose();
    }
  }

  /**
   * Получить следующий вопрос по ответу
   */
  public Object getNextQuestion(Long userId, String previousQuestionId, List<String> answers) {

    KieSession kieSession = kieContainer.newKieSession();

    try {
      // ---- 1. Создаём предпочтения из готового метода ----
      UserPreferencesFact prefFact = createUserPreferencesFact(userId);

      // ---- 2. Основные факты ----
      SessionFact sessionFact = new SessionFact();
      sessionFact.setCurrentState(previousQuestionId);

      AnswerFact answerFact = new AnswerFact();
      answerFact.setQuestionId(previousQuestionId);
      answerFact.setSelectedAnswers(answers);

      // ---- 3. Вставляем факты ----
      kieSession.insert(prefFact);
      kieSession.insert(sessionFact);
      kieSession.insert(answerFact);

      // ---- 4. Запускаем правила ----
      kieSession.fireAllRules();

      // ---- 5. Финальный результат ----
      if (sessionFact.isSessionCompleted()) {
        return sessionFact.getRecommendedRecipeId();
      } else {
        Optional<QuestionFact> nextQuestion = findQuestionInSession(kieSession);
        return nextQuestion
            .map(this::convertToQuestionDto)
            .orElseThrow(() -> new RuntimeException("Не удалось получить следующий вопрос"));
      }

    } finally {
      kieSession.dispose();
    }
  }

  // Вспомогательные методы остаются теми же
  private Optional<QuestionFact> findQuestionInSession(KieSession kieSession) {
    Collection<?> questions = kieSession.getObjects(new QuestionFactFilter());
    return questions.stream()
        .map(obj -> (QuestionFact) obj)
        .findFirst();
  }

  private QuestionDto convertToQuestionDto(QuestionFact question) {
    java.util.List<ru.lama.expertCookingSystem.dto.AnswerOptionDto> answerDtos = question.getAnswers().stream()
        .map(answer -> new ru.lama.expertCookingSystem.dto.AnswerOptionDto(
            answer.getAnswerId(),
            answer.getAnswerText()
        ))
        .toList();

    return new QuestionDto(
        question.getQuestionId(),
        question.getQuestionText(),
        question.getQuestionType(),
        answerDtos
    );
  }

  private static class QuestionFactFilter implements ObjectFilter {
    @Override
    public boolean accept(Object object) {
      return object instanceof QuestionFact;
    }
  }

  private UserPreferencesFact createUserPreferencesFact(Long userId) {
    User profile = userService.getByUserId(userId);

    UserPreferencesFact pref = new UserPreferencesFact();
    pref.setUserId(userId);
    pref.setDietType(profile.getPreferences().getDietType() != null
        ? profile.getPreferences().getDietType().getName()
        : null);
    pref.setAllergies(profile.getPreferences().getAllergies());
    pref.setExcludedIngredients(profile.getPreferences().getExcludedIngredients());

    return pref;
  }

}