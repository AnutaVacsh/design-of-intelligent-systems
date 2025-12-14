package ru.lama.expertCookingSystem.enams;

/**
 * Типы диет (соответствует таблице diet_types)
 */
public enum DietType {
  NONE("Без ограничений"),
  VEGETARIAN("Вегетарианская"),
  VEGAN("Веганская"),
  GLUTEN_FREE("Безглютеновая"),
  DAIRY_FREE("Без молочных продуктов"),
  KETO("Кето"),
  PALEO("Палео"),
  LOW_CARB("Низкоуглеводная"),
  LOW_FAT("Низкожировая"),
  HIGH_PROTEIN("Высокобелковая");

  private final String displayName;

  DietType(String displayName) {
    this.displayName = displayName;
  }

  public String getDisplayName() {
    return displayName;
  }
}
