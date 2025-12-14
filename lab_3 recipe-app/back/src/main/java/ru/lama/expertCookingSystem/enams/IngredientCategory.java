package ru.lama.expertCookingSystem.enams;

/**
 * Категории ингредиентов (соответствует таблице ingredient_categories)
 */
public enum IngredientCategory {
  VEGETABLE("Овощи"),
  FRUIT("Фрукты"),
  MEAT("Мясо"),
  FISH("Рыба и морепродукты"),
  DAIRY("Молочные продукты"),
  GRAIN("Зерновые"),
  SPICE("Специи и приправы"),
  NUT("Орехи и семена"),
  OIL("Масла и жиры"),
  LEGUME("Бобовые"),
  HERB("Зелень"),
  MUSHROOM("Грибы"),
  EGG("Яйца"),
  FLOUR("Мука и мучные изделия");

  private final String displayName;

  IngredientCategory(String displayName) {
    this.displayName = displayName;
  }

  public String getDisplayName() {
    return displayName;
  }
}
