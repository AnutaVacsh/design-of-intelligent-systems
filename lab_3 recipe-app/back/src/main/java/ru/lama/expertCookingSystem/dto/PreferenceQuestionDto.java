package ru.lama.expertCookingSystem.dto;

import java.util.List;

public record PreferenceQuestionDto(
    Long id,
    String fieldName,
    String questionText,
    String questionType,
    Integer sortOrder,
    List<QuestionOptionDto> options
) {}