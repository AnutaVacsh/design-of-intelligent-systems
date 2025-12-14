package ru.lama.expertCookingSystem.enams;

/**
 * Типы вопросов в опроснике
 */
public enum QuestionType {
  SINGLE_SELECT("Одиночный выбор"),
  MULTI_SELECT("Множественный выбор"),
  TEXT("Текстовый ответ");

  private final String description;

  QuestionType(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }
}
