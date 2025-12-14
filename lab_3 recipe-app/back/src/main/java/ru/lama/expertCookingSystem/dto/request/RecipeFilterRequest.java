package ru.lama.expertCookingSystem.dto.request;

// For search/filter
public record RecipeFilterRequest(
    String cuisineType,
    Integer maxPrepTime,
    Integer maxCookTime,
    String difficulty,
    Boolean vegetarian,
    Boolean vegan,
    Boolean glutenFree,
    Boolean dairyFree
) {

}
