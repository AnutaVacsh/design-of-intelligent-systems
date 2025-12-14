package ru.lama.expertCookingSystem.enams;

/**
 * Типы кухонь
 */
public enum CuisineType {
  RUSSIAN("Русская"),
  ITALIAN("Итальянская"),
  ASIAN("Азиатская"),
  EUROPEAN("Европейская"),
  MEDITERRANEAN("Средиземноморская"),
  AMERICAN("Американская"),
  MEXICAN("Мексиканская"),
  GEORGIAN("Грузинская"),
  JAPANESE("Японская"),
  CHINESE("Китайская"),
  INDIAN("Индийская"),
  FRENCH("Французская");

  private final String displayName;

  CuisineType(String displayName) {
    this.displayName = displayName;
  }

  public String getDisplayName() {
    return displayName;
  }
}
