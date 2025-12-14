package ru.lama.expertCookingSystem.drools.facts;

import lombok.Data;

@Data
public class SessionFact {
  private String sessionId;
  private String currentState;
  private Integer progress;
  private boolean sessionCompleted;
  private Long recommendedRecipeId;
  private String recommendedRecipeName;

  private String userPreferences;
  private String dietaryRestrictions;
}