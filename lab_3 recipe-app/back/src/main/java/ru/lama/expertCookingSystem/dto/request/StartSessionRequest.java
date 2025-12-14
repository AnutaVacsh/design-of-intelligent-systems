package ru.lama.expertCookingSystem.dto.request;

// Session
public record StartSessionRequest(
    Long userId // опционально, если сессия для авторизованного пользователя
) {

}
