package ru.lama.expertCookingSystem.enams;

/**
 * Уровень сложности рецепта
 */
public enum DifficultyLevel {
  EASY("Легкий"),
  MEDIUM("Средний"),
  HARD("Сложный");

  private final String displayName;

  DifficultyLevel(String displayName) {
    this.displayName = displayName;
  }

  public String getDisplayName() {
    return displayName;
  }
}
