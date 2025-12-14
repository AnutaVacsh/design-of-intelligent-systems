package ru.lama.expertCookingSystem.dto;

import java.time.LocalDateTime;
import java.util.List;

// For session management
public record SessionAnswerData(
    String questionId,
    List<String> selectedAnswers,
    LocalDateTime answeredAt
) {

}
