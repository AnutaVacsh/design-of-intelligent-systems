package ru.lama.expertCookingSystem.dto;

import ru.lama.expertCookingSystem.dto.request.UpdatePreferencesRequest;

public record SubmitPreferenceTestRequest(
    Long userId,
    UpdatePreferencesRequest preferences
) {

}
