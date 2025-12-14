package ru.lama.expertCookingSystem.enams;


public enum AllergenType {
  NUTS("Орехи"),
  DAIRY("Молочные продукты"),
  EGGS("Яйца"),
  FISH("Рыба"),
  SHELLFISH("Морепродукты"),
  GLUTEN("Глютен"),
  SOY("Соя"),
  SESAME("Кунжут"),
  LUPIN("Люпин"),
  MUSTARD("Горчица"),
  CELERY("Сельдерей"),
  SULFITES("Сульфиты");

  private final String displayName;

  AllergenType(String displayName) {
    this.displayName = displayName;
  }

  public String getDisplayName() {
    return displayName;
  }
}