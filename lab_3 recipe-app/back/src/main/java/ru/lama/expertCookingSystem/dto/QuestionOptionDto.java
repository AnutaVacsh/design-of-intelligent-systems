package ru.lama.expertCookingSystem.dto;

public record QuestionOptionDto(
    Long id,
    String optionKey,
    String optionText,
    String emoji,
    Boolean isDefault,
    Integer sortOrder
) {}