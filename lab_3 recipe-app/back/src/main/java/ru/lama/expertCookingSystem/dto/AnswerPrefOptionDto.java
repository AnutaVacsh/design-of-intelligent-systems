package ru.lama.expertCookingSystem.dto;

public record AnswerPrefOptionDto(
    String optionKey,
    String optionText,
    String emoji,
    Boolean isDefault,
    String description
) {

}
