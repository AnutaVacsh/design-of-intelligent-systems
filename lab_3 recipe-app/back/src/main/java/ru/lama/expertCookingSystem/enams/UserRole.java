package ru.lama.expertCookingSystem.enams;

/**
 * Роли пользователей
 */
public enum UserRole {
  USER("Пользователь"),
  ADMIN("Администратор"),
  MODERATOR("Модератор");

  private final String displayName;

  UserRole(String displayName) {
    this.displayName = displayName;
  }

  public String getDisplayName() {
    return displayName;
  }
}
