package ru.lama.expertCookingSystem.dto.response;

import ru.lama.expertCookingSystem.dto.RecipeDto;

public record RecommendationResponse(
    RecipeDto recipe,
    boolean sessionCompleted
) {

}
