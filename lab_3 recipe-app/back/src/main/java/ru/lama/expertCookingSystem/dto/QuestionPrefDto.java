package ru.lama.expertCookingSystem.dto;

import java.util.List;

public record QuestionPrefDto(
    String questionId,           // "diet_type", "allergies", "excluded_ingredients"
    String questionText,         // текст вопроса
    String questionType,
    List<AnswerPrefOptionDto> options
) {}
