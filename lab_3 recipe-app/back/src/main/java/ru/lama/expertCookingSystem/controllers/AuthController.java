package ru.lama.expertCookingSystem.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import ru.lama.expertCookingSystem.api.AuthApi;
import ru.lama.expertCookingSystem.dto.request.LoginRequest;
import ru.lama.expertCookingSystem.dto.request.SignupRequest;
import ru.lama.expertCookingSystem.dto.response.ApiResponse;
import ru.lama.expertCookingSystem.dto.response.AuthResponse;
import ru.lama.expertCookingSystem.services.AuthService;

@RestController
@CrossOrigin(origins = "*", allowCredentials = "false")
@RequiredArgsConstructor
public class AuthController implements AuthApi {

  private final AuthService authService;

  public ResponseEntity<ApiResponse<AuthResponse>> signup(SignupRequest request) {
    try {
      AuthResponse response = authService.registerUser(request);
      return ResponseEntity.ok(ApiResponse.success("User registered successfully", response));
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
    } catch (Exception e) {
      return ResponseEntity.internalServerError().body(ApiResponse.error("Registration failed due to server error"));
    }
  }

  /**
   * Обработка запроса аутентификации пользователя
   *
   * @param request данные для входа (username, password)
   * @return ResponseEntity с результатом аутентификации
   */
  @Override
  public ResponseEntity<ApiResponse<AuthResponse>> login(LoginRequest request) {
    try {
      AuthResponse response = authService.authenticateUser(request);
      return ResponseEntity.ok(ApiResponse.success("Login successful", response));
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
    } catch (Exception e) {
      return ResponseEntity.internalServerError().body(ApiResponse.error("Login failed due to server error"));
    }
  }
}
