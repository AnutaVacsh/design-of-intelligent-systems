package ru.lama.expertCookingSystem.dto.response;

import ru.lama.expertCookingSystem.dto.QuestionDto;

public record SessionResponse(
    String sessionId,
    QuestionDto currentQuestion
) {

}
