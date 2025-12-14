package ru.lama.expertCookingSystem.dto;

import java.util.List;

// Recipes
public record RecipeDto(
    Long id,
    String name,
    String description,
    String instructions,
    String imageUrl,
    Integer prepTime,
    Integer cookTime,
    String difficultyLevel,
    Integer servings,
    String cuisineType,
    Boolean vegetarian,
    Boolean vegan,
    Boolean glutenFree,
    Boolean dairyFree,
    List<IngredientDto> ingredients
) {

}
