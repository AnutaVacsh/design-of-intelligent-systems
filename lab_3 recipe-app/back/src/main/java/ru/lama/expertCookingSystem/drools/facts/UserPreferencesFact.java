package ru.lama.expertCookingSystem.drools.facts;

import lombok.Data;
import java.util.List;

@Data
public class UserPreferencesFact {
  private Long userId;
  private String dietType;
  private List<String> allergies;
  private List<String> excludedIngredients;
  private Integer maxCookingTime;
  private String difficultyLevel;
  private String cuisineType;

  public UserPreferencesFact() {}

  public UserPreferencesFact(Long userId) {
    this.userId = userId;
  }
}