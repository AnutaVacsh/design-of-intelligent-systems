package ru.lama.expertCookingSystem.dto;

import java.util.List;

// Session
public record QuestionDto(
    String questionId,
    String questionText,
    String questionType, // "SINGLE_SELECT", "MULTI_SELECT", "TEXT"
    List<AnswerOptionDto> answers
) {

}
