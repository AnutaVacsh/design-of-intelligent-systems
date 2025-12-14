package ru.lama.expertCookingSystem.dto.response;

import ru.lama.expertCookingSystem.dto.UserPreferencesDto;

public record UserProfileResponse(
    String username,
    String email,
    UserPreferencesDto preferences
) {}
