package ru.lama.expertCookingSystem.enams;

/**
 * Статусы сессии опросника
 */
public enum SessionStatus {
  STARTED("Начата"),
  IN_PROGRESS("В процессе"),
  COMPLETED("Завершена"),
  CANCELLED("Отменена"),
  EXPIRED("Истекла");

  private final String description;

  SessionStatus(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }
}
