package ru.lama.expertCookingSystem.drools.dto;

public record DroolsSesResponse(
    Long recipeId,
    boolean sessionCompleted
) {

}
