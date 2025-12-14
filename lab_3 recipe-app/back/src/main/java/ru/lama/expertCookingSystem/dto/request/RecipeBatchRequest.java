package ru.lama.expertCookingSystem.dto.request;

import java.util.List;

// For batch operations
public record RecipeBatchRequest(
    List<Long> recipeIds
) {

}
