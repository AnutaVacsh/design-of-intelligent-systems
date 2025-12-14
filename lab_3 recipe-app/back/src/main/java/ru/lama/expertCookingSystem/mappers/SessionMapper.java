package ru.lama.expertCookingSystem.mappers;//package ru.lama.expertCookingSystem.mappers;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.lama.expertCookingSystem.dto.AnswerOptionDto;
import ru.lama.expertCookingSystem.dto.QuestionDto;
import ru.lama.expertCookingSystem.dto.RecipeDto;
import ru.lama.expertCookingSystem.dto.response.RecommendationResponse;
import ru.lama.expertCookingSystem.dto.response.SessionResponse;
import ru.lama.expertCookingSystem.entity.UserSession;

@Mapper(componentModel = "spring")
public interface SessionMapper {

//  @Mapping(target = "sessionId", source = "sessionToken")
//  @Mapping(target = "currentQuestion", source = "currentQuestion")
//  @Mapping(target = "progress", source = "progress")
//  SessionResponse toSessionResponse(UserSession session, QuestionDto currentQuestion, Integer progress);

  RecommendationResponse toRecommendationResponse(RecipeDto recipe);

  default QuestionDto createQuestion(String questionId, String questionText, String questionType,
      List<AnswerOptionDto> answers) {
    return new QuestionDto(questionId, questionText, questionType, answers);
  }

  default AnswerOptionDto createAnswerOption(String answerId, String answerText) {
    return new AnswerOptionDto(answerId, answerText);
  }
}
