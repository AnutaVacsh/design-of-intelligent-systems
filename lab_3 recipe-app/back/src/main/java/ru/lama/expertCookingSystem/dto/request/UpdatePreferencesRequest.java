package ru.lama.expertCookingSystem.dto.request;

import java.util.List;

public record UpdatePreferencesRequest(
    Long dietTypeId,
    List<String> allergies,
    List<String> excludedIngredients
) {

}
