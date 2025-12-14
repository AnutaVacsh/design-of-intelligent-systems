package ru.lama.expertCookingSystem.enams;

/**
 * Единицы измерения ингредиентов
 */
public enum MeasurementUnit {
  GRAM("г", "грамм"),
  KILOGRAM("кг", "килограмм"),
  MILLILITER("мл", "миллилитр"),
  LITER("л", "литр"),
  PIECE("шт", "штука"),
  TEASPOON("ч.л.", "чайная ложка"),
  TABLESPOON("ст.л.", "столовая ложка"),
  CUP("стак.", "стакан"),
  PINCH("щеп.", "щепотка"),
  TO_TASTE("по вкусу", "по вкусу");

  private final String abbreviation;
  private final String fullName;

  MeasurementUnit(String abbreviation, String fullName) {
    this.abbreviation = abbreviation;
    this.fullName = fullName;
  }

  public String getAbbreviation() {
    return abbreviation;
  }

  public String getFullName() {
    return fullName;
  }
}
