package ru.lama.expertCookingSystem.mappers;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.lama.expertCookingSystem.dto.IngredientDto;
import ru.lama.expertCookingSystem.dto.RecipeDto;
import ru.lama.expertCookingSystem.dto.RecipeHistoryDto;
import ru.lama.expertCookingSystem.dto.response.RecommendationResponse;
import ru.lama.expertCookingSystem.entity.Recipe;
import ru.lama.expertCookingSystem.entity.RecipeIngredient;
import ru.lama.expertCookingSystem.entity.UserRecipeHistory;

@Mapper(componentModel = "spring")
public interface RecipeMapper {

  @Mapping(target = "vegetarian", source = "isVegetarian")
  @Mapping(target = "vegan", source = "isVegan")
  @Mapping(target = "glutenFree", source = "isGlutenFree")
  @Mapping(target = "dairyFree", source = "isDairyFree")
  RecipeDto toRecipeDto(Recipe recipe);

  List<RecipeDto> toRecipeDtoList(List<Recipe> recipes);

  @Mapping(target = "name", source = "ingredient.name")
  IngredientDto toIngredientDto(RecipeIngredient recipeIngredient);

  List<IngredientDto> toIngredientDtoList(List<RecipeIngredient> recipeIngredients);

  @Mapping(target = "recipeId", source = "recipe.id")
  @Mapping(target = "name", source = "recipe.name")
  @Mapping(target = "imageUrl", source = "recipe.imageUrl")
  RecipeHistoryDto toRecipeHistoryDto(UserRecipeHistory history);

  List<RecipeHistoryDto> toRecipeHistoryDtoList(List<UserRecipeHistory> histories);
}