package ru.lama.expertCookingSystem.dto.request;

import java.util.List;

public record AnswerRequest(
    String sessionId,
    String questionId,
    List<String> answers // выбранные ответы
) {

}
