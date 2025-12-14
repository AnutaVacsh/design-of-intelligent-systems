package ru.lama.expertCookingSystem.dto.request;

// Auth
public record SignupRequest(
    String username,
    String email,
    String password
) {

}
