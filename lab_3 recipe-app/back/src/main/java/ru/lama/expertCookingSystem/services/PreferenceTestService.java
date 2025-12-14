package ru.lama.expertCookingSystem.services;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.lama.expertCookingSystem.dto.AnswerPrefOptionDto;
import ru.lama.expertCookingSystem.dto.QuestionPrefDto;
import ru.lama.expertCookingSystem.entity.PreferenceQuestion;
import ru.lama.expertCookingSystem.entity.QuestionOption;
import ru.lama.expertCookingSystem.repositories.PreferenceQuestionRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class PreferenceTestService {

  private final PreferenceQuestionRepository preferenceQuestionRepository;

  /**
   * Получить все вопросы теста предпочтений
   */
  public List<QuestionPrefDto> getAllPreferenceQuestions() {
    log.info("Getting all preference questions from database");

    List<PreferenceQuestion> questions = preferenceQuestionRepository.findAllByOrderBySortOrderAsc();

    log.info("Found {} preference questions", questions.size());
    return questions.stream()
        .map(this::convertToQuestionDto)
        .collect(Collectors.toList());
  }

  private QuestionPrefDto convertToQuestionDto(PreferenceQuestion question) {
    List<AnswerPrefOptionDto> optionDtos = question.getOptions().stream()
        .map(this::convertToAnswerOptionDto)
        .collect(Collectors.toList());

    return new QuestionPrefDto(
        question.getFieldName(),
        question.getQuestionText(),
        question.getQuestionType(),
        optionDtos
    );
  }

  private AnswerPrefOptionDto convertToAnswerOptionDto(QuestionOption option) {
    return new AnswerPrefOptionDto(
        option.getOptionKey(),
        option.getOptionText(),
        option.getEmoji(),
        option.getIsDefault(),
        null
    );
  }
}