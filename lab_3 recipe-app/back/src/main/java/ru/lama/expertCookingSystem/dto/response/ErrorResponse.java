package ru.lama.expertCookingSystem.dto.response;

import java.time.LocalDateTime;

// For error responses
public record ErrorResponse(
    String error,
    String message,
    String path,
    LocalDateTime timestamp
) {

  public ErrorResponse(String error, String message, String path) {
    this(error, message, path, LocalDateTime.now());
  }
}
