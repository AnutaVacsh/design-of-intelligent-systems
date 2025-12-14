package ru.lama.expertCookingSystem.enams;

/**
 * Статусы рецептов
 */
public enum RecipeStatus {
  ACTIVE("Активный"),
  INACTIVE("Неактивный"),
  DRAFT("Черновик"),
  PENDING_REVIEW("На модерации");

  private final String displayName;

  RecipeStatus(String displayName) {
    this.displayName = displayName;
  }

  public String getDisplayName() {
    return displayName;
  }
}
