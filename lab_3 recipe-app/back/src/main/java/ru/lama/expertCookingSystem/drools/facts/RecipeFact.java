package ru.lama.expertCookingSystem.drools.facts;

import java.util.List;
import lombok.Data;

@Data
public class RecipeFact {
  private Long recipeId;
  private String name;
  private String description;
  private boolean vegetarian;
  private boolean vegan;
  private boolean glutenFree;
  private boolean dairyFree;
  private Integer prepTime;
  private Integer cookTime;
  private String difficultyLevel;
  private String cuisineType;
  private Integer matchScore;
  private List<String> ingredients;

  public RecipeFact() {}

  public RecipeFact(Long recipeId, String name) {
    this.recipeId = recipeId;
    this.name = name;
    this.matchScore = 0;
  }
}