package ru.lama.expertCookingSystem.dto;

import java.util.List;

public record UserPreferencesDto(
    String dietType,
    List<String> allergies,
    List<String> excludedIngredients
) {

}
