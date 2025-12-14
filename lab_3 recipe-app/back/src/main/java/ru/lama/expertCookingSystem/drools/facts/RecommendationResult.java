package ru.lama.expertCookingSystem.drools.facts;

import lombok.Data;

@Data
public class RecommendationResult {
  private String sessionId;
  private Long recipeId;
  private String recipeName;
  private Integer matchScore;
  private String reasoning;

  public RecommendationResult() {}

  public RecommendationResult(String sessionId, Long recipeId, String recipeName) {
    this.sessionId = sessionId;
    this.recipeId = recipeId;
    this.recipeName = recipeName;
    this.matchScore = 100;
  }
}