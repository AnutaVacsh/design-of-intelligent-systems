package ru.lama.expertCookingSystem.dto;

import java.time.LocalDateTime;

public record RecipeHistoryDto(
    Long recipeId,
    String name,
    String imageUrl,
    LocalDateTime matchedAt,
    Integer rating
) {

}
